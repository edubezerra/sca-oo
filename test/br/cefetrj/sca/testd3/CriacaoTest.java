package br.cefetrj.sca.testd3;

import java.util.List;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.AlunoRepositorio;
import br.cefetrj.sca.dominio.HistoricoEscolar;
import br.cefetrj.sca.dominio.PeriodoLetivo;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("file:sca/WebContent")
@ContextConfiguration(locations = { "file:src/applicationContext.xml" })
@Transactional
public class CriacaoTest {

	@Autowired
	AlunoRepositorio rep;
	
	TreeMap<PeriodoLetivo, Integer> alunosPorSemestre = new TreeMap<>();
	
	PeriodoLetivo periodoAnalisar;
	PeriodoLetivo periodoFimAnalisar;
	PeriodoLetivo aux;
	
	@Test
	public void test() throws Exception{
		periodoAnalisar = new PeriodoLetivo(2013, 1);
		periodoFimAnalisar = PeriodoLetivo.PERIODO_CORRENTE;
		
		aux = periodoAnalisar;
		while(!aux.equals(periodoFimAnalisar)) {			
			alunosPorSemestre.put(aux, 0);
			
			aux = aux.proximo();
		}
		
		alunosPorSemestre.put(periodoFimAnalisar, 0);
		
		List<Aluno> alunosBCC = rep.getAlunosByCursoEPeriodo("BCC", periodoAnalisar.toString());
		caseAlunos(alunosBCC);		
	}
	
	public void caseAlunos(List<Aluno> alunoList) throws Exception{
		for(Aluno aluno: alunoList) {
			HistoricoEscolar hist = aluno.getHistorico();
			
			for(PeriodoLetivo periodo: hist.getPeriodosLetivosByItemHistoricoEscolar()){
				Integer qtd = alunosPorSemestre.get(periodo) == null? 0 : alunosPorSemestre.get(periodo) ;
				
				alunosPorSemestre.put(periodo, ++qtd);
			}
		}
		
		
		
		JSONArray array = new JSONArray();
		
		for(PeriodoLetivo key : alunosPorSemestre.keySet()) {
			JSONObject obj = new JSONObject();
			
			obj.put("Periodo Letivo", key);
			obj.put("Alunos", alunosPorSemestre.get(key));
			
			array.put(obj);
			
			//System.out.println("Periodo: " + key + " | Qtd: " + alunosPorSemestre.get(key));
		}
		
		System.out.println(array);
		
	}
	
	
}
