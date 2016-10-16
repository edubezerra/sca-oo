package br.cefetrj.sca.dominio;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class TabelaEquivalencias {

	@Id
	@GeneratedValue
	Long id;
	
	@OneToMany
	Set<BlocoEquivalencia> blocosEquivalencia;
	
	@ManyToOne
	@JsonIgnore
	VersaoCurso versaoCursoCorrespondente;
	
	@SuppressWarnings("unused")
	private TabelaEquivalencias() {
	}
	
	public TabelaEquivalencias(VersaoCurso versaoCursoCorrespondente) {
		this.versaoCursoCorrespondente = versaoCursoCorrespondente;
	}
	
	public void setBlocosEquivalencia(Set<BlocoEquivalencia> blocosEquivalencia) {
		this.blocosEquivalencia = blocosEquivalencia;
	}
	
	public void adicionarBlocoEquivalencia(BlocoEquivalencia b) {
		if(this.blocosEquivalencia == null)
			setBlocosEquivalencia(new HashSet<>());
		
		this.blocosEquivalencia.add(b);
	}

	public Long getId() {
		return id;
	}

	public Set<BlocoEquivalencia> getBlocosEquivalencia() {
		return Collections.unmodifiableSet(blocosEquivalencia);
	}

	public VersaoCurso getVersaoCursoCorrespondente() {
		return versaoCursoCorrespondente;
	}
}
