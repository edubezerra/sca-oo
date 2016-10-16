package br.cefetrj.sca.infra.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfessorWS {
	
	private Long id;
	private String matricula;
	private String nome;

	@JsonCreator
	public ProfessorWS(Long id, String matricula,  String nome) {
		super();
		this.id = id;
		this.matricula = matricula;
		this.nome = nome;
	}

	@JsonProperty("id") 
	public Long getId() {
		return id;
	}

	@JsonProperty("matricula")
	public String getMatricula() {
		return matricula;
	}

	@JsonProperty("nome")
	public String getNome() {
		return nome;
	}

}
