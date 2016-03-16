package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.VersaoCurso;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ImportadorCursos {
	private static ApplicationContext context = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext.xml" });

	static String colunas[] = { "ID_DISCIPLINA", "COD_DISCIPLINA", "NOME_DISCIPLINA", "CH_TEORICA", "CH_PRATICA",
			"CH_TOTAL", "CREDITOS", "ENCARGO_DIDATICO", "IND_HORARIO", "SITUACAO", "COD_ESTRUTURADO", "NOME_UNIDADE",
			"SIGLA_UNIDADE", "COD_CURSO", "NUM_VERSAO", "ID_VERSAO_CURSO", "IND_SIM_NAO" };

	static String colunasPeriodoMinimo[] = {"COD_CURSO", "CURSO", "PERIODO_MINIMO", "NUM_VERSAO"};
	
	/**
	 * Dicionário de pares (sigla, objeto da classe VersaoCurso) de cada
	 * curso.
	 */
	private HashMap<String, VersaoCurso> versoesCursos = new HashMap<>();

	/**
	 * Dicionário de pares (sigla, objeto da classe VersaoCurso) de cada
	 * curso.
	 */
	private HashMap<String, Curso> cursos = new HashMap<>();

	public ImportadorCursos() {
	}

	public static void main(String[] args) {
		String planilhaCSTSI = "./planilhas/grades-curriculares/DisciplinasCSTSI.xls";
		String planilhaBCC = "./planilhas/grades-curriculares/DisciplinasBCC.xls";
		
		String planilhaInfoPeriodoMinimo = "./planilhas/curso-periodo-minimo/curso_periodo_minimo.xls";
		
		//ImportadorCursos.run(planilhaCSTSI);
		//ImportadorCursos.run(planilhaBCC);
		
		ImportadorCursos.mergeInfoPeriodo(planilhaInfoPeriodoMinimo);
	}

	public static void run(String arquivoPlanilha) {
		System.out.println("ImportadorCursos.run()");
		try {
			ImportadorCursos iim = new ImportadorCursos();
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}
	
	/**
	 * Método feito para importar a informação do período máximo dos cursos (esta informação está em outra planilha).
	 * @param arquivoPlanilha
	 */
	public static void mergeInfoPeriodo(String arquivoPlanilha) {
		System.out.println("ImportadorCursos.mergeInfoPeriodo()");
		
		try {
			
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCAPU");

			EntityManager em = emf.createEntityManager();

			em.getTransaction().begin();
			
			File inputWorkbook = new File(arquivoPlanilha);
			
			Workbook w;

			List<String> colunasList = Arrays.asList(colunasPeriodoMinimo);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");
			w = Workbook.getWorkbook(inputWorkbook, ws);
			Sheet sheet = w.getSheet(0);

			for (int i = 1; i < sheet.getRows(); i++) {
				String siglaCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
				Integer qtdPeriodoMinimo = Integer.parseInt(sheet.getCell(colunasList.indexOf("PERIODO_MINIMO"), i).getContents());
				String numeroVersao =sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();
				
				VersaoCurso versaoCurso = getVersaoCurso(siglaCurso, numeroVersao);
				
				if(versaoCurso != null) {
					versaoCurso.setQtdPeriodoMinimo(qtdPeriodoMinimo);
					
					em.merge(versaoCurso);
				}
			}
			
			em.getTransaction().commit();

			em.close();
			
		} catch(BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Feito!");
	}

	public void gravarDadosImportados() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		/**
		 * Realiza a persistência dos objetos Curso.
		 */
		Set<String> cursosIt = cursos.keySet();
		for (String siglaCurso : cursosIt) {
			System.out.println("Gravando " + cursos.get(siglaCurso));
			em.persist(cursos.get(siglaCurso));
		}

		/**
		 * Realiza a persistência dos objetos <code>VersaoCurso</code>.
		 */
		Set<String> versoesIt = versoesCursos.keySet();
		for (String numeroMaisSigla : versoesIt) {
			System.out.println("Gravando " + versoesCursos.get(numeroMaisSigla));
			em.persist(versoesCursos.get(numeroMaisSigla));
		}

		em.getTransaction().commit();

		em.close();

		System.out.println("Foram importados " + cursos.keySet().size() + " cursos.");
		System.out.println("Foram importados " + versoesCursos.keySet().size() + " versões de cursos.");
	}

	public void importarPlanilha(String inputFile) throws BiffException, IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	public void importarPlanilha(File inputWorkbook) throws BiffException, IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println("Iniciando importação de cursos...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {
			String siglaCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();
			String nomeCurso = sheet.getCell(colunasList.indexOf("NOME_UNIDADE"), i).getContents();

			if (cursos.get(siglaCurso) == null) {
				Curso curso = new Curso(siglaCurso, nomeCurso);
				cursos.put(siglaCurso, curso);
			}

			if (versoesCursos.get(siglaCurso + numVersao) == null) {
				Curso curso = cursos.get(siglaCurso);
				VersaoCurso versao = new VersaoCurso(numVersao, curso);
				versoesCursos.put(siglaCurso + numVersao, versao);
			}
		}
	}
	
	private static VersaoCurso getVersaoCurso(String siglaCurso, String numeroVersao) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("from VersaoCurso versao "
						+ "where versao.numero = :numeroVersao and versao.curso.sigla = :siglaCurso");
		query.setParameter("numeroVersao", numeroVersao);
		query.setParameter("siglaCurso", siglaCurso);
		return (VersaoCurso) query.getSingleResult();
	}
}
