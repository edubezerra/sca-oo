package br.cefetrj.sca.dominio.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
public class TurmaRepositorioTest {

	@Autowired
	TurmaRepositorio turmaRepositorio;

	@Autowired
	DisciplinaRepositorio disciplinaRepo;

	@Autowired
	AlunoRepositorio alunoRepo;

	@Test
	public void testGravarTurmaNoPeriodoCorrente() {
		assertNotNull("Repositório não definido.", turmaRepositorio);

		Disciplina disciplina = disciplinaRepo.findByNomeEmVersaoCurso(
				"MATEMÁTICA DISCRETA", "BCC", "2012");
		assertNotNull(disciplina.getId());

		String codigoTurma = "01234";
		Turma turma = new Turma(disciplina, codigoTurma);

		turmaRepositorio.save(turma);

		assertNotNull(turma.getId());

		assertEquals(turma.getPeriodo(), PeriodoLetivo.PERIODO_CORRENTE);
	}

	@Test
	public void testGravarTurmaEmPeriodoFornecido() {
		assertNotNull("Repositório não definido.", turmaRepositorio);

		Disciplina disciplina = disciplinaRepo.findByNomeEmVersaoCurso(
				"MATEMÁTICA DISCRETA", "BCC", "2012");
		assertNotNull(disciplina.getId());

		String codigoTurma = "690011";
		PeriodoLetivo periodo = new PeriodoLetivo(2014, EnumPeriodo.PRIMEIRO);
		Turma turma = new Turma(disciplina, codigoTurma, 40, periodo);

		turmaRepositorio.save(turma);

		assertNotNull(turma.getId());

		assertEquals(turma.getPeriodo().getAno().intValue(), 2014);
		assertEquals(turma.getPeriodo().getPeriodo(), EnumPeriodo.PRIMEIRO);
	}

	@Test
	public void testObterTurmaPorCodigoNoPeriodoLetivo() {

		assertNotNull("Repositório não definido.", turmaRepositorio);

		String codigoTurma = "400002";
		PeriodoLetivo periodo = new PeriodoLetivo(2015, EnumPeriodo.SEGUNDO);

		PeriodoLetivo periodoLetivo = new PeriodoLetivo(2015, 2);

		assertEquals(periodo, periodoLetivo);

		String codigoDisciplina = "GEXT7501";
		Disciplina disciplina = disciplinaRepo.findByCodigoEmVersaoCurso(
				codigoDisciplina, "BCC", "2012");
		Turma turma = turmaRepositorio
				.findTurmaByCodigoAndDisciplinaAndPeriodo(codigoTurma,
						disciplina, periodo);

		assertNotNull(turma);

		assertEquals(turma.getPeriodo(), periodo);

		assertNotNull(turma.getId());
	}

	@Test
	public void testFindTurmasCursadas() {
		String cpf = "148.323.947-03";
		Aluno aluno = alunoRepo.findAlunoByCpf(cpf);

		assertEquals("REBECCA PONTES SALLES", aluno.getNome());

		List<Turma> lista = turmaRepositorio
				.findTurmasCursadasPorAlunoNoPeriodo(aluno.getMatricula(),
						new PeriodoLetivo(2015, 2));

		assertEquals(6, lista.size());

		assertEquals(true, listaContemDisciplina(lista, "COMPILADORES"));
		assertEquals(true, listaContemDisciplina(lista, "ALGORITMOS EM GRAFOS"));
		assertEquals(
				true,
				listaContemDisciplina(lista,
						"CONCEPÇÃO E ELABORAÇÃO DE PROJETO FINAL"));
		assertEquals(true,
				listaContemDisciplina(lista, "INFORMÁTICA E SOCIEDADE"));
		assertEquals(true,
				listaContemDisciplina(lista, "LEGISLAÇÃO EM INFORMÁTICA"));
		assertEquals(
				true,
				listaContemDisciplina(lista, "PROJETO E CONSTRUÇÃO DE SISTEMAS"));
	}

	private boolean listaContemDisciplina(List<Turma> lista,
			String nomeDisciplina) {
		for (Turma turma : lista) {
			if (turma.getDisciplina().getNome().equals(nomeDisciplina)) {
				return true;
			}
		}
		return false;
	}

	@Test
	public void testObterTurmaExtraEmPeriodoLetivo() {

		assertNotNull("Repositório não definido.", turmaRepositorio);

		String codigoTurma = "EXTRA";

		String codigoDisciplina = "GCC1103";

		PeriodoLetivo periodo = new PeriodoLetivo(2015, EnumPeriodo.SEGUNDO);

		PeriodoLetivo periodoLetivo = new PeriodoLetivo(2015, 2);

		assertEquals(periodo, periodoLetivo);

		Disciplina disciplina = disciplinaRepo.findByCodigoEmVersaoCurso(
				codigoDisciplina, "BCC", "2012");

		Turma turma = turmaRepositorio
				.findTurmaByCodigoAndDisciplinaAndPeriodo(codigoTurma,
						disciplina, periodo);

		assertNotNull(turma);

		System.out.println(turma.getPeriodo());

		assertEquals(turma.getPeriodo(), periodo);

		assertNotNull(turma.getId());
	}

	@Test
	public void testFindTurmasLecionadasPorProfessorEmPeriodo() {
		assertNotNull("Repositório não definido.", turmaRepositorio);

		String matriculaProfessor = "1506449";

		String arrayNomesDisciplinas[] = { "ALGORITMOS EM GRAFOS",
				"PADRÕES DE SOFTWARE", "MATEMÁTICA DISCRETA",
				"PROJETO E CONSTRUÇÃO DE SISTEMAS",
				"ARQUITETURA E PADRÕES DE SOFTWARE" };

		List<String> listaNomesDisciplinas = Arrays
				.asList(arrayNomesDisciplinas);

		PeriodoLetivo periodoLetivo = new PeriodoLetivo(2016, 2);

		List<Turma> lista = turmaRepositorio
				.findTurmasLecionadasPorProfessorEmPeriodo(matriculaProfessor,
						periodoLetivo);
		for (Turma turma : lista) {
			assertTrue(listaNomesDisciplinas
					.contains(turma.getNomeDisciplina()));
		}

	}

	@Test
	public void teste() {
		List<Turma> turmas = turmaRepositorio
				.findTurmasCursadasPorAluno("1311038BCC");
		for (Turma turma : turmas) {
			System.out.println(turma.getDisciplina().getNome() + "/"
					+ turma.getPeriodo());
		}
	}
}
