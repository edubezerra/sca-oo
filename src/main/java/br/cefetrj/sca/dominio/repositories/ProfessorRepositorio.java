package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Professor;

public interface ProfessorRepositorio extends
		JpaRepository<Professor, Serializable> {

	Professor findProfessorByMatricula(String matricula);

	@Query("select p from Professor p")
	List<Professor> findProfessores();
	

}
