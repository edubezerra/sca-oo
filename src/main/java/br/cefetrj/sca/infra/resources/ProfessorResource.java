package br.cefetrj.sca.infra.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ProfessorResource {

	@Autowired
	ProfessorRepositorio profRepo;

	@RequestMapping(value = "/getProfessor/{matriculaProfessor}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public String getProfessor(@PathVariable String matriculaProfessor) {
		Professor prof = profRepo.findProfessorByMatricula(matriculaProfessor);
		ProfessorWS pws = new ProfessorWS(prof.getId(), prof.getMatricula(), prof.getNome());
        String mapAsJson;
		try {
			mapAsJson = new ObjectMapper().writeValueAsString(pws);
			return mapAsJson;
		} catch (JsonProcessingException e) {
			return null;
		}

	}

}
