package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Turma {

	enum SituacaoTurma {
		ABERTA_PARA_INSCRICAO, EM_ANDAMENTO, FECHADA_PARA_LANCAMENTO
	};

	private static final int CAPACIDADE_PRESUMIDA = 40;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * O código da turma. Deve necessariamente ser composto de sete caracteres
	 * (e.g., 6100009)
	 */
	private String codigo;

	/**
	 * Capacidade máxima da turma. Se não for definido pelo construtor, seu
	 * valor presumido é <code>CAPACIDADE_PRESUMIDA</code>.
	 */
	private int capacidadeMaxima = CAPACIDADE_PRESUMIDA;

	/**
	 * Disciplina para a qual esta turma foi aberta.
	 */
	@ManyToOne
	private Disciplina disciplina;

	/**
	 * Professor responsável por esta turma.
	 */
	@ManyToOne
	private Professor professor;

	/**
	 * Informações sobre locais e horários de aulas dessa turma.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "TURMA_ID", referencedColumnName = "ID")
	private List<Aula> aulas = new ArrayList<>();

	/**
	 * Inscrições realizadas nesta turma.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "TURMA_ID", referencedColumnName = "ID")
	private Set<Inscricao> inscricoes = new HashSet<>();

	/**
	 * Semestre letivo em que esta turma é ofertada.
	 */
	@Embedded
	private PeriodoLetivo periodo;

	@SuppressWarnings("unused")
	private Turma() {
	}

	/**
	 * Cria uma turma com disciplina e código fornecidos como parâmetros. A
	 * capacidade máxima da turma criada é igual a
	 * <code>CAPACIDADE_PRESUMIDA</code>. O semestre letvo da turma criada é
	 * <code>SemestreLetivo.SEMESTRE_LETIVO_CORRENTE</code>.
	 * 
	 * @param disciplina
	 *            disciplina para a qual esta turma é criada.
	 * 
	 * @param codigo
	 *            código da turma.
	 * 
	 */
	public Turma(Disciplina disciplina, String codigo) {

		if (disciplina == null) {
			throw new IllegalArgumentException("Disciplina não fornecida!");
		}

		this.disciplina = disciplina;

		if (codigo == null || codigo.isEmpty()) {
			throw new IllegalArgumentException("Código da turma é obrigatório.");
		}

		this.codigo = codigo;

		this.periodo = PeriodoLetivo.PERIODO_CORRENTE;
		this.capacidadeMaxima = CAPACIDADE_PRESUMIDA;
	}

	/**
	 * Cria uma turma com disciplina, código, número de vagas e semestre letivo
	 * fornecidos como parâmetros. A capacidade máxima da turma criada é igual a
	 * 
	 * @param disciplina
	 *            disciplina para a qual a turma é aberta
	 * 
	 * @param codigo
	 *            código da turma
	 * 
	 * @param numeroVagas
	 *            quantidades de vagas oferecidas na turma
	 * 
	 * @param periodo
	 *            período letivo em que a turma é ofertada
	 */
	public Turma(Disciplina disciplina, String codigo, Integer numeroVagas, PeriodoLetivo periodo) {

		this(disciplina, codigo);

		if (numeroVagas == null) {
			throw new IllegalArgumentException("Número de vagas indefinido!");
		}
		if (numeroVagas <= 0) {
			throw new IllegalArgumentException("Número de vagas deve ser positivo.");
		}
		this.capacidadeMaxima = numeroVagas;

		if (periodo == null) {
			throw new IllegalArgumentException("Período é obrigatório.");
		}
		this.periodo = periodo;
	}

	public Long getId() {
		return id;
	}

	public PeriodoLetivo getPeriodo() {
		return this.periodo;
	}

	public int getCapacidadeMaxima() {
		return capacidadeMaxima;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public Professor getProfessor() {
		return professor;
	}

	public String getCodigo() {
		return codigo;
	}

	public Set<Inscricao> getInscricoes() {
		return Collections.unmodifiableSet(this.inscricoes);
	}

	/**
	 * Inscreve o aluno passado como parâmetro nesta turma.
	 * 
	 * RN02: Uma turma não pode ter mais alunos inscritos do que a capacidade
	 * máxima definida para ela.
	 * 
	 * @param aluno
	 *            aluno para inscrever na turma.
	 */
	public void inscreverAluno(Aluno aluno) {
		if (inscricoes.size() + 1 > capacidadeMaxima) {
			throw new IllegalStateException(
					"Inscrição não realizada. Limite de vagas já alcançado (" + capacidadeMaxima + ")");
		} else {
			Inscricao inscricao = new Inscricao(aluno);
			for (Inscricao umaInscricao : inscricoes) {
				if (umaInscricao.getAluno().getMatricula().equals(aluno.getMatricula())) {
					throw new IllegalArgumentException("Aluno já inscrito na turma.");
				}
			}
			this.inscricoes.add(inscricao);
		}
	}

	/**
	 * Altera a capacidade máxima de inscrições para esta turma.
	 * 
	 * @param capacidadeMaxima
	 *            nova capacidade máxima de inscrições.
	 * 
	 * @throws IllegalArgumentException
	 *             se <code>capacidadeMaxima</code> é nulo ou se não é um número
	 *             positivo, ou se for um valor menor do que a quantidade atual
	 *             de inscrições.
	 */
	public void setCapacidadeMaxima(Integer capacidadeMaxima) {
		if (capacidadeMaxima == null) {
			throw new IllegalArgumentException("Número de vagas é obrigatório.");
		}
		if (capacidadeMaxima <= 0) {
			throw new IllegalArgumentException("Número de vagas deve ser positivo.");
		}
		if (capacidadeMaxima < inscricoes.size()) {
			throw new IllegalArgumentException("Há mais inscritos do que a capacidade máxima fornecida.");
		}
		this.capacidadeMaxima = capacidadeMaxima;
	}

	/**
	 * Verifica se aluno foi aprovado nesta turma.
	 * 
	 * @param a
	 *            referência para objeto aluno
	 * @return <code>true</code> se aluno passado como parâmetro foi aprovado
	 *         nessa turma; <code>false</code> em caso contrário.
	 */
	public boolean aprovado(Aluno a) {
		return false;
	}

	public boolean isAlunoInscrito(Aluno aluno) {
		if (aluno == null) {
			throw new IllegalArgumentException("Erro: Aluno não pode ser nulo.");
		}

		return getInscricao(aluno) != null;
	}

	private Inscricao getInscricao(Aluno aluno) {
		for (Inscricao inscricao : inscricoes) {
			if (inscricao.getAluno() == aluno) {
				return inscricao;
			}
		}
		return null;
	}

	public int getQtdInscritos() {
		return this.inscricoes.size();
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public void adicionarAula(String diaSemana, String horarioInicio, String horarioTermino, LocalAula local) {
		Aula a = new Aula(EnumDiaSemana.valueOf(diaSemana), horarioInicio, horarioTermino, local);
		this.aulas.add(a);
	}

	public String getNomeDisciplina() {
		return getDisciplina().getNome();
	}

	public String getNomeProfessor() {
		if (this.getProfessor() != null)
			return this.getProfessor().getNome();
		else
			return null;
	}

	public void lancarAvaliacao(FichaAvaliacoes.ItemFicha item) {
		Inscricao inscricao = localizarInscricao(item.matriculaAluno);
		if (inscricao != null) {
			verificarFaltas(item.qtdFaltas);
			try {
				inscricao.lancarAvaliacao(item);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(
						"Lançamento inválido para aluno de matrícula " + item.matriculaAluno + ": " + e.getMessage());
			}
		}
	}

	private void verificarFaltas(Integer qtdFaltas) {
		if (qtdFaltas != null && (qtdFaltas < 0 || qtdFaltas > this.getQtdAulas())) {
			throw new IllegalArgumentException("Quantidade de faltas inválida!");
		}
	}

	private Inscricao localizarInscricao(String matriculaAluno) {
		for (Inscricao inscricao : inscricoes) {
			if (inscricao.alunoTemMatricula(matriculaAluno)) {
				return inscricao;
			}
		}
		return null;
	}

	public String getCodigoDisciplina() {
		return this.getDisciplina().getCodigo();
	}

	public String getHorarioFormatado() {
		StringBuilder sb = new StringBuilder();
		int qtdAulas = this.aulas.size(), i = 0;
		if (qtdAulas == 0) {
			sb.append("indefinido");
		} else {
			for (Aula aula : this.aulas) {
				sb.append(aula.getDia());
				sb.append(", ");
				sb.append(aula.getHoraInicio());
				sb.append(" - ");
				sb.append(aula.getHoraTermino());
				if (++i < qtdAulas) {
					sb.append("; ");
				}
			}
		}
		return sb.toString();
	}

	public List<Aula> getAulas() {
		return aulas;
	}

	public Integer getQtdAulas() {
		return this.getDisciplina().getCargaHoraria();
	}

	@Override
	public String toString() {
		return "Turma [codigo=" + codigo + ", disciplina=" + disciplina + ", periodo=" + periodo + "]";
	}
	
	
}