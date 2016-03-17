package br.cefetrj.sca.service;

import java.util.List;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.AlunoRepositorio;
import br.cefetrj.sca.dominio.HistoricoEscolar;
import br.cefetrj.sca.dominio.PeriodoLetivo;

@Component
public class RelatorioEvasaoService {

	@Autowired
	AlunoRepositorio alunoRepo;
	
    TreeMap<PeriodoLetivo, Integer> alunosPorSemestre = new TreeMap<>();
	
	public void initTreeMap(PeriodoLetivo periodoLetivo){
		
		PeriodoLetivo periodoAnalisar = periodoLetivo;
		PeriodoLetivo periodoFimAnalisar = PeriodoLetivo.PERIODO_CORRENTE;
		
		PeriodoLetivo aux = periodoAnalisar;

		while (aux.compareTo(periodoFimAnalisar) <= 0) {
			alunosPorSemestre.put(aux, 0);
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
				Integer qtd = alunosPorSemestre.get(periodo) == null? 0 : alunosPorSemestre.get(periodo) ;
				
				alunosPorSemestre.put(periodo, ++qtd);
			}
		}
		
		JSONArray array = new JSONArray();
		
		for(PeriodoLetivo key : alunosPorSemestre.keySet()) {
			JSONObject obj = new JSONObject();
	
			try {					
				obj.put("Periodo Letivo", key);
				obj.put("Alunos", alunosPorSemestre.get(key));
				
				array.put(obj);
			} catch(JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		return array;
		
	}
	
}
