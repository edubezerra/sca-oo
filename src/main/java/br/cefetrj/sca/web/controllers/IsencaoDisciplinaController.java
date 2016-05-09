package br.cefetrj.sca.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.ItemIsencao;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.service.IsencaoDisciplinaService;

@Controller
@SessionAttributes("login")
@RequestMapping("/isencaoDisciplina")
public class IsencaoDisciplinaController {

	@Autowired
	IsencaoDisciplinaService is;

	@RequestMapping(value = "/alunoView", method = RequestMethod.GET)
	public String isencaoDisciplina(Model model, HttpServletRequest request, HttpSession session) {

		String matricula = UsuarioController.getCurrentUser().getLogin();
		session.setAttribute("login", matricula);
		try {

			Aluno aluno = is.findAlunoByMatricula(matricula);

			String siglaCurso = aluno.getVersaoCurso().getCurso().getSigla();

			List<Disciplina> disciplinas = is.findDisciplinas(siglaCurso);
			model.addAttribute("aluno", aluno);
			model.addAttribute("disciplinas", disciplinas);

			return "/isencaoDisciplina/aluno/alunoView";

		} catch (Exception exc) {

			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/validaComprovante", method = RequestMethod.POST)
	public String validaComprovante(Model model, HttpServletRequest request, HttpSession session,
			@RequestParam("file") MultipartFile file) {
		try {
			String matricula = (String) session.getAttribute("login");
			Aluno aluno = is.findAlunoByMatricula(matricula);
			String auxcheckboxes[] = request.getParameterValues("choice");
			Long checkBoxes[] = new Long[auxcheckboxes.length];
			// convertendo o array de checkBoxes para Long
			for (int i = 0; i < auxcheckboxes.length; i++) {
				checkBoxes[i] = Long.parseLong(auxcheckboxes[i]);
			}
			if (aluno.getProcessoIsencao() != null) {
				for (int i = 0; i < checkBoxes.length; i++) {
					ItemIsencao itemIsencao = new ItemIsencao();
					itemIsencao.setDisciplina(is.getDisciplinaPorId(checkBoxes[i]));
					//salvar itemIsencao?
					aluno.getProcessoIsencao().getListaItenIsencao().add(itemIsencao);
					//salvar processoIsencao?
					

				}
			}

			return "/isencaoDisciplina/aluno/alunoSucesso";
		} catch (Exception exc) {

			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	private void validarArquivoComprovanteIsencao(MultipartFile file) {
		if (file == null) {
			throw new IllegalArgumentException("O comprovante da matrícula no período corrente deve ser fornecido.");
		}
		if (file.getSize() > Comprovante.TAMANHO_MAXIMO_COMPROVANTE) {
			throw new IllegalArgumentException("O arquivo de comprovante deve ter 10mb no máximo");
		}
		String[] tiposAceitos = { "application/pdf", "image/jpeg", "image/png" };
		if (ArrayUtils.indexOf(tiposAceitos, file.getContentType()) < 0) {
			throw new IllegalArgumentException("O arquivo de comprovante deve ser no formato PDF, JPEG ou PNG");
		}
	}
}
