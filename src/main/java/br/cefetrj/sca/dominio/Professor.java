package br.cefetrj.sca.dominio;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.dominio.contas.Email;
import br.cefetrj.sca.dominio.gradesdisponibilidade.GradeDisponibilidade;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;

@Entity
public class Professor {
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Matrícula do professor, composta apenas de dígitos e de tamanho 7.
	 */
	private String matricula;

	@OneToMany
	private Set<GradeDisponibilidade> grades;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "PROFESSOR_DISCIPLINA", joinColumns = {
			@JoinColumn(name = "PROFESSOR_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "DISCIPLINA_ID", referencedColumnName = "ID") })
	private Set<Disciplina> habilitacoes = new HashSet<>();

	@Embedded
	Pessoa pessoa;

	public Long getId() {
		return id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@SuppressWarnings("unused")
	private Professor() {

	}

	public Professor(String matricula, String nome, String email) {
		this(matricula);
		this.pessoa = new Pessoa(nome, new Email(email));
	}

	public Professor(String matricula, String nome) {
		this(matricula);
		this.pessoa = new Pessoa(nome);
	}

	public Professor(String matricula) {
		if (matricula == null || matricula.isEmpty()) {
			throw new IllegalArgumentException("Matrícula é obrigatório.");
		}
		// if (matricula.length() != TAMANHO_MATRICULA) {
		// throw new IllegalArgumentException("Matrícula deve ter tamanho "
		// + TAMANHO_MATRICULA + ".");
		// }
		if (!contemApenasDigitos(matricula)) {
			throw new IllegalArgumentException("Matrícula deve conter apenas dígitos: " + matricula + ".");
		}
		this.matricula = matricula;
	}

	private boolean contemApenasDigitos(String str) {

		if (str == null || str.length() == 0)
			return false;

		for (int i = 0; i < str.length(); i++) {

			if (!Character.isDigit(str.charAt(i)))
				return false;
		}

		return true;
	}

	public boolean estaHabilitado(Disciplina disciplina) {
		return this.habilitacoes.contains(disciplina);
	}

	public String getNome() {
		return this.pessoa.getNome();
	}

	public Set<Disciplina> getHabilitacoes() {
		return Collections.unmodifiableSet(this.habilitacoes);
	}

	public String getMatricula() {
		return matricula;
	}

	public Disciplina getHabilitacao(String nomeDisciplina) {
		for (Disciplina d : habilitacoes) {
			if (d.getNome().equals(nomeDisciplina))
				return d;
		}
		return null;
	}

	public void removerHabilitacoes(List<String> nomesDisciplinas) {
		for (String nome : nomesDisciplinas) {

		}
		// TODO Auto-generated method stub

	}

	public void habilitarPara(Disciplina d) {
		this.habilitacoes.add(d);
	}
}