package br.cefetrj.sca.dominio;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;

@Entity
public class ItemIsencao {

	@Id
	@GeneratedValue
	private Long id;

	public static String EM_ANALISE;
	public static String DEFERIDO;
	public static String INDEFERIDO;
	
	private String situacao;
	private Date dataAnalise;

	@ManyToOne
	Disciplina disciplina;
	
	@OneToOne(cascade = {CascadeType.ALL})
	Comprovante comprovante;

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Date getDataAnalise() {
		return dataAnalise;
	}

	public void setDataAnalise(Date dataAnalise) {
		this.dataAnalise = dataAnalise;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Comprovante getComprovante() {
		return comprovante;
	}

	public void setComprovante(String contentType, byte[] data, String nome) {
		this.comprovante = new Comprovante(contentType, data, nome);
	}

}
