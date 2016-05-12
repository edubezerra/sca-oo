package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.EnumSituacaoAvaliacao;
import br.cefetrj.sca.dominio.Inscricao;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;

@Component
public class RelatorioReprovacaoTurmaService {

	@Autowired
	TurmaRepositorio turmaRepo;
	
	private TreeMap<PeriodoLetivo, List<Aluno>> alunosPorSemestre;
	
	public JSONArray createDataResponse(String codigoTurma){
		alunosPorSemestre = new TreeMap<>();
		
		List<Turma> turmaList = turmaRepo.findTurmasByCodigo(codigoTurma);
		
		System.out.println("Quantidade de turmas: " + turmaList);
		
		for(Turma t: turmaList) {
			Set<Inscricao> inscricaoSet = t.getInscricoes();
			
			System.out.println("Quantidade de inscricoes: " + inscricaoSet.size());
			
			for(Inscricao i : inscricaoSet) {				
				if(i.getAvaliacao().equals(EnumSituacaoAvaliacao.REPROVADO_POR_MEDIA)
						|| i.getAvaliacao().equals(EnumSituacaoAvaliacao.REPROVADO_POR_FALTAS)
						|| i.getAvaliacao().equals(EnumSituacaoAvaliacao.REPROVADO_SEM_NOTA)) {
					
					System.out.println(" ----- REPROVADO -----");
					
					List<Aluno> alunos = alunosPorSemestre.get(t.getPeriodo()) == null? new ArrayList<>() : alunosPorSemestre.get(t.getPeriodo());
					alunos.add(i.getAluno());
					
					alunosPorSemestre.put(t.getPeriodo(), alunos);
				}

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
