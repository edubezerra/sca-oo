package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;

public interface AvaliacaoTurmaRepositorio extends
		JpaRepository<AvaliacaoTurma, Serializable> {

	@Query("SELECT a FROM AvaliacaoTurma a WHERE a.turmaAvaliada.id = ?1 AND a.alunoAvaliador.matricula = ?2")
	public AvaliacaoTurma getAvaliacaoTurma(Long idTurma, String matricula);
	
	@Query("SELECT a from AvaliacaoTurma a WHERE a.turmaAvaliada.id = ?1")
	public AvaliacaoTurma findAvaliacoesTurma(Long idTurma);
	
	@Query("SELECT a from AvaliacaoTurma a WHERE a.turmaAvaliada.id = ?1")
	public List<AvaliacaoTurma> findAvaliacoesTurmaLista(Long idTurma);
}
