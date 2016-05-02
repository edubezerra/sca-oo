package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Departamento;

public interface DepartamentoRepositorio extends JpaRepository<Departamento, Serializable> {

	Departamento findDepartamentoById(Long id);

	Departamento findDepartamentoBySigla(String siglaDepartamento);

	@Query("SELECT d from Departamento d JOIN d.professores p WHERE p.matricula = ?1")
	Departamento findDepartamentoByProfessor(String matriculaProfessor);

	@Query("SELECT d from Departamento d")
	List<Departamento> findDepartamentos();
	
}
