/**
 * 
 */
package br.cefetrj.sca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.dominio.repositories.UsuarioRepositorio;
import br.cefetrj.sca.dominio.usuarios.Usuario;

@Service
@Transactional
public class UsuarioService {
	@Autowired
	private UsuarioRepositorio repositorio;
	
	@Autowired
	ProfessorRepositorio professorRepo;

	@Autowired
	DepartamentoRepositorio departamentoRepo;

	public void saveUser(Usuario usr) {
		repositorio.save(usr);
	}

	/*
	 * Since the method is running with Transaction, No need to call hibernate
	 * update explicitly. Just fetch the entity from db and update it with
	 * proper values within transaction. It will be updated in db once
	 * transaction ends.
	 */
	public void updateUser(Usuario user) {
		Usuario entity = repositorio.findOne(user.getId());
		if (entity != null) {
			entity.setLogin(user.getLogin());
			entity.setPassword(user.getPassword());
			entity.setNome(user.getNome());
			entity.setEmail(user.getEmail());
			entity.setUserProfiles(user.getUserProfiles());
		}
	}

	public List<Usuario> findAll() {
		return repositorio.findAll();
	}

	public Usuario create(Usuario user) {
		return repositorio.save(user);
	}

	public Usuario findUserById(int id) {
		return repositorio.findOne(id);
	}

	public Usuario login(String email, String password) {
		return repositorio.findByLoginAndPassword(email, password);
	}

	public Usuario update(Usuario user) {
		return repositorio.save(user);
	}

	public void deleteUser(int id) {
		repositorio.delete(id);
	}

	public Usuario findUserByLogin(String login) {
		return repositorio.findUsuarioByLogin(login);
	}

	public boolean isLoginJaExistente(int id, String login) {
		return repositorio.findUsuarioByLogin(login) != null;
	}
	
	public List<Professor> findProfessores(){
		return professorRepo.findProfessores();
	}
	
	public List<Departamento> findDepartamentos(){
		return departamentoRepo.findDepartamentos();
	}
	
	public Departamento findDepartamentoByProfessor(String matriculaProfessor){
		return departamentoRepo.findDepartamentoByProfessor(matriculaProfessor);
	}

	public Departamento findDepartamentoBySigla(String siglaDepartamento){
		return departamentoRepo.findDepartamentoBySigla(siglaDepartamento);
	}
}
