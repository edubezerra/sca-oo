package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.FichaMatriculaForaPrazoFabrica;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.matriculaforaprazo.AlocacacaoDisciplinasEmDepartamento;
import br.cefetrj.sca.dominio.matriculaforaprazo.MatriculaForaPrazo;
import br.cefetrj.sca.dominio.repositories.AlocacacaoDisciplinasEmDepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.MatriculaForaPrazoRepositorio;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;
import br.cefetrj.sca.service.util.FichaMatriculaForaPrazo;
import br.cefetrj.sca.service.util.FichaMatriculaForaPrazo.ItemRequerimentoInfo;

@Service
public class RequerimentoMatriculaForaPrazoService {

	public static final int TAMANHO_MAXIMO_COMPROVANTE = 10000000;

	@Autowired
	private DepartamentoRepositorio departamentoRepositorio;

	@Autowired
	private TurmaRepositorio turmaRepositorio;

	@Autowired
	private MatriculaForaPrazoRepositorio matriculaForaPrazoRepositorio;

	@Autowired
	private FichaMatriculaForaPrazoFabrica fabrica;

	@Autowired
	private AlunoRepositorio alunoRepositorio;

	@Autowired
	AlocacacaoDisciplinasEmDepartamentoRepositorio alocacaoRepositorio;
	
	private String matriculaAluno;

	public Aluno findAlunoByMatricula(String matriculaAluno2) {
		matriculaAluno = matriculaAluno2;
		return alunoRepositorio.findAlunoByMatricula(matriculaAluno);
	}

	public SortedMap<PeriodoLetivo, MatriculaForaPrazo> findMatriculasForaPrazoByAluno(Long idAluno) {
		List<MatriculaForaPrazo> requerimentos = matriculaForaPrazoRepositorio.findMatriculasForaPrazoByAluno(idAluno);
		List<PeriodoLetivo> periodosLetivos = MatriculaForaPrazo.periodosCorrespondentes(requerimentos);
		SortedMap<PeriodoLetivo, MatriculaForaPrazo> mapa = new TreeMap<>();
		for (int i = 0; i < requerimentos.size(); i++) {
			mapa.put(periodosLetivos.get(i), requerimentos.get(i));
		}

		return mapa;
	}

	public void confirmarRegistroRequerimento(FichaMatriculaForaPrazo ficha) {

		MatriculaForaPrazo matriculaForaPrazo = matriculaForaPrazoRepositorio
				.findMatriculaForaPrazoByAlunoAndSemestre(ficha.getAluno(), PeriodoLetivo.PERIODO_CORRENTE);

		if (matriculaForaPrazo == null) {
			matriculaForaPrazo = new MatriculaForaPrazo(ficha.getAluno(), PeriodoLetivo.PERIODO_CORRENTE);
		}

		matriculaForaPrazo.getItens().clear();

		for (ItemRequerimentoInfo item : ficha.getItensRequerimento()) {
			String codigoTurma = item.getCodigoTurma();
			String codigoDisciplina = item.getCodigoDisciplina();
			Turma turma = turmaRepositorio.findTurmaByCodigoAndDisciplinaAndPeriodo(codigoTurma, codigoDisciplina,
					PeriodoLetivo.PERIODO_CORRENTE);
			Departamento depto = departamentoRepositorio.findDepartamentoBySigla(item.getSiglaDepartamento());
			int opcao = item.getOpcao();
			matriculaForaPrazo.addItem(turma, depto, opcao);
		}

		matriculaForaPrazo.setComprovante(ficha.getComprovante());

		matriculaForaPrazoRepositorio.save(matriculaForaPrazo);
	}

	public FichaMatriculaForaPrazo criarFichaSolicitacao(String matricula) {
		return fabrica.criar(matricula);
	}

	public List<Turma> findTurmasByDepartamentoAndPeriodoLetivo(String siglaDepartamento, PeriodoLetivo periodo) {
		Departamento depto = departamentoRepositorio.findDepartamentoBySigla(siglaDepartamento);
		AlocacacaoDisciplinasEmDepartamento a = alocacaoRepositorio
				.findAlocacacaoDisciplinasEmDepartamentoByDepartamento(depto);
		List<Turma> turmasDoPeriodo = findTurmasByPeriodoLetivo(periodo);
		List<Turma> turmas = new ArrayList<>();
		for (Turma turma : turmasDoPeriodo) {
			if (a.getDisciplinas().contains(turma.getDisciplina())) {
				turmas.add(turma);
			}
		}
		return turmas;
	}

	//Fiz uma modificação nessa parte do código porque estava dando erro!!
	public List<Turma> findTurmasByPeriodoLetivo(PeriodoLetivo periodo) {
		List<Turma> turmasDisponiveis = turmaRepositorio.findTurmasAbertasNoPeriodo(periodo);
		List<Turma>  turmaCursadas = findTurmasCursadasPorAlunoNoPeriodo(matriculaAluno, periodo);
		turmasDisponiveis.removeAll(turmaCursadas);
		return turmasDisponiveis;
	}

	public Turma findTurmaById(Long idTurma) {
		return turmaRepositorio.findOne(idTurma);
	}

	public MatriculaForaPrazo findMatriculaForaPrazoById(Long solicitacaoId) {
		return matriculaForaPrazoRepositorio.findMatriculaForaPrazoById(solicitacaoId);
	}

	public MatriculaForaPrazo findMatriculaForaPrazoByAlunoAndPeriodo(String matriculaAluno, PeriodoLetivo periodo) {
		Aluno aluno = alunoRepositorio.findAlunoByMatricula(matriculaAluno);
		return matriculaForaPrazoRepositorio.findMatriculaForaPrazoByAlunoAndSemestre(aluno, periodo);
	}

	public List<Turma> findTurmasCursadasPorAlunoNoPeriodo(String matriculaAluno, PeriodoLetivo periodo) {
		return turmaRepositorio.findTurmasCursadasPorAlunoNoPeriodo(matriculaAluno, periodo);
	}
}
