package br.cefetrj.sca.web.controllers;

import java.util.List;
import java.util.logging.Logger;

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
import br.cefetrj.sca.dominio.ProcessoIsencao;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
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
			System.out.println("Matricula - " + matricula);
			Aluno aluno = is.findAlunoByMatricula(matricula);

			String siglaCurso = aluno.getVersaoCurso().getCurso().getSigla();

			List<Disciplina> disciplinas = is.findDisciplinas(siglaCurso);
			model.addAttribute("aluno", aluno);
			model.addAttribute("disciplinas", disciplinas);

			return "/isencaoDisciplina/alunoView";

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
			String checkboxes[] = request.getParameterValues("choice");

			/*for (int i = 0; i < checkboxes.length; i++) {
				System.out.println("CheckBox - " + checkboxes[i]);
			} */
			
			validarArquivoComprovanteIsencao(file);
			
			// SE O ALUNO JÁ POSSUI UM PROCESSO DE ISENÇÃO, É FEITO O UPDATE!
			if (aluno.getProcessoIsencao() != null) {
				request.getParameter("choice");

			} else {
				//SE O ALUNO NÃO POSSUI UM PROCESSO DE ISENÇÃO, ENTÃO UM NOVO SERÁ CRIADO.
				ProcessoIsencao pi = new ProcessoIsencao();
				request.getParameter("choice");
			}
			return "/isencaoDisciplina/alunoSucesso";
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
