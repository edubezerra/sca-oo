package br.cefetrj.sca.dominio.matriculaforaprazo;

import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Comprovante {
	@Id
	@GeneratedValue
	private Long id;
	
	private String contentType;
	
	private String nome;
	
	public static long TAMANHO_MAXIMO_COMPROVANTE =  10000000;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] data;
	
	@SuppressWarnings("unused")
	private Comprovante(){
	}
	
	public Comprovante(String contentType, byte[] data, String nome){
		if(contentType == null || data == null || nome == null || nome.isEmpty()){
			throw new IllegalArgumentException("Erro ao importar arquivo");
		}
		
		this.contentType = contentType;
		this.data = data;
		this.nome = nome;
		
	}

	public Long getId() {
		return id;
	}

	public String getContentType() {
		return contentType;
	}
	
	public String getNome() {
		return nome;
	}
	
	public byte[] getData() {
		return data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Comprovante other = (Comprovante) obj;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (!Arrays.equals(data, other.data))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	

	
	

}
