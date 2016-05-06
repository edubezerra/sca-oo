package br.cefetrj.sca.web.controllers;

import java.io.IOException;
import java.util.List;
import java.util.SortedMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.matriculaforaprazo.MatriculaForaPrazo;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.RequerimentoMatriculaForaPrazoService;
import br.cefetrj.sca.service.util.FichaMatriculaForaPrazo;

/**
 * TODO
 * 
 * remoção de item - OK
 * 
 * centralização de colunas bootstrap - OK
 * 
 * atualização de turmas em função de seleção de departamento - OK
 * 
 * até três itens por requerimento - OK
 * 
 * alocações ***PARCIAIS*** disciplinas a departamentos - OK
 * 
 * cadastro de alocações disciplinas a departamentos
 * 
 * análise de itens
 * 
 * aluno deve visualizar suas matrículas regulares no período no momento da
 * realização do requerimento
 * 
 * @author Eduardo
 *
 */
@Controller
@RequestMapping("/matriculaForaPrazo/requerimento")
public class RequerimentoMatriculaForaPrazoController {

	protected Logger logger = Logger.getLogger(RequerimentoMatriculaForaPrazoController.class.getName());

	@Autowired
	private RequerimentoMatriculaForaPrazoService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/visualizarRequerimentosView";
	}

	/**
	 * Invocado quando o usuário (aluno) solicita iniciar o registro de um novo
	 * requerimento de matrícula fora do prazo.
	 */
	@RequestMapping(value = "/iniciarRegistroRequerimento", method = RequestMethod.POST)
	public String iniciarRegistroRequerimento(Model model, HttpSession sessao) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matriculaAluno = usr.getLogin();
		try {
			FichaMatriculaForaPrazo ficha = service.criarFichaSolicitacao(matriculaAluno);

			sessao.setAttribute("ficha", ficha);

			sessao.setAttribute("turmasCursadas",
					service.findTurmasCursadasPorAlunoNoPeriodo(matriculaAluno, PeriodoLetivo.PERIODO_CORRENTE));

			sessao.setAttribute("turmasDisponiveis",
					service.findTurmasByPeriodoLetivo(matriculaAluno, PeriodoLetivo.PERIODO_CORRENTE));

			return "/matriculaForaPrazo/requerimento/registrarRequerimentoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	/**
	 * Método invocado quando o usuário (aluno) seleciona a opção (link) para
	 * realizar um requerimento. Esse método produz a lista de requerimentos
	 * anteriores. A partir dessa lista, o usuário pode selecionar o
	 * requerimento correspondente ao período letivo corrente para edição. Pode
	 * também selecionar qualquer requerimento de períodos letivos anteriores
	 * para visualização apenas.
	 */
	@RequestMapping(value = "/visualizarRequerimentos", method = RequestMethod.GET)
	public String visualizarRequerimentos(Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matriculaAluno = usr.getLogin();
		this.definirModelParaVisualizacaoRequerimentos(model, matriculaAluno);
		return "/matriculaForaPrazo/requerimento/visualizarRequerimentosView";
	}

	/**
	 * Método invocado quando o usuário submete dados para a adição de um novo
	 * item (turma/disciplina) ao requerimento de matrícula fora do prazo.
	 */
	@RequestMapping(value = "/adicionarItem", method = RequestMethod.POST)
	public String adicionarItemEmRequerimento(@RequestParam String siglaDepartamento, @RequestParam Long idTurma,
			@RequestParam int opcao, @RequestParam String observacao, Model model, HttpSession sessao)
					throws IOException {

		FichaMatriculaForaPrazo ficha = (FichaMatriculaForaPrazo) sessao.getAttribute("ficha");

		if (ficha.getItensRequerimento().size() < MatriculaForaPrazo.QTD_MAXIMA_ITENS) {

			try {

				Turma turma = service.findTurmaById(idTurma);
				ficha.adicionarItemRequerimento(turma.getId(), turma.getCodigo(), turma.getDisciplina().getCodigo(),
						turma.getNomeDisciplina(), siglaDepartamento, opcao, observacao);
				model.addAttribute("sucesso", "Item foi corretamente adicionado ao requerimento.");
			} catch (Exception e) {
				model.addAttribute("error", e.getMessage());
			}
		} else {
			model.addAttribute("error", "A quantidade máxima possível de itens neste requerimento é "
					+ MatriculaForaPrazo.QTD_MAXIMA_ITENS);
		}
		return "/matriculaForaPrazo/requerimento/registrarRequerimentoView";
	}

	/**
	 * Método invocado quando o usuário solicita que um item do requerimento
	 * seja removido. Esse item é identificado pelo ID da turma correspondente,
	 * que é passado como parâmetro GET.
	 */
	@RequestMapping("/item/remove/{idTurma}")
	public String removerItem(@PathVariable int idTurma, Model model, HttpSession sessao) {

		FichaMatriculaForaPrazo ficha = (FichaMatriculaForaPrazo) sessao.getAttribute("ficha");

		ficha.removerItemRequerimento(idTurma);

		return "/matriculaForaPrazo/requerimento/registrarRequerimentoView";
		// return completarDespachoParaView(model, ficha);
	}

	/**
	 * Método invocado quando o usuário confirma o registro dos itens de
	 * requerimento de matrícula fora do prazo.
	 */
	@RequestMapping(value = "/confirmarRegistroRequerimento", method = RequestMethod.POST)
	public String definirComprovante(// @RequestParam MultipartFile file,
			HttpServletRequest request, Model model, HttpSession sessao) {

		FichaMatriculaForaPrazo ficha = (FichaMatriculaForaPrazo) sessao.getAttribute("ficha");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getLogin();

		// if (file == null) {
		// model.addAttribute("error",
		// "Ccomprovante de matrícula deve ser fornecido.");
		// } else {
		try {
			// validarArquivoComprovanteMatricula(file);
			// ficha.setComprovante(file.getContentType(), file.getBytes(),
			// file.getOriginalFilename());
			service.confirmarRegistroRequerimento(ficha);
			model.addAttribute("sucesso", "Solicitação registrada com sucesso.");
			this.definirModelParaVisualizacaoRequerimentos(model, matricula);
			// } catch (IOException e) {
			// model.addAttribute("error",
			// "Erro: comprovante de matrícula é inválido.");
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			model.addAttribute("aluno", ficha.getAluno());
			model.addAttribute("periodoLetivo", PeriodoLetivo.PERIODO_CORRENTE);
		}
		// }
		return "/matriculaForaPrazo/requerimento/visualizarRequerimentosView";

	}

	/**
	 * Método invocado quando o usuário solicita visualizar os detalhes de um
	 * requerimento.
	 */
	@RequestMapping(value = "/visualizarDetalhesRequerimento", method = RequestMethod.POST)
	public String visualizarDetalhesRequerimento(
			@RequestParam int ano, 
			@RequestParam EnumPeriodo periodo,
			Model model) {

		Usuario usr = UsuarioController.getCurrentUser();
		String matriculaAluno = usr.getLogin();

		try {
			MatriculaForaPrazo requerimento;
			requerimento = service.findMatriculaForaPrazoByAlunoAndPeriodo(
					matriculaAluno,
					new PeriodoLetivo(ano, periodo));

			model.addAttribute("requerimento", requerimento);

			return "/matriculaForaPrazo/requerimento/listarDetalhesRequerimento";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			this.definirModelParaVisualizacaoRequerimentos(model, matriculaAluno);
			return "/matriculaForaPrazo/requerimento/visualizarRequerimentosView";
		}
	}

	// @RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	// public void downloadFile(@RequestParam Long solicitacaoId,
	// HttpServletRequest request, HttpServletResponse response,
	// HttpSession sessao) {
	// Usuario usr = UsuarioController.getCurrentUser();
	// String matriculaAluno = usr.getLogin();
	// try {
	// MatriculaForaPrazo requerimento =
	// service.findMatriculaForaPrazoById(solicitacaoId);
	// Comprovante comprovante = requerimento.getComprovante();
	// GerenteArquivos.downloadFile(matriculaAluno, solicitacaoId, request,
	// response, comprovante);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// @RequestMapping(method = RequestMethod.GET, value =
	// "/getTurmasByDepartamento")
	// @ResponseBody
	// public String handleRequest(@RequestParam("idDepartamento") int id) {
	// List<Turma> turmas =
	// service.findTurmasByPeriodoLetivo(PeriodoLetivo.PERIODO_CORRENTE);
	// return turmas.toString();
	// }

	// private void validarArquivoComprovanteMatricula(MultipartFile file) {
	// if (file == null) {
	// throw new IllegalArgumentException(
	// "O comprovante da matrícula no período corrente deve ser fornecido.");
	// }
	// if (file.getSize() >
	// MatriculaForaPrazoService.TAMANHO_MAXIMO_COMPROVANTE) {
	// throw new IllegalArgumentException(
	// "O arquivo de comprovante deve ter 10mb no máximo");
	// }
	// String[] tiposAceitos = { "application/pdf", "image/jpeg", "image/png" };
	// if (ArrayUtils.indexOf(tiposAceitos, file.getContentType()) < 0) {
	// throw new IllegalArgumentException(
	// "O arquivo de comprovante deve ser no formato PDF, JPEG ou PNG");
	// }
	// }

	private void definirModelParaVisualizacaoRequerimentos(Model model, String matriculaAluno) {
		Aluno aluno = service.findAlunoByMatricula(matriculaAluno);

		SortedMap<PeriodoLetivo, MatriculaForaPrazo> mapa = service.findMatriculasForaPrazoByAluno(aluno.getId());

		model.addAttribute("listaPeriodosLetivos", mapa.keySet());

		if (mapa.get(PeriodoLetivo.PERIODO_CORRENTE) != null) {
			model.addAttribute("periodoLetivoCorrente", PeriodoLetivo.PERIODO_CORRENTE);
		}

		model.addAttribute("aluno", aluno);
	}
}
