package br.cefetrj.sca.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.dominio.usuarios.PerfilUsuario;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.UserProfileService;
import br.cefetrj.sca.service.UsuarioService;
import br.cefetrj.sca.web.config.SecurityUser;

@Controller
@RequestMapping("/usuarios")
@SessionAttributes("roles")
public class UsuarioController {

	@Autowired
	private static UsuarioService userService;

	@Autowired
	UserProfileService userProfileService;

	@Autowired
	MessageSource messageSource;

	@Autowired
	ProfessorRepositorio professorRepo;

	@Autowired
	DepartamentoRepositorio departamentoRepo;

	@Autowired
	public void setUserService(UsuarioService userService) {
		UsuarioController.userService = userService;
	}

	public static Usuario getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			String login = ((UserDetails) principal).getUsername();
			Usuario loginUser = userService.findUserByLogin(login);
			return new SecurityUser(loginUser);
		}

		return null;
	}

	/**
	 * Esse método lista todos os usuários.
	 */
	@RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {

		List<Usuario> users = userService.findAll();
		model.addAttribute("users", users);

		return "/usuarios/userslist";
	}

	@RequestMapping(value = { "/", "/listProfessorDepartamento" }, method = RequestMethod.GET)
	public String listProfessorByDepartamento(ModelMap model) {

		List<Professor> professores = professorRepo.findProfessores();
		List<Departamento> departamentos = departamentoRepo.findDepartamentos();
		Map<String, String> mapProfDep = new HashMap<String, String>();

		for (int i = 0; i < professores.size(); i++) {
			Departamento dep = departamentoRepo.findDepartamentoByProfessor(professores.get(i).getMatricula());
			
			System.out.println("professor matricula -> " + professores.get(i).getMatricula() + " departamanto -> "
					+ departamentoRepo.findDepartamentoByProfessor(professores.get(i).getMatricula()));
			
			String nomeDep = null;
			String matricula = null;
			
			if (departamentoRepo.findDepartamentoByProfessor(professores.get(i).getMatricula()) != null) {
				nomeDep = dep.getSigla();
				matricula = professores.get(i).getMatricula();
				
				System.out.println("Adicionados no map: matricula "+matricula+" departamento: " + nomeDep);
			} else{
				matricula = professores.get(i).getMatricula();				
			}		
			
			mapProfDep.put(matricula, nomeDep); // chave, value
		}
		
		for(int i=0;i<mapProfDep.size();i++){
			System.out.println("--------------------------Pecorrendo o map");
			System.out.println("matricula : "+mapProfDep);
		}

		model.addAttribute("mapProfDep", mapProfDep);
		model.addAttribute("departamentos", departamentos);
		model.addAttribute("professores", professores);

		return "/usuarios/cadastroProfessor/cadastroProfessorDepartamento";

	}

	/**
	 * Esse método adiciona o Professor a um Departamento.
	 */
	@RequestMapping(value = { "/", "/setListProfessorDepartamento" }, method = RequestMethod.POST)
	public String setListProfessorByDepartamento(Model model, HttpServletRequest request, HttpSession session,
			@RequestParam("matricula") List<String> matriculas,
			@RequestParam("departamento") List<String> departamentos) {

		for (int j = 0; j < departamentos.size(); j++) {

			int tracoDep = departamentos.get(j).indexOf("-");

			tracoDep = tracoDep + 1;

			Departamento d = departamentoRepo.findDepartamentoBySigla(departamentos.get(j).substring(0, tracoDep - 1));

			for (int i = 0; i < matriculas.size(); i++) {
				int tracoMat = matriculas.get(i).indexOf("-");

				tracoMat = tracoMat + 1;

				if (matriculas.get(i).substring(tracoMat).equals(departamentos.get(j).substring(tracoDep))) {
					System.out.println("Substring Departamento : " + departamentos.get(j).substring(0, tracoDep - 1));
					System.out.println("Nome do Departamento: " + d.getNome());
					Professor p = professorRepo.findProfessorByMatricula(matriculas.get(i).substring(0, tracoMat - 1));
					System.out.println("Substring Professor: " + matriculas.get(i).substring(0, tracoMat - 1));
					System.out.println("Nome Professor: " + p.getNome());

					d.addProfessor(p);

				}

				tracoMat = 0;
			}
			tracoDep = 0;
			departamentoRepo.save(d);
		}
		System.out.println("salvar");
		return "/usuarios/cadastroProfessor/sucesso";
	}

	/**
	 * Esse método fornece um meio de adicionar um novo usuário.
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.GET)
	public String newUser(ModelMap model) {
		Usuario user = new Usuario();
		model.addAttribute("user", user);
		model.addAttribute("edit", false);
		return "/usuarios/registration";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.POST)
	public String saveUser(@Valid Usuario user, BindingResult result, ModelMap model) {

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "/usuarios/registration";
		}

		if (!userService.isLoginJaExistente(user.getId(), user.getLogin())) {
			FieldError loginError = new FieldError("user", "login", messageSource.getMessage("non.unique.login",
					new String[] { user.getLogin() }, Locale.getDefault()));
			result.addError(loginError);
			return "/usuarios/registration";
		}

		userService.saveUser(user);

		model.addAttribute("success", "Usuário " + user.getNome() + " registrado com sucesso");

		return "/usuarios/registrationsuccess";
	}

	/**
	 * Esse método fornece um meio de atualizar um usuário.
	 */
	@RequestMapping(value = { "/edit-user-{login}" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String login, ModelMap model) {
		Usuario user = userService.findUserByLogin(login);
		model.addAttribute("user", user);
		model.addAttribute("edit", true);
		return "/usuarios/registration";
	}

	/**
	 * Este método, chamado na submissão do form, manipula a requisição POST
	 * para atualizar um usuário. Ele também valida os dados fornecidos.
	 */
	@RequestMapping(value = { "/edit-user-{login}" }, method = RequestMethod.POST)
	public String updateUser(@Valid Usuario user, BindingResult result, ModelMap model, @PathVariable String login) {

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "/usuarios/registration";
		}

		userService.updateUser(user);

		model.addAttribute("success", "Usuário " + user.getNome() + " atualizado com sucesso");
		return "/usuarios/registrationsuccess";
	}

	/**
	 * This method will delete an user by it's SSOID value.
	 */
	@RequestMapping(value = { "/delete-user-{login}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String login) {
		// userService.deleteUserLogin(login);
		return "redirect:/list";
	}

	/**
	 * Este método fornece uma lista de objetos <code>PerfilUsuario</code>
	 */
	@ModelAttribute("roles")
	public List<PerfilUsuario> initializeProfiles() {
		return userProfileService.findAll();
	}

}
