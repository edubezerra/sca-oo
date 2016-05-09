package br.cefetrj.sca.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;

@Service
public class IsencaoDisciplinaService {

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	private ProfessorRepositorio professorRepo;

	@Autowired
	private DisciplinaRepositorio disciplinaRepo;

	public Aluno findAlunoByMatricula(String matricula) {

		Aluno aluno = alunoRepo.findAlunoByMatricula(matricula);
		if (aluno != null)
			return aluno;
		else
			System.out.println("IsencaoDisciplinaService - Aluno não encontrado!");
		return null;
	}

	public Professor findProfessorByMatricula(String matricula) {
		Professor professor = professorRepo.findProfessorByMatricula(matricula);
		if (professor != null)
			return professor;
		else
			System.out.println("IsencaoDisciplinaService - Professor não encontrado!");
		return null;
	}

	public Disciplina getDisciplinaPorId(Long idDisciplina) {
		return disciplinaRepo.findDisciplinaById(idDisciplina);
	}

	public List<Disciplina> findDisciplinas(String siglaCurso) {
		List<Disciplina> disciplinas = disciplinaRepo.findBySigla(siglaCurso);
		if (disciplinas.isEmpty()) {
			System.out.println("IsencaoDisciplinaService  Lista de disciplinas está vazia!");
			return null;
		} else
			return disciplinas;
	}
}