package br.cefetrj.sca.dominio;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;

public class DisciplinaService {
	@Autowired
	private DisciplinaRepositorio dao;

	public Disciplina getDisciplinaPorCodigo(String codigoDisciplina, String siglaCurso, String versaoCurso) {
		return dao.findByCodigoEmVersaoCurso(codigoDisciplina, siglaCurso, versaoCurso);
	}

	public Disciplina getDisciplinaPorNome(String nomeDisciplina, String siglaCurso, String versaoCurso) {
		return dao.findByNomeEmVersaoCurso(nomeDisciplina, siglaCurso, versaoCurso);
	}

	
}
