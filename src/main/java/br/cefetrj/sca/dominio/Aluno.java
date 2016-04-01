package br.cefetrj.sca.dominio;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Aluno {

	private static final int TAM_MIN_MATRICULA = 10;
	private static final int TAM_MAX_MATRICULA = 11;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Matrícula do aluno, composta de <code>TAM_MATRICULA</code> carateres.
	 */
	private String matricula;

	@Embedded
	Pessoa pessoa;

	@OneToOne
	ProcessoIsencao processoIsencao;

	@OneToOne(cascade = CascadeType.ALL)
	private HistoricoEscolar historico;

	@ManyToOne
	private VersaoCurso versaoCurso;

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private Aluno() {
	}

	public Aluno(String nome, String cpf, String matricula, VersaoCurso versaoCurso) {
		if (cpf == null || cpf.equals("")) {
			throw new IllegalArgumentException("CPF deve ser fornecido.");
		}

		if (nome == null || nome.equals("")) {
			throw new IllegalArgumentException("Nome não pode ser vazio.");
		}
		if (matricula == null || matricula.equals("")) {
			throw new IllegalArgumentException("Matrícula não pode ser vazia.");
		}
		if (!(matricula.length() >= TAM_MIN_MATRICULA && matricula.length() <= TAM_MAX_MATRICULA)) {
			throw new IllegalArgumentException("Matrícula deve ter entre " + TAM_MIN_MATRICULA + " e "
					+ TAM_MAX_MATRICULA + " caracteres: " + matricula);
		}
		this.pessoa = new Pessoa(nome, cpf);
		this.matricula = matricula;
		this.versaoCurso = versaoCurso;
		this.historico = new HistoricoEscolar(this.versaoCurso);
	}

	public Aluno(String nome, String cpf, String matricula, VersaoCurso versaoCurso, Date dataNascimento,
			String enderecoEmail) {
		this(nome, cpf, matricula, versaoCurso);
		this.pessoa = new Pessoa(nome, dataNascimento, enderecoEmail);
	}

	public ProcessoIsencao getProcessoIsencao() {
		return processoIsencao;
	}

	public void setProcessoIsencao(ProcessoIsencao processoIsencao) {
		this.processoIsencao = processoIsencao;
	}

	public String getNome() {
		return this.pessoa.getNome();
	}

	public Date getDataNascimento() {
		return pessoa.getDataNascimento();
	}

	public String getMatricula() {
		return matricula;
	}

	public String getEmail() {
		return pessoa.getEmail().toString();
	}

	public String getCpf() {
		return this.pessoa.getCpf();
	}

	public void setVersaoCurso(VersaoCurso versaoCurso) {
		if (versaoCurso == null) {
			throw new IllegalArgumentException("Curso deve ser definido!");
		}
		this.versaoCurso = versaoCurso;
	}

	public VersaoCurso getVersaoCurso() {
		return versaoCurso;
	}

	public List<Disciplina> getDisciplinasPossiveis() {
		return historico.getDisciplinasPossiveis();
	}

	public HistoricoEscolar getHistorico() {
		return historico;
	}

	public void registrarMatricula(Turma turma) {
		this.historico.lancar(turma.getDisciplina(), EnumSituacaoAvaliacao.MATRICULA, turma.getPeriodo());
	}

	public void registrarNoHistoricoEscolar(Disciplina disciplina, EnumSituacaoAvaliacao situacaoFinal,
			PeriodoLetivo semestre) {
		HistoricoEscolar he = this.getHistorico();
		he.lancar(disciplina, situacaoFinal, semestre);
	}

	@Override
	public String toString() {
		return "Aluno [matricula=" + matricula + ", pessoa=" + pessoa + "]";
	}

}
