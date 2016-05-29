package br.cefetrj.sca.web.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import br.cefetrj.sca.dominio.matriculaforaprazo.MatriculaForaPrazo;
import br.cefetrj.sca.dominio.repositories.ItemIsencaoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProcessoIsencaoRepositorio;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.IsencaoDisciplinaService;
import br.cefetrj.sca.service.RequerimentoMatriculaForaPrazoService;

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
	
	
	@RequestMapping(value = "/visualizarProcessoIsencao", method = RequestMethod.GET)
	public String visualizarProcessoIsencao(Model model, HttpServletRequest request, HttpSession session) {

		String matricula = UsuarioController.getCurrentUser().getLogin();
		session.setAttribute("login", matricula);
		try {

			Aluno aluno = is.findAlunoByMatricula(matricula);
			List<ItemIsencao> it = new ArrayList<>();
			String verificarSituacaoItemIsencao = null;
			
			if(aluno.getProcessoIsencao() != null){
				for(int i=0;i<aluno.getProcessoIsencao().getListaItenIsencao().size();i++){
					it.add(aluno.getProcessoIsencao().getListaItenIsencao().get(i));
					if(aluno.getProcessoIsencao().getListaItenIsencao().get(i).getSituacao() != null){
						verificarSituacaoItemIsencao = "verificado";
					}
				}
								
				model.addAttribute("verificarSituacaoItemIsencao", verificarSituacaoItemIsencao);
				model.addAttribute("itemIsencaoByProcessoIsencao", it);
			}
			model.addAttribute("aluno", aluno);
		
			return "/isencaoDisciplina/aluno/alunoSucesso";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}	
	
	
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
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = "/validaComprovante", method = RequestMethod.POST)
	public String validaComprovante(Model model, HttpServletRequest request, HttpSession session,
			@RequestParam("file") MultipartFile file) {
		try {
			String matricula = (String) session.getAttribute("login");
			Aluno aluno = is.findAlunoByMatricula(matricula);

			String auxcheckboxes[] = request.getParameterValues("choice");
			Long checkBoxes[] = new Long[auxcheckboxes.length];

			for (int i = 0; i < auxcheckboxes.length; i++) {
				checkBoxes[i] = Long.parseLong(auxcheckboxes[i]);
			}
			ProcessoIsencao pi = null;
			ItemIsencao itemIsencao = null;

			if (aluno.getProcessoIsencao() != null) {				
				aluno.getProcessoIsencao().getListaItenIsencao().removeAll(aluno.getProcessoIsencao().getListaItenIsencao());
				pi = aluno.getProcessoIsencao();
				
				for(int i=0;i<aluno.getProcessoIsencao().getListaItenIsencao().size();i++){							
					aluno.getProcessoIsencao().getListaItenIsencao().removeAll(aluno.getProcessoIsencao().getListaItenIsencao());
				}
				for (int i = 0; i < checkBoxes.length; i++) {
					itemIsencao = new ItemIsencao();

					itemIsencao.setDisciplina(is.getDisciplinaPorId(checkBoxes[i]));

					aluno.getProcessoIsencao().getListaItenIsencao().add(itemIsencao);

					itemIsencaoRepo.save(itemIsencao);
				}
								
				processoIsencaoRepo.save(pi);

			} else {
				List<ItemIsencao> listaIsen = new ArrayList<>();

				pi = new ProcessoIsencao();

				for (int i = 0; i < checkBoxes.length; i++) {
					itemIsencao = new ItemIsencao();

					itemIsencao.setDisciplina(is.getDisciplinaPorId(checkBoxes[i]));
					listaIsen.add(itemIsencao);

					itemIsencao.setComprovante(file.getContentType(), file.getBytes(),
							 file.getOriginalFilename());

					itemIsencaoRepo.save(itemIsencao);

				}			
				pi.setListaItenIsencao(listaIsen);
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date data = new Date();				
				Date dataAtual = sdf.parse(sdf.format(data));
				
				pi.setDataRegistro(dataAtual);
				aluno.setProcessoIsencao(pi);
				
				processoIsencaoRepo.save(pi);
			}
			
			model.addAttribute("itemIsencaoByProcessoIsencao", aluno.getProcessoIsencao().getListaItenIsencao());
			
			return visualizarProcessoIsencao(model, request, session);
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
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
			return "/menuPrincipalView";
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

				for (int i = 0; i < alunoIsencao.size(); i++) {
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
			return "/menuPrincipalView";
		}
	}
	
	@RequestMapping(value = { "/", "/professorSucesso" }, method = RequestMethod.POST)
	public String confirmacaoProcessoIsencao(Model model, HttpServletRequest request, HttpSession session,
			@RequestParam("radio") List<String> radio,
			@RequestParam("alunosItemIsencao") List<String> item,
			@RequestParam("aluno") String matriculaAluno) {
		
		Aluno aluno = is.findAlunoByMatricula(matriculaAluno);
		
		int contadorItemIsencao = 0;
		for (int i = 0; i < item.size(); i++) {
			int itemNumero = item.get(i).indexOf("-") + 1;		
			String itemTraco = item.get(i).substring(itemNumero);
			for (int j = 0; j < radio.size(); j++) {
				
				String radioTraco = null;
				
				int radioNumero = radio.get(j).indexOf("-") + 1;		
				radioTraco = radio.get(j).substring(radioNumero);

				if(radioTraco.equals(itemTraco)){
						String valorSalvo = radio.get(j).substring(0, radio.get(j).indexOf("-"));
						aluno.getProcessoIsencao().getListaItenIsencao().get(i).setSituacao(valorSalvo);
						Date date = new Date();
						aluno.getProcessoIsencao().getListaItenIsencao().get(i).setDataAnalise(date);
					
						itemIsencaoRepo.save(aluno.getProcessoIsencao().getListaItenIsencao().get(i));
				}
			}
		}
		
		for (int i = 0; i < item.size(); i++) {
			if (aluno.getProcessoIsencao().getListaItenIsencao().get(i).getSituacao() != null) {
				contadorItemIsencao = contadorItemIsencao + 1;
				if (item.size() == contadorItemIsencao) {
					aluno.getProcessoIsencao().setSituacaoProcessoIsencao("ANALISADO");
				} else {
					aluno.getProcessoIsencao().setSituacaoProcessoIsencao("EM ANALISE");
				}
			}
		}
			return "/menuPrincipalView";
	}
	
	
	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(@RequestParam("comprovanteProcIsencao") long idProcIsencao,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession sessao) {
		
		try {
			List<ItemIsencao> listIten = processoIsencaoRepo.findItemIsencaoByProcessoIsencao(idProcIsencao);
			long idQualquerItemIsencao = 0;
			for(int i=0;i<listIten.size();i++){
				idQualquerItemIsencao = listIten.get(0).getId();
			}
			ItemIsencao requerimento = is.findItemIsencaoById(idQualquerItemIsencao);
			Comprovante comprovante = requerimento.getComprovante();
			GerenteArquivos.downloadFileNovo(idQualquerItemIsencao,
					request, response, comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
