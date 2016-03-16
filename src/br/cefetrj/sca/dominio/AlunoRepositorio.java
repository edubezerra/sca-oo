package br.cefetrj.sca.dominio;

import java.util.List;

public interface AlunoRepositorio {

	public abstract boolean incluir(Aluno p);

	public abstract boolean alterar(Aluno p);

	public abstract boolean excluir(Aluno p);

	public abstract Aluno getAlunoPorMatricula(String matricula);

	public abstract List<Aluno> obterTodos();

	public abstract Aluno getAlunoPorCPF(String cpf);

	public abstract Aluno getAlunoPorId(String idAluno);
	
	public abstract List<Aluno> getAlunosByCursoEPeriodo(String siglaCurso, String periodo);
}