package br.cefetrj.sca.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.service.RequerimentoMatriculaForaPrazoService;

@RestController
public class TurmasDisponiveisRestController {

	@Autowired
	private RequerimentoMatriculaForaPrazoService service;

	@RequestMapping("/matriculaForaPrazo/turmas/{idDepartamento}")
	public List<Turma> getTurmasNoperiodoCorrenteByDepartamento(
			@PathVariable int idDepartamento) {
		List<Turma> turmas = service
				.findTurmasByPeriodoLetivo(PeriodoLetivo.PERIODO_CORRENTE);
		List<Turma> turmasSelecionadas = new ArrayList<>();
		for (Turma turma : turmas){
			if (turma.getNomeDisciplina().startsWith("GCC")) {
				turmasSelecionadas.add(turma);
			}
		}
		return turmasSelecionadas;
	}

}