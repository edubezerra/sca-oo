package br.cefetrj.sca.infra.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class TurmaResource {

	@Autowired
	TurmaRepositorio turmaRepo;

	@RequestMapping(value = "/getTurmasProfessor/{matriculaProfessor}", method = RequestMethod.GET, produces = {
	"application/json; charset=UTF-8" }) 
	public String getTurmasProfessor(
			@PathVariable String matriculaProfessor) {

		List<Turma> turmas = turmaRepo.findTurmasLecionadasPorProfessorEmPeriodo(
				matriculaProfessor, PeriodoLetivo.PERIODO_CORRENTE);
		
		List<TurmaWS> turmasJSON = new ArrayList<TurmaWS>();
		for (Turma turma : turmas) {
			 Disciplina d = turma.getDisciplina();
			 DisciplinaWS dws = new DisciplinaWS(d.getId(), d.getCodigo(), d.getNome());
			 TurmaWS tws = new TurmaWS(turma.getId() , turma.getCodigo(), dws);
			 turmasJSON.add(tws);
		}
		
		String json;
		try {
			json = new ObjectMapper().writeValueAsString(turmasJSON);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}

	}
}
