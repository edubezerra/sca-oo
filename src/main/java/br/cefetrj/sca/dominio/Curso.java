package br.cefetrj.sca.dominio;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Curso {

	@Id
	@GeneratedValue
	Long id;

	String sigla;

	String nome;
	
	@OneToMany
	List<Disciplina> disciplinas;
	
	@OneToOne
	Professor coordenador;

	@SuppressWarnings("unused")
	private Curso() {
	}

	public Curso(String siglaCurso, String nomeCurso) {
		this.sigla = siglaCurso;
		this.nome = nomeCurso;
	}

	public String getSigla() {
		return sigla;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sigla == null) ? 0 : sigla.hashCode());
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
		Curso other = (Curso) obj;
		if (sigla == null) {
			if (other.sigla != null)
				return false;
		} else if (!sigla.equals(other.sigla))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Curso [sigla=" + sigla + ", nome=" + nome + "]";
	}

	public Professor getCoordenador() {
		return coordenador;
	}

	public void setCoordenador(Professor coordenador) {
		this.coordenador = coordenador;
	}
}
