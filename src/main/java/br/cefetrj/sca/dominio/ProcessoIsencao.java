package br.cefetrj.sca.dominio;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ProcessoIsencao {
	@Id
	@GeneratedValue
	private Long id;
	private String situacaoProcessoIsencao;

	private Date dataRegistro;

	@OneToMany
	List<ItemIsencao> listaItenIsencao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public List<ItemIsencao> getListaItenIsencao() {
		return listaItenIsencao;
	}

	public void setListaItenIsencao(List<ItemIsencao> listaItenIsencao) {
		this.listaItenIsencao = listaItenIsencao;
	}

	public String getSituacaoProcessoIsencao() {
		return situacaoProcessoIsencao;
	}

	public void setSituacaoProcessoIsencao(String situacaoProcessoIsencao) {
		this.situacaoProcessoIsencao = situacaoProcessoIsencao;
	}
	
}
