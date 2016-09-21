package br.cefetrj.sca.web.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.VisualizacaoAvaliacaoDiscenteService;

@Controller
@RequestMapping("/visualizacaoAvaliacaoDiscente")
public class VisualizacaoAvaliacaoDiscenteController {
	@Autowired
	private VisualizacaoAvaliacaoDiscenteService discenteService;

	@Autowired
	private ProfessorRepositorio repositorio;

	@RequestMapping("/turma")
	public List<AvaliacaoTurma> ApresentarTurma() {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();

		Professor professor = repositorio.findProfessorByMatricula(matricula);

		List<AvaliacaoTurma> at = discenteService.listarTurmaLecionadas(professor, PeriodoLetivo.PERIODO_CORRENTE);

		return at;

	}

	public void EscolhaTurma(AvaliacaoTurma t) {

	}

}
