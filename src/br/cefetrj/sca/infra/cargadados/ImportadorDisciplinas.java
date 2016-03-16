package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.VersaoCurso;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ImportadorDisciplinas {

	static String colunas[] = { "ID_DISCIPLINA", "COD_DISCIPLINA",
			"NOME_DISCIPLINA", "CH_TEORICA", "CH_PRATICA", "CH_TOTAL",
			"CREDITOS", "ENCARGO_DIDATICO", "IND_HORARIO", "SITUACAO",
			"COD_ESTRUTURADO", "NOME_UNIDADE", "SIGLA_UNIDADE", "COD_CURSO",
			"NUM_VERSAO", "ID_VERSAO_CURSO", "IND_SIM_NAO" };

	static String colunasPeriodoIdeal [] = {"COD_CURSO", "COD_DISCIPLINA", "COD_TURMA", "PERIODO_IDEAL", "NOME_DISCIPLINA", 
			"VAGAS_OFERECIDAS", "DIA_SEMANA", "HR_INICIO", "HR_FIM", "TIPO_AULA", "NOME_UNIDADE", "ANO", "PERIODO"};
	
	private HashMap<String, Curso> cursos = new HashMap<>();
	private HashMap<String, VersaoCurso> versoesCursos = new HashMap<>();

	private List<Disciplina> disciplinas = new ArrayList<Disciplina>();

	public static void main(String[] args) {
		run();
	}

	public static void run() {
		System.out.println("ImportadorGradesCurriculares.run()");
		try {
			ImportadorDisciplinas iim = new ImportadorDisciplinas();
			String arquivoPlanilha = "./planilhas/grades-curriculares/DisciplinasBCC.xls";
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();

			iim = new ImportadorDisciplinas();
			arquivoPlanilha = "./planilhas/grades-curriculares/DisciplinasCSTSI.xls";
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();
			
			
			arquivoPlanilha = "./planilhas/turmas-ofertadas/BCC.2015.1.S.xls";
			
			iim.mergePeriodoIdeal(arquivoPlanilha);
			
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	/**
	 * Método feito para importar o período ideal dos cursos (esta informação está em outra planilha).
	 * @param arquivoPlanilha
	 */
	private void mergePeriodoIdeal(String arquivoPlanilha) {
		System.out.println("ImportadorDisciplinas.mergePeriodoIdeal()");
		
		try {
			
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCAPU");

			EntityManager em = emf.createEntityManager();

			em.getTransaction().begin();
			
			File inputWorkbook = new File(arquivoPlanilha);
			
			Workbook w;

			List<String> colunasList = Arrays.asList(colunasPeriodoIdeal);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");
			w = Workbook.getWorkbook(inputWorkbook, ws);
			Sheet sheet = w.getSheet(0);

			for (int i = 1; i < sheet.getRows(); i++) {
				String codDisciplina = sheet.getCell(colunasList.indexOf("COD_DISCIPLINA"), i).getContents();
				Integer periodoIdeal = Integer.parseInt(sheet.getCell(colunasList.indexOf("PERIODO_IDEAL"), i).getContents());

				Query query;
				Disciplina d = null;
				
				try {
					query = em.createQuery("from Disciplina d where d.codigo = :codigo");
					query.setParameter("codigo", codDisciplina);
					d = (Disciplina) query.getSingleResult();
				} catch (NoResultException e) {
					System.out.println("Disciplina nao encontrada: " + codDisciplina);
					d = null;
				}
				
				if(d != null) {
					d.setPeriodoIdeal(periodoIdeal);
					
					em.merge(d);
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

	private void gravarDadosImportados() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		/**
		 * Realiza a persistência dos objetos Curso.
		 */
		Set<String> cursosIt = cursos.keySet();
		for (String codCurso : cursosIt) {

			System.out.println("Gravando curso " + codCurso);

			Curso curso = cursos.get(codCurso);

			em.persist(curso);
		}

		/**
		 * Realiza a persistência dos objetos Disciplina.
		 */
		for (Disciplina disciplina : disciplinas) {
			System.out.println("Gravando disciplina: " + disciplina);
			em.persist(disciplina);
		}

		em.getTransaction().commit();

		em.close();

		System.out.println("Foram importadas " + disciplinas.size()
				+ " disciplinas.");
	}

	public void importarPlanilha(String inputFile) throws BiffException,
			IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	private void importarPlanilha(File arquivoPlanilha) throws BiffException,
			IOException {
		Workbook w;

		System.err.println("Abrindo planilha " + arquivoPlanilha);

		List<String> colunasList = Arrays.asList(colunas);
		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(arquivoPlanilha, ws);
		Sheet sheet = w.getSheet(0);

		importarVersoesCursos(colunasList, sheet);
		importarDisciplinas(colunasList, sheet);
	}

	private VersaoCurso getVersaoCurso(String siglaCurso, String numeroVersao) {
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

	private void importarVersoesCursos(List<String> colunasList, Sheet sheet) {
		System.out
				.println("Iniciando importação de dados relativos a versões de cursos...");
		for (int i = 1; i < sheet.getRows(); i++) {
			String codCurso = sheet
					.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"),
					i).getContents();
			if (versoesCursos.get(codCurso + numVersao) == null) {
				VersaoCurso versao = getVersaoCurso(codCurso, numVersao);
				versoesCursos.put(codCurso + numVersao, versao);
			}

		}
		System.out.println("Foram encontradas " + versoesCursos.size()
				+ " versões de cursos.");
	}

	private void importarDisciplinas(List<String> colunasList, Sheet sheet) {
		System.out
				.println("Iniciando importação de dados relativos a disciplinas...");
		for (int i = 1; i < sheet.getRows(); i++) {
			String codigoDisciplina = sheet.getCell(
					colunasList.indexOf("COD_DISCIPLINA"), i).getContents();
			String nomeDisciplina = sheet.getCell(
					colunasList.indexOf("NOME_DISCIPLINA"), i).getContents();
			String creditos = sheet.getCell(colunasList.indexOf("CREDITOS"), i)
					.getContents();
			String numHoras = sheet.getCell(colunasList.indexOf("CH_TOTAL"), i)
					.getContents();

			String codCurso = sheet
					.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"),
					i).getContents();

			boolean disciplinaJaExiste = false;
			for (Disciplina disciplina : disciplinas) {
				if (disciplina.equals(codigoDisciplina)) {
					disciplinaJaExiste = true;
					break;
				}
			}
			if (!disciplinaJaExiste) {
				Disciplina disciplina = new Disciplina(codigoDisciplina,
						nomeDisciplina, creditos, numHoras);
				VersaoCurso versaoCurso = versoesCursos.get(codCurso
						+ numVersao);
				disciplina.alocarEmVersao(versaoCurso);
				disciplinas.add(disciplina);
			} else {
				System.err.println("Já existe disciplina com código "
						+ codigoDisciplina);
			}
		}
	}
}
