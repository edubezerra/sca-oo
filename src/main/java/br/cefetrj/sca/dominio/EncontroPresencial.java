package br.cefetrj.sca.dominio;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class EncontroPresencial {

	@Id
	@GeneratedValue
	private Long id;

	private Date data;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "FREQUENCIA_ALUNOS", joinColumns = { @JoinColumn(name = "ENCONTRO_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "ALUNO_ID", referencedColumnName = "ID") })
	private Set<Aluno> alunos = new HashSet<>();
	
	public EncontroPresencial(Date data)
	{  
		if(data == null){
			throw new IllegalArgumentException("Data do encontro não fornecida!");
		}
		
		this.data = data;
	}

	public Long getId() {
		return id;
	}

	public Date getData() {
		return data;
	}

	public Set<Aluno> getAlunos() {
		return alunos;
	}

}
