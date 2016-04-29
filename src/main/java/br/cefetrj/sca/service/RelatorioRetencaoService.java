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
import br.cefetrj.sca.dominio.repositories.CursoRepositorio;

@Component
public class RelatorioRetencaoService {

	@Autowired
	AlunoRepositorio alunoRepo;
	
	@Autowired
	CursoRepositorio cursoRepo;
	
    private TreeMap<PeriodoLetivo, List<Aluno>> alunosRetencao;
    
	public void initTreeMap(PeriodoLetivo periodoLetivo){
		
		alunosRetencao = new TreeMap<>();
		
		PeriodoLetivo periodoAnalisar = periodoLetivo;
		PeriodoLetivo periodoFimAnalisar = PeriodoLetivo.PERIODO_CORRENTE;
		
		PeriodoLetivo aux = periodoAnalisar;

		while (aux.compareTo(periodoFimAnalisar) <= 0) {
			alunosRetencao.put(aux, new ArrayList<>());
			aux = aux.proximo();
		}
	
	}
	
    public JSONArray createDataResponse(String siglaCurso, String periodoLetivo){
		
		String[] periodoInfos = periodoLetivo.split("/");
		
		String periodoMatricula = periodoInfos[0].substring(periodoInfos[0].length() - 2) + periodoInfos[1];
		
		List<Aluno> alunosList = alunoRepo.getAlunosByCursoEPeriodo(siglaCurso, periodoMatricula);
		
        PeriodoLetivo periodoMinimo = new PeriodoLetivo(Integer.parseInt(periodoInfos[0]),Integer.parseInt(periodoInfos[1]));
        
        //int qtdPeriodoMinimo = alunosList.get(0).getVersaoCurso().getPeriodoMinimo();
        
        int qtdPeriodoMinimo = 6;
        
        for(int i = 0; i < qtdPeriodoMinimo; i++){
        	
        	periodoMinimo = periodoMinimo.proximo();
        	
        }
        
        initTreeMap(periodoMinimo);
        
		for(Aluno aluno: alunosList) {
			
			HistoricoEscolar hist = aluno.getHistorico();
			
			for(PeriodoLetivo periodo: hist.getPeriodosLetivosByItemHistoricoEscolar()){
				
				if(periodo.compareTo(periodoMinimo) >= 0){
					
					List<Aluno> alunos = alunosRetencao.get(periodo) == null? new ArrayList<>() : alunosRetencao.get(periodo);
					
					alunos.add(aluno);
					
					alunosRetencao.put(periodo, alunos);	
					
				}
				
			}
		}
		
		JSONArray array = new JSONArray();
		
		for(PeriodoLetivo key : alunosRetencao.keySet()) {
			JSONObject obj = new JSONObject();
	
			try {	
				
				obj.put("Periodo Letivo", key);
				obj.put("Alunos", alunosRetencao.get(key).size());
				
				JSONArray arrayAlunos = new JSONArray();
				for(Aluno a: alunosRetencao.get(key)) {
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

