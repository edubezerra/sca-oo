package br.cefetrj.sca.web.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.ItemIsencao;
import br.cefetrj.sca.dominio.ProcessoIsencao;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.repositories.ItemIsencaoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProcessoIsencaoRepositorio;
import br.cefetrj.sca.service.IsencaoDisciplinaService;

@Controller
@SessionAttributes("login")
@RequestMapping("/isencaoDisciplina")
public class IsencaoDisciplinaController {

	@Autowired
	IsencaoDisciplinaService is;

	@Autowired
	ItemIsencaoRepositorio itemIsencaoRepo;

	@Autowired
	ProcessoIsencaoRepositorio processoIsencaoRepo;

	@RequestMapping(value = "/alunoView", method = RequestMethod.GET)
	public String isencaoDisciplina(Model model, HttpServletRequest request, HttpSession session) {

		String matricula = UsuarioController.getCurrentUser().getLogin();
		session.setAttribute("login", matricula);
		try {

			Aluno aluno = is.findAlunoByMatricula(matricula);

			String siglaCurso = aluno.getVersaoCurso().getCurso().getSigla();

			List<Disciplina> disciplinas = is.findDisciplinas(siglaCurso);
			model.addAttribute("aluno", aluno);
			model.addAttribute("disciplinas", disciplinas);

			return "/isencaoDisciplina/aluno/alunoView";

		} catch (Exception exc) {

			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/validaComprovante", method = RequestMethod.POST)
	public String validaComprovante(Model model, HttpServletRequest request, HttpSession session,
			@RequestParam("file") MultipartFile file) {
		try {
			String matricula = (String) session.getAttribute("login");
			Aluno aluno = is.findAlunoByMatricula(matricula);

			// validarArquivoComprovanteIsencao(file);

			String auxcheckboxes[] = request.getParameterValues("choice");
			Long checkBoxes[] = new Long[auxcheckboxes.length];
			// convertendo o array de checkBoxes para Long
			for (int i = 0; i < auxcheckboxes.length; i++) {
				checkBoxes[i] = Long.parseLong(auxcheckboxes[i]);
			}

			// instanciando o processo isencao
			ProcessoIsencao pi = null;
			ItemIsencao itemIsencao = null;

			if (aluno.getProcessoIsencao() != null) {
				System.out.println("ja existe processo isencao o aluno e ta atualizando o processo isencao dele");
				// se ele tiver o processo de isencao, vai salvar o processo que
				// já esta existente
				pi = aluno.getProcessoIsencao();
				for (int i = 0; i < checkBoxes.length; i++) {
					itemIsencao = new ItemIsencao();

					itemIsencao.setDisciplina(is.getDisciplinaPorId(checkBoxes[i]));
					// salvar itemIsencao?
					aluno.getProcessoIsencao().getListaItenIsencao().add(itemIsencao);
					// salvar processoIsencao?

					itemIsencaoRepo.save(itemIsencao);
					System.out.println(itemIsencao.getId() + "------------" + itemIsencao.getDisciplina());
					
					System.out.println(itemIsencao.getSituacao() + "------------" );

				}
				processoIsencaoRepo.save(pi);
				System.out.println("->>>> " + aluno.getProcessoIsencao().getId());
				System.out.println("->>>> " + aluno.getProcessoIsencao().getDataRegistro());

			} else {
				List<ItemIsencao> listaIsen = new ArrayList<>();

				pi = new ProcessoIsencao();
				
				System.out.println("comprovante---------");

				 //Comprovante comprovante = new Comprovante(file.getContentType(), file.getBytes(),
				 //file.getOriginalFilename());
				// validarArquivoComprovanteIsencao(file);

				for (int i = 0; i < checkBoxes.length; i++) {
					itemIsencao = new ItemIsencao();

					itemIsencao.setDisciplina(is.getDisciplinaPorId(checkBoxes[i]));
					listaIsen.add(itemIsencao);

					//itemIsencao.setComprovante(comprovante);
					//itemIsencao.setComprovante(file.getContentType(), file.getBytes(),
							// file.getOriginalFilename());

					itemIsencaoRepo.save(itemIsencao);

				}			
				pi.setListaItenIsencao(listaIsen);
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date data = new Date();				
				Date dataAtual = sdf.parse(sdf.format(data));
				
				pi.setDataRegistro(dataAtual);
				aluno.setProcessoIsencao(pi);

				processoIsencaoRepo.save(pi);
				System.out.println("salvando processo isencao");
			}

			return "/isencaoDisciplina/aluno/alunoSucesso";
		} catch (Exception exc) {

			model.addAttribute("error", exc.getMessage());
			exc.printStackTrace();
			return "/homeView";
		}
	}

	private void validarArquivoComprovanteIsencao(MultipartFile file) {
		if (file == null) {
			throw new IllegalArgumentException("O comprovante da matrícula no período corrente deve ser fornecido.");
		}
		if (file.getSize() > Comprovante.TAMANHO_MAXIMO_COMPROVANTE) {
			throw new IllegalArgumentException("O arquivo de comprovante deve ter 10mb no máximo");
		}
		String[] tiposAceitos = { "application/pdf", "image/jpeg", "image/png" };
		if (ArrayUtils.indexOf(tiposAceitos, file.getContentType()) < 0) {
			throw new IllegalArgumentException("O arquivo de comprovante deve ser no formato PDF, JPEG ou PNG");
		}
	}

	////////////////////////////////////// PROFESSOR ///////////////////////////////////// ////////////////////////////

	@RequestMapping(value = "/professorView", method = RequestMethod.GET)
	public String isencaoDisciplinaProfessor(Model model, HttpServletRequest request, HttpSession session) {

		String matricula = UsuarioController.getCurrentUser().getLogin();
		session.setAttribute("login", matricula);
		try {

			Professor professor = is.findProfessorByMatricula(matricula);

			// List<ProcessoIsencao> pi = is.findProcessosIsencao();
			List<Aluno> alunos = is.getTodosOsAlunos();
			List<Aluno> alunosProcessoIsencao = new ArrayList<>();

			for (int i = 0; i < alunos.size(); i++) {
				if (alunos.get(i).getProcessoIsencao() != null) {
					alunosProcessoIsencao.add(alunos.get(i));
				}
			}

			model.addAttribute("professor", professor);
			model.addAttribute("alunosProcessoIsencao", alunosProcessoIsencao);

			return "/isencaoDisciplina/professor/professorView";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = { "/", "/itemProcessoIsencaoView" }, method = RequestMethod.POST)
	public String itensProcessoIsencaoProfessor(Model model, HttpServletRequest request, HttpSession session,
			@RequestParam("matricula") List<String> matriculas) {

		String matricula = UsuarioController.getCurrentUser().getLogin();
		session.setAttribute("login", matricula);

		try {
			Professor professor = is.findProfessorByMatricula(matricula);
			Aluno aluno =null;

			List<Aluno> alunoIsencao = is.getTodosOsAlunos();
			List<ItemIsencao> alunosItemIsencao = new ArrayList<>();

			for (int j = 0; j < matriculas.size(); j++) {
				System.out.println("----> " + matriculas.get(j));

				for (int i = 0; i < alunoIsencao.size(); i++) {
					//System.out.println("Segundo for:: "+alunoIsencao.get(i).getMatricula());
					if (alunoIsencao.get(i).getMatricula().equals(matriculas.get(j))) {
						alunosItemIsencao.addAll(alunoIsencao.get(i).getProcessoIsencao().getListaItenIsencao());
						aluno = is.findAlunoByMatricula(alunoIsencao.get(i).getMatricula());
					}
				}
			}

			model.addAttribute("professor", professor);
			model.addAttribute("aluno", aluno);
			model.addAttribute("alunosItemIsencao", alunosItemIsencao);

			return "/isencaoDisciplina/professor/itemProcessoIsencaoView";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
	
	@RequestMapping(value = { "/", "/professorSucesso" }, method = RequestMethod.POST)
	public String confirmacaoProcessoIsencao(Model model, HttpServletRequest request, HttpSession session,
			@RequestParam("radio") List<String> radio,
			@RequestParam("alunosItemIsencao") List<String> item,
			@RequestParam("aluno") String matriculaAluno) {
		
		System.out.println("sop matricula: " + matriculaAluno);
		
		Aluno aluno = is.findAlunoByMatricula(matriculaAluno);
		
	/*	for (int i = 0; i < item.size(); i++) {
			System.out.println("itens: " + item.get(i));
		}
		for (int j = 0; j < radio.size(); j++) {
			System.out.println("radio : " + radio.get(j));
		} */

		for (int i = 0; i < item.size(); i++) {
			String itemTraco = item.get(i).substring(8, 9);
			System.out.println("itens: "+item.get(i) +" itemtraco " + itemTraco);
			for (int j = 0; j < radio.size(); j++) {
				//String radioTraco = radio.get(j).substring(10, 11);
				String radioTraco = null;
				
				if(radio.get(j).length() == 11){
					radioTraco = radio.get(j).substring(10, 11);
				} else {
					radioTraco = radio.get(j).substring(8, 9);
				}
				
				if(radioTraco.equals(itemTraco)){
					System.out.println("ENTROUUU");
						System.out.println("inffffff");
						String valorSalvo = radio.get(j).substring(0, radio.get(j).indexOf("-"));					
						aluno.getProcessoIsencao().getListaItenIsencao().get(i).setSituacao(valorSalvo);
												
						itemIsencaoRepo.save(aluno.getProcessoIsencao().getListaItenIsencao().get(i));
				}
			}
		}
			return "/isencaoDisciplina/professor/professorSucesso";
	}	
}
