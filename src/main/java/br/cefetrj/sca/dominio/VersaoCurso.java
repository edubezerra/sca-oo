package br.cefetrj.sca.dominio;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;




import com.fasterxml.jackson.annotation.JsonIgnore;

import br.cefetrj.sca.dominio.atividadecomplementar.AtividadeComplementar;
import br.cefetrj.sca.dominio.atividadecomplementar.TabelaAtividadesComplementares;

/**
 * Representa uma versão de grade curricular de um curso.
 * 
 * @author Rebecca Salles
 * 
 */
@Entity
public final class VersaoCurso {

	@Id
	@GeneratedValue
	Long id;
	
	private final String numero;
	
	@ManyToOne
	private Curso curso;

	@OneToMany(mappedBy = "versaoCurso", fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
	List<Disciplina> disciplinas;
	
	@OneToMany
	Set<TabelaEquivalencias> tabelasEquivalencias;
	
	/**
	 * Inteiro contendo a quantidade de periodos minimos para um curso.
	 */
	private Integer qtdPeriodoMinimo;
	
	@Transient
	private Integer periodoMaximo;
	
	/**
	 * Carga horária mínima de disciplinas optativas.
	 */
	private Duration cargaHorariaMinOptativas = Duration.ofHours(0);
	
	/**
	 * Carga horária mínima de atividades complementares.
	 */
	private Duration cargaHorariaMinAitvComp = Duration.ofHours(0);

	/**
	 * Tabela de Atividades Complementares exigidas nesta grade.
	 */
	@OneToOne
	private TabelaAtividadesComplementares atividades = null;
	
	
	@SuppressWarnings("unused")
	private VersaoCurso() {
		numero = null;
	}

	public VersaoCurso(String numero, Curso curso) {
		
		if (curso == null) {
			throw new IllegalArgumentException("Curso não fornecido!");
		}
		if (numero == null || numero.isEmpty()) {
			throw new IllegalArgumentException("Início de vigência não fornecida!");
		}
		this.curso = curso;
		this.numero = numero;
	}
	
	public VersaoCurso(String numero, Curso curso,Duration chMinOpt,Duration chaMinAitv) {

		this(numero,curso);
		if (chMinOpt == null || chMinOpt.isNegative()) {
			throw new IllegalArgumentException("Carga horária mínima de disciplinas optativas não pode ser nula e deve ser maior ou igual a zero.");
		}
		if (chaMinAitv == null || chaMinAitv.isNegative()) {
			throw new IllegalArgumentException("Carga horária mínima de atividades complementares não pode ser nula e deve ser maior ou igual a zero.");
		}
	
		this.cargaHorariaMinOptativas = chMinOpt;
		this.cargaHorariaMinAitvComp = chaMinAitv;
	}

	public Long getId() {
		return id;
	}
	
	public TabelaAtividadesComplementares getTabelaAtividades() {
		return this.atividades;
	}
	
	public List<Disciplina> getDisciplinas() {
		return Collections.unmodifiableList(this.disciplinas);
	}

	/**
	 * Adiciona uma tabela de equivalencia de disciplinas na versao do curso.
	 * @param tabelasEquivalencias
	 */
	public void setTabelasEquivalencias(Set<TabelaEquivalencias> tabelasEquivalencias) {
		this.tabelasEquivalencias = tabelasEquivalencias;
	}

	/**
	 * Adiciona uma tabela de atividades complementares nesta grade.
	 */
	public void setTabelaAtividades(TabelaAtividadesComplementares tabelaAtiv) {
		this.atividades = tabelaAtiv;
	}
	
	/**
	 * Adiciona uma atividade complementar à tabela de atividades complementares desta grade.
	 * Caso a tabela de atividades complementares desta grade for nula, uma nova instância de
	 * TabelaAtividadesComplementares é criada contendo a atividade complementar passada como
	 * parâmetro. 
	 */
	public void adicionaAtividade(AtividadeComplementar ativ) {
		if(this.atividades == null)
			setTabelaAtividades(new TabelaAtividadesComplementares());
		this.atividades.adicionarAtividade(ativ);
	}
		
	/**
	 * Adiciona uma disciplina nesta grade.
	 */
	public void adicionarDisciplina(Disciplina d) {
		if (disciplinas.contains(d)) {
			throw new IllegalArgumentException(
					"Esta disciplina já está associada a esta grade.");
		}
		this.disciplinas.add(d);
	}
	
	public void adicionarTabelaEquivalencia(TabelaEquivalencias t) {
		if(this.tabelasEquivalencias == null) 
			setTabelasEquivalencias(new HashSet<>());
		
		this.tabelasEquivalencias.add(t);
	}
		
	public String getNumero() {
		return numero;
	}

	public Duration getCargaHorariaMinOptativas() {
		return cargaHorariaMinOptativas;
	}

	public Duration getCargaHorariaMinAitvComp() {
		return cargaHorariaMinAitvComp;
	}
	
	public void setCargaHorariaMinOptativas(Duration cargaHorariaMinOptativas) {
		this.cargaHorariaMinOptativas = cargaHorariaMinOptativas;
	}

	public void setCargaHorariaMinAitvComp(Duration cargaHorariaMinAitvComp) {
		this.cargaHorariaMinAitvComp = cargaHorariaMinAitvComp;
	}

	public Curso getCurso() {
		return curso;
	}
	
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	@Override
	public String toString() {
		return "VersaoCurso [numero=" + numero + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((curso == null) ? 0 : curso.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersaoCurso other = (VersaoCurso) obj;
		if (curso == null) {
			if (other.curso != null)
				return false;
		} else if (!curso.equals(other.curso))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	public Integer getQtdPeriodoMinimo() {
		return qtdPeriodoMinimo;
	}

	public Integer getPeriodoMaximo() {
		return periodoMaximo;
	}

	public void setQtdPeriodoMinimo(Integer qtdPeriodoMinimo) {
		if(qtdPeriodoMinimo == null || qtdPeriodoMinimo < 0)
			throw new IllegalArgumentException("Valor invalido para o período mínimo de término de um curso.");
		
		this.qtdPeriodoMinimo = qtdPeriodoMinimo;
		this.periodoMaximo = (2 * qtdPeriodoMinimo) - 1;
	}
}
