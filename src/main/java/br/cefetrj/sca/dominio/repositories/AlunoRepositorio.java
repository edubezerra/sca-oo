package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Aluno;

public interface AlunoRepositorio extends JpaRepository<Aluno, Serializable> {
	
	Aluno findAlunoByMatricula(String matricula);

	@Query("SELECT a from Aluno a WHERE a.pessoa.cpf = ?1")
	Aluno findAlunoByCpf(String cpf);
	
	@Query("SELECT a from Aluno a")
	List<Aluno> getAllAlunos();
	
}