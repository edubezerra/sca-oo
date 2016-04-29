package br.cefetrj.sca.dominio.repositories;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.Inscricao;
import br.cefetrj.sca.dominio.NotaFinal;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.infra.cargadados.ImportadorTudo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class InscricaoRepositorioTest {

	static EntityManager em = ImportadorTudo.entityManager;
	
	@Autowired
	TurmaRepositorio turmaRepositorio;
	
	@Test
	public void run() {
		String codigoTurma = "910004";
		List<Turma> turmas = turmaRepositorio.findTurmasByCodigo(codigoTurma);
		
		System.out.println("Lista Turmas: " + turmas.size());
		
		NotaFinal notaFinal = new NotaFinal(1L);
		notaFinal.setNotaP1(new BigDecimal(3));
		notaFinal.setNotaP2(new BigDecimal(3));
		notaFinal.setNotaP3(new BigDecimal(3));
		
		notaFinal.setFrequencia(new BigDecimal(1));
		
		
		em.getTransaction().begin();
		em.merge(notaFinal);
		//em.persist(notaFinal);
		/*
		for(Turma t: turmas) {
			for(Inscricao i: t.getInscricoes()) {
				i.registrarAvaliacao(notaFinal);
				em.merge(i);
			}
			
			System.out.println("FIM TURMA");
		}
		
		*/
		em.getTransaction().commit();
	}
	
}
