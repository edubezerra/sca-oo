package br.cefetrj.sca.web.controllers;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.cefetrj.sca.service.RelatorioReprovacaoTurmaService;

@Controller
@RequestMapping("/relatorioReprovacaoTurma")
public class RelatorioReprovacaoTurmaController {

	@Autowired
	private RelatorioReprovacaoTurmaService service;
	
	protected Logger logger = Logger
			.getLogger(RelatorioReprovacaoTurmaController.class.getName());
	
	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}
	
	@RequestMapping(value = "/homeReprovacaoTurma", method = RequestMethod.GET)
	public String homeEvasao(HttpServletRequest request, Model model) {
         try{
        	 
        	 return "/relatorioReprovacaoTurma/homeReprovacaoTurmaView";
        	 
         }catch(Exception exc){
        	 
        	 model.addAttribute("error", exc.getMessage());
 			 return "/homeView";
        	 
         }
	}
	
	@RequestMapping(value = "/relatorioReprovacaoTurma", method = RequestMethod.POST)
	public String relatorioReprovacaoTurma(HttpServletRequest request, Model model ,@RequestParam String turma) {
         try{
        	 
        	 JSONArray data = service.createDataResponse(turma);
        	 model.addAttribute("data", data.toString());
        	 model.addAttribute("turma", turma);
        	 return "/relatorioReprovacaoTurma/homeReprovacaoTurmaView";
        	 
         } catch(Exception exc){
        	 model.addAttribute("error", exc.getMessage());
 			 return "/homeView";
        	 
         }
	}
}
