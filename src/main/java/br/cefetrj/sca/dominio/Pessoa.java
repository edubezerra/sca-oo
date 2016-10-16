package br.cefetrj.sca.dominio;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.cefetrj.sca.dominio.contas.Email;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

@Embeddable
public class Pessoa {

	private String nome;

	@Embedded
	private Email email;

	@Embedded
	private EnderecoResidencial enderecoResidencial;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascimento;

	private String cpf;
	
	private String senha;

	public Pessoa() {
	}

	public Pessoa(String nome) {
		if (nome == null || nome.isEmpty()) {
			throw new IllegalArgumentException("Nome é obrigatório.");
		}
		this.nome = nome;
	}

	public Pessoa(String nome, EnderecoResidencial endereco) {
		this(nome);
		this.enderecoResidencial = endereco;
	}

	public Pessoa(String nome, Date dataNascimento, String enderecoEmail) {
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.email = new Email(enderecoEmail);
	}

	/**
	 * 
	 * @param nome
	 *            nome completo.
	 * @param cpf
	 *            número do CPF.
	 * 
	 * @see <a
	 *      href="http://github.com/caelum/caelum-stella/wiki/Validadores-core">http://github.com/caelum/caelum-stella/wiki/Validadores-core</a>
	 */
	public Pessoa(String nome, String cpf) {
		this(nome);
		CPFValidator validator = new CPFValidator();
		try {
			validator.assertValid(cpf);
			this.cpf = cpf;
		} catch (InvalidStateException e) {
			throw new IllegalArgumentException("CPF é inválido!");
		}
	}

	public Pessoa(String nome, Email email) {
		this(nome);
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EnderecoResidencial getEnderecoResidencial() {
		return enderecoResidencial;
	}

	public void setEnderecoResidencial(EnderecoResidencial endereco) {
		this.enderecoResidencial = endereco;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "Pessoa [nome=" + nome + ", cpf=" + cpf + "]";
	}
}
