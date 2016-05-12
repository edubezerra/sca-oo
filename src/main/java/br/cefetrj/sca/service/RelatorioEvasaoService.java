package br.cefetrj.sca.service;

import java.util.ArrayList;

import java.util.List;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.HistoricoEscolar;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;

@Component
public class RelatorioEvasaoService {

	@Autowired
	AlunoRepositorio alunoRepo;
	
    private TreeMap<PeriodoLetivo, List<Aluno>> alunosPorSemestre;
	
	public void initTreeMap(PeriodoLetivo periodoLetivo){
		
		alunosPorSemestre = new TreeMap<>();
		
		PeriodoLetivo periodoAnalisar = periodoLetivo;
		PeriodoLetivo periodoFimAnalisar = PeriodoLetivo.PERIODO_CORRENTE;
		
		PeriodoLetivo aux = periodoAnalisar;

		while (aux.compareTo(periodoFimAnalisar) <= 0) {
			alunosPorSemestre.put(aux, new ArrayList<>());
			aux = aux.proximo();
		}
	
	}
	
	public JSONArray createDataResponse(String siglaCurso, String periodoLetivo){
		
		String[] periodoInfos = periodoLetivo.split("/");
		PeriodoLetivo periodoAnalisar = new PeriodoLetivo(Integer.parseInt(periodoInfos[0]), Integer.parseInt(periodoInfos[1]));
		
		initTreeMap(periodoAnalisar);
		
		String periodoMatricula = periodoInfos[0].substring(periodoInfos[0].length() - 2) + periodoInfos[1];
		
		List<Aluno> alunosList = alunoRepo.getAlunosByCursoEPeriodo(siglaCurso, periodoMatricula);
		
		for(Aluno aluno: alunosList) {
			HistoricoEscolar hist = aluno.getHistorico();
			
			for(PeriodoLetivo periodo: hist.getPeriodosLetivosByItemHistoricoEscolar()){
				
				List<Aluno> alunos = alunosPorSemestre.get(periodo) == null? new ArrayList<>() : alunosPorSemestre.get(periodo);
				
				alunos.add(aluno);
				
				alunosPorSemestre.put(periodo, alunos);
			}
		}
		
		JSONArray array = new JSONArray();
		
		for(PeriodoLetivo key : alunosPorSemestre.keySet()) {
			JSONObject obj = new JSONObject();
	
			try {					
				obj.put("Periodo Letivo", key);
				obj.put("Alunos", alunosPorSemestre.get(key).size());
				
				JSONArray arrayAlunos = new JSONArray();
				for(Aluno a: alunosPorSemestre.get(key)) {
					arrayAlunos.put(a.getNome());
				}
				
				obj.put("Lista de Alunos", arrayAlunos) ;
				
				array.put(obj);
			} catch(JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		return array;
		
	}
}
