package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.VersaoCurso;

public interface DisciplinaRepositorio extends JpaRepository<Disciplina, Serializable> {
	// void adicionarTodas(List<Disciplina> lista);

	Disciplina findDisciplinaByNome(String nomeDisciplina);

	Disciplina findDisciplinaByCodigo(String codigoDisciplina);

	@Query("from Disciplina d where d.codigo = ?1 " + "and d.versaoCurso.curso.sigla = ?2 "
			+ "and d.versaoCurso.numero = ?3")
	Disciplina findByCodigoEmVersaoCurso(String codigoDisciplina, String siglaCurso, String numeroVersaoCurso);

	@Query("from Disciplina d where d.nome = ?1 " + "and d.versaoCurso.curso.sigla = ?2"
			+ " and d.versaoCurso.numero = ?3")
	Disciplina findByNomeEmVersaoCurso(String nomeDisciplina, String siglaCurso, String numeroVersaoCurso);
	/*
	 * @Query("from Disciplina d where d.versaoCurso.curso.sigla = ?1 " )
	 * List<Disciplina> findBySigla(String siglaCurso);
	 */
}
