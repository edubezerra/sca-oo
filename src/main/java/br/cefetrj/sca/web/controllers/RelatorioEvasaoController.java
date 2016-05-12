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
import br.cefetrj.sca.service.RelatorioEvasaoService;

@Controller
@RequestMapping("/relatorioEvasao")
public class RelatorioEvasaoController{

	protected Logger logger = Logger
			.getLogger(RelatorioEvasaoController.class.getName());

	@Autowired
	private RelatorioEvasaoService service;
	
	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}

	@RequestMapping(value = "/homeEvasao", method = RequestMethod.GET)
	public String homeEvasao(HttpServletRequest request, Model model) {
         try{
        	 
        	 return "/relatorioEvasao/homeEvasaoView";
        	 
         }catch(Exception exc){
        	 
        	 model.addAttribute("error", exc.getMessage());
 			 return "/homeView";
        	 
         }
	
	}
	
	@RequestMapping(value = "/relatorioEvasao", method = RequestMethod.POST)
	public String relatorioEvasao(HttpServletRequest request, Model model ,@RequestParam String curso,
			@RequestParam String periodoLetivo) {
         try{
        	 
        	 JSONArray data = service.createDataResponse(curso, periodoLetivo);
        	 model.addAttribute("data", data.toString());
        	 model.addAttribute("curso", curso);
        	 return "/relatorioEvasao/homeEvasaoView";
        	 
         }catch(Exception exc){
        	 
        	 model.addAttribute("error", exc.getMessage());
 			 return "/homeView";
        	 
         }
		
	}
	
}
