package br.cefetrj.sca.dominio;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Departamento {
	@Id
	@GeneratedValue
	private Long id;

	private String sigla;

	private String nome;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<Professor> professores = new HashSet<Professor>();

	@SuppressWarnings("unused")
	private Departamento() {
	}

	public Departamento(String sigla, String nome) {
		this.sigla = sigla;
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String deptName) {
		this.nome = deptName;
	}

	public String getSigla() {
		return sigla;
	}

	public void addProfessor(Professor professor) {
		this.professores.add(professor);
	}

	public Set<Professor> getProfessores() {
		return professores;
	}

	public String toString() {
		return "sigla: " + getSigla() + ", nome: " + getNome();
	}

}