package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.ProcessoIsencao;

public interface ProcessoIsencaoRepositorio extends JpaRepository<ProcessoIsencao, Serializable> {
	
	@Query("SELECT pi from ProcessoIsencao pi")
	List<ProcessoIsencao> findProcessoIsencao();
	
	ProcessoIsencao findProcessoIsencaoById(Long id);

}
