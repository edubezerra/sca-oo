package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.ItemIsencao;

public interface ItemIsencaoRepositorio extends JpaRepository<ItemIsencao, Serializable> {
	
	ItemIsencao findItemIsencaoById(Long id);

}
