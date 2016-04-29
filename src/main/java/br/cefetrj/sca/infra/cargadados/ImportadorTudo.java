package br.cefetrj.sca.infra.cargadados;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ImportadorTudo {

	public static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
			StandalonePersistenceConfig.class);

	private static EntityManagerFactory emf = (EntityManagerFactory) context
			.getBean("entityManagerFactory");

	public static EntityManager entityManager = emf.createEntityManager();

	public static void main(String[] args) {
		ImportadorTudo importador = context.getBean(ImportadorTudo.class);
		
		entityManager = emf.createEntityManager();

		importador.run();
		entityManager.close();
		emf.close();
	}

	public void run() {
		try {
//			new ImportadorQuestionarioAvaliacaoTurmas().run();
//			new ImportadorCursos().run();
//			new ImportadorDisciplinas().run();
//			new ImportadorPreReqs().run();
//			new ImportadorAlunos().run();
//			new ImportadorDocentes().run();
//			new ImportadorDepartamentos().run();
//
//			new ImportadorHistoricosEscolares().run();
//			
//			new ImportadorTurmasComInscricoes().run();
//			new ImportadorAlocacoesProfessoresEmTurmas().run();
//			new ImportadorAtividadesComp().run();
//			new ImportadorHabilitacoesParaProfessor().run();
//			
//			new ImportadorAlocacoesProfessoresEmDepartamentos().run();
//			
//			new ImportadorAlocacoesDisciplinasEmDepartamentos().run();
			
		} catch (IllegalArgumentException | IllegalStateException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}
}
