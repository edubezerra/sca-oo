package br.cefetrj.sca.infra.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TurmaWS {
	
	private Long id;
	private String codigo;
	private DisciplinaWS disciplina;
	 
	@JsonCreator
	public TurmaWS(Long id, String codigo, DisciplinaWS disciplina) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.disciplina = disciplina;
	}

	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	@JsonProperty("codigo")
	public String getCodigo() {
		return codigo;
	}
	
	@JsonProperty("disciplina")
	public DisciplinaWS getDisciplina() {
		return disciplina;
	}
	
	

}
