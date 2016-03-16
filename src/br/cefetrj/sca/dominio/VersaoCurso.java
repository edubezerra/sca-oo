package br.cefetrj.sca.dominio;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public final class VersaoCurso {
	@Id
	@GeneratedValue
	Long id;

	private final String numero;

	@ManyToOne
	private Curso curso;

	@OneToMany(mappedBy = "versaoCurso", fetch=FetchType.EAGER)
	List<Disciplina> disciplinas;

	/**
	 * Inteiro contendo a quantidade de períodos mínimos para um curso.
	 */
	private Integer qtdPeriodoMinimo;
	
	@Transient
	private Integer periodoMaximo;
	
	@SuppressWarnings("unused")
	private VersaoCurso() {
		numero = null;
	}

	public VersaoCurso(String numero, Curso curso) {
		this.numero = numero;
		this.curso = curso;
	}

	public Long getId() {
		return id;
	}

	public String getNumero() {
		return numero;
	}

	@Override
	public String toString() {
		return "VersaoCurso [numero=" + numero + "]";
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Integer getQtdPeriodoMinimo() {
		return qtdPeriodoMinimo;
	}

	public void setQtdPeriodoMinimo(Integer qtdPeriodoMinimo) {
		if(qtdPeriodoMinimo == null || qtdPeriodoMinimo < 0)
			throw new IllegalArgumentException("Valor invalido para o período mínimo para o término de um curso.");
		
		this.qtdPeriodoMinimo = qtdPeriodoMinimo;
		this.periodoMaximo = (2 * qtdPeriodoMinimo) - 1;
	}

	public Integer getPeriodoMaximo() {
		return periodoMaximo;
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

	public void adicionarDisciplina(Disciplina disciplina) {
		if (!disciplinas.contains(disciplina)) {
			this.disciplinas.add(disciplina);
		}
	}
	
	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}
}
