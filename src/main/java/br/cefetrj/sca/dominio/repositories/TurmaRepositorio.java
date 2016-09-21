package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;

public interface TurmaRepositorio extends JpaRepository<Turma, Serializable> {

	@Query("SELECT t from Turma t WHERE t.periodo = ?1")
	List<Turma> findTurmasAbertasNoPeriodo(PeriodoLetivo periodo);

	@Query("SELECT t from Turma t JOIN t.inscricoes i JOIN i.aluno a "
			+ "WHERE a.matricula = ?1 AND  t.periodo = ?2")
	List<Turma> findTurmasCursadasPorAlunoNoPeriodo(String matriculaAluno, PeriodoLetivo periodo);

	@Query("SELECT t from Turma t JOIN t.inscricoes i JOIN i.aluno a " + "WHERE a.matricula = ?1")
	List<Turma> findTurmasCursadasPorAluno(String matricula);

	@Query("SELECT t from Turma t WHERE t.professor.matricula = ?1 AND  t.periodo = ?2")
	List<Turma> findTurmasLecionadasPorProfessorEmPeriodo(String matriculaProfessor, PeriodoLetivo periodo);

	@Query("SELECT t from Turma t WHERE t.codigo = ?1 and t.disciplina.codigo = ?2 and t.periodo = ?3")
	Turma findTurmaByCodigoAndDisciplinaAndPeriodo(String codigoTurma, String codigoDisciplina, PeriodoLetivo periodo);

	Turma findTurmaById(Long idTurma);
	
	@Query("SELECT a from AvaliacaoTurma a WHERE a.turmaAvaliada.id = ?1")
	AvaliacaoTurma findAvaliacoesTurma(Long idTurma);

	@Query("SELECT t from Turma t WHERE t.professor.matricula = ?1")
	public List<Turma> findTurmasLecionadasPorProfessor(String matriculaProfessor);
}
