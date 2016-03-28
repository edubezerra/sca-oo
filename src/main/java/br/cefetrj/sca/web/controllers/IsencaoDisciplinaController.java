package br.cefetrj.sca.web.controllers;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.service.IsencaoDisciplinaService;

@Controller
@SessionAttributes("login")
@RequestMapping("/isencaoDisciplinaController")
public class IsencaoDisciplinaController {

	@Autowired
	IsencaoDisciplinaService is;

	@RequestMapping(value = "/isencaoDisciplina", method = RequestMethod.GET)
	public String paginaInicialIsencao(Model model, HttpServletRequest request, HttpSession session) {

		try {
			String matricula = (String) session.getAttribute("login");
			System.out.println("Matricula - "+matricula);
			Aluno aluno = is.findAlunoByMatricula(matricula);

			String siglaCurso = aluno.getVersaoCurso().getCurso().getSigla();

		//	List<Disciplina> disciplinas = is.findDisciplinas(siglaCurso);
			model.addAttribute("aluno", aluno);
		//	model.addAttribute("disciplinas", disciplinas);

			return "/isencaoDisciplina/alunoView";

		} catch (Exception exc) {

			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
}
