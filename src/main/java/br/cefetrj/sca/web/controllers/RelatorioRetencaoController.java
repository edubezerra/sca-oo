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

import br.cefetrj.sca.service.RelatorioRetencaoService;

@Controller
@RequestMapping("/relatorioRetencao")
public class RelatorioRetencaoController{
	
	protected Logger logger = Logger
			.getLogger(RelatorioEvasaoController.class.getName());

	@Autowired
	private RelatorioRetencaoService service;
	
	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}
	
	@RequestMapping(value = "/homeRetencao", method = RequestMethod.GET)
	public String homeRetencao(HttpServletRequest request, Model model) {
         try{
        	 
        	 return "/relatorioRetencao/homeRetencaoView";
        	 
         }catch(Exception exc){
        	 
        	 model.addAttribute("error", exc.getMessage());
 			 return "/homeView";
        	 
         }
	
	}
		
	@RequestMapping(value = "/relatorioRetencao", method = RequestMethod.POST)
	public String relatorioRetencao(HttpServletRequest request, Model model ,@RequestParam String curso, @RequestParam String periodoLetivo) {
         try{
        	 
        	 JSONArray data = service.createDataResponse(curso, periodoLetivo);
        	 model.addAttribute("data", data.toString());
        	 model.addAttribute("curso", curso);
        	 return "/relatorioRetencao/homeRetencaoView";
        	 
         }catch(Exception exc){
        	 
        	 exc.printStackTrace();
        	 model.addAttribute("error", exc.getMessage());
 			 return "/login";
        	 
         }
		
	}
	
}
