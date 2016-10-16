package br.cefetrj.sca.dominio;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;


@Entity
public class Disciplina {
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Nome desta disciplina.
	 */
	private String nome;

	/**
	 * Código identificador desta disciplina.
	 */
	private String codigo;

	/**
	 * Quantidade de créditos desta disciplina.
	 */
	private Integer quantidadeCreditos;

	/**
	 * Carga horária (em horas-aula) desta disciplina durante sua oferta em um
	 * período letivo. Valores comuns desse atributo são 36, 54, 72 e 90.
	 */
	private int cargaHoraria;

	/**
	 * Indica se a disciplina é optativa ou obrigatória na grade curricular
	 * correspondente.
	 */
	boolean ehOptativa = false;

	/**
	 * Pré-requisitos desta disciplina. Uma disciplina pode ter zero ou mais
	 * pré-requisitos.
	 */
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "DISCIPLINA_PREREQS", joinColumns = { @JoinColumn(name = "GRADE_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "DISCIPLINA_ID", referencedColumnName = "ID") })
	private Set<Disciplina> preReqs = new HashSet<Disciplina>();

	/**
	 * A versão de curso a que esta disciplina pertence.
	 */
	@ManyToOne
	VersaoCurso versaoCurso;

	@SuppressWarnings("unused")
	private Disciplina() {
	}

	private Disciplina(String nome, String codigo, String quantidadeCreditos) {
		super();
		if (StringUtils.isBlank(nome)) {
			throw new IllegalArgumentException("Valor inválido para nome.");
		}
		this.nome = nome;
		if (StringUtils.isBlank(codigo)) {
			throw new IllegalArgumentException("Valor inválido para código.");
		}
		this.codigo = codigo;
		try {
			this.quantidadeCreditos = Integer.parseInt(quantidadeCreditos);
			if (this.quantidadeCreditos < 0) {
				throw new IllegalArgumentException(
						"Valor inválido para quantidade de créditos.");
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Valor inválido para quantidade de créditos.");
		}
	}

	public Disciplina(String codigo, String nome, String quantidadeCreditos,
			String cargaHoraria) {
		this(nome, codigo, quantidadeCreditos);
		try {
			this.cargaHoraria = Integer.parseInt(cargaHoraria);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Valor inválido para carga horária.");
		}
	}

	public String getNome() {
		return nome;
	}

	public String getCodigo() {
		return codigo;
	}

	public Integer getQuantidadeCreditos() {
		return quantidadeCreditos;
	}

	public void setQuantidadeCreditos(Integer quantidadeCreditos) {
		if (this.quantidadeCreditos < 0) {
			throw new IllegalArgumentException(
					"Valor inválido para quantidade de créditos: "
							+ quantidadeCreditos);
		}
		this.quantidadeCreditos = quantidadeCreditos;
	}

	public Set<Disciplina> getPreRequisitos() {
		return preReqs;
	}

	public Long getId() {
		return id;
	}

	public VersaoCurso getVersaoCurso() {
		return versaoCurso;
	}

	public void alocarEmVersao(VersaoCurso versaoCurso) {
		this.versaoCurso = versaoCurso;
	}

	@Override
	public String toString() {
		return "Disciplina [nome=" + nome + ", codigo=" + codigo
				+ ", versaoCurso=" + versaoCurso + "]";
	}

	public void comPreRequisito(Disciplina disciplina) {
		this.preReqs.add(disciplina);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((versaoCurso == null) ? 0 : versaoCurso.hashCode());
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
		Disciplina other = (Disciplina) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (versaoCurso == null) {
			if (other.versaoCurso != null)
				return false;
		} else if (!versaoCurso.equals(other.versaoCurso))
			return false;
		return true;
	}

	public int getCargaHoraria() {
		return cargaHoraria;
	}
	
	public void definirComoOptativa() {
		this.ehOptativa = true;
	}
	
	public void definirComoObrigatoria() {
		this.ehOptativa = false;
	}
}
