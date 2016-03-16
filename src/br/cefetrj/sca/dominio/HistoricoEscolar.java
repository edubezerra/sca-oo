package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class HistoricoEscolar {

	@Id
	@GeneratedValue
	Long id;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "HISTORICO_ESCOLAR_ID", referencedColumnName = "ID")
	Set<ItemHistoricoEscolar> itens = new HashSet<>();

	@ManyToOne
	private VersaoCurso versaoCurso;

	@SuppressWarnings("unused")
	private HistoricoEscolar() {
	}

	public HistoricoEscolar(VersaoCurso versaoCurso) {
		this.versaoCurso = versaoCurso;
	}

	public Long getId() {
		return id;
	}

	/*
	 * Retorna a lista de disciplinas do curso da aluno em que ele pode se
	 * matricular. Um aluno pode se matrícular em uma disciplina D se as duas
	 * condições a seguir forem verdadeiras:
	 * 
	 * 1) o aluno já possuir os créditos em todas as disciplinas que são
	 * pré-requisitos de D (se essas existirem).
	 * 
	 * 2) o aluno não possuir os créditos de D.
	 */
	public List<Disciplina> getDisciplinasPossiveis() {

		List<Disciplina> disciplinas = this.versaoCurso.getDisciplinas();

		List<Disciplina> disciplinasCursadas = new ArrayList<Disciplina>();

		Iterator<ItemHistoricoEscolar> it = itens.iterator();
		EnumSituacaoAvaliacao aprovado = EnumSituacaoAvaliacao.APROVADO;
		EnumSituacaoAvaliacao isento = EnumSituacaoAvaliacao.ISENTO;
		EnumSituacaoAvaliacao isentoTransf = EnumSituacaoAvaliacao.ISENTO_POR_TRANSFERENCIA;
		EnumSituacaoAvaliacao creditos = EnumSituacaoAvaliacao.APROVEITAMENTO_CREDITOS;
		EnumSituacaoAvaliacao estudos = EnumSituacaoAvaliacao.APROVEITAMENTO_POR_ESTUDOS;

		while (it.hasNext()) {
			ItemHistoricoEscolar item = it.next();
			if (item.getSituacao() == aprovado || item.getSituacao() == isento
					|| item.getSituacao() == isentoTransf
					|| item.getSituacao() == creditos
					|| item.getSituacao() == estudos) {

				disciplinasCursadas.add(item.getDisciplina());
				disciplinas.remove(item.getDisciplina());
			}
		}

		/**
		 * Se o aluno não cursou todos os pré-requisitos de uma disciplina,
		 * então não pode se matricular nesta disciplina.
		 */
		Set<Disciplina> disciplinasParaRemover = new HashSet<>();
		for (Disciplina disciplina : disciplinas) {
			if (!disciplinasCursadas.containsAll(disciplina.getPreRequisitos())) {
				disciplinasParaRemover.add(disciplina);
			}
		}
		disciplinas.removeAll(disciplinasParaRemover);

		return disciplinas;
	}

	public void lancar(Disciplina disciplina, EnumSituacaoAvaliacao situacao,
			PeriodoLetivo periodo) {
		ItemHistoricoEscolar item = new ItemHistoricoEscolar(disciplina,
				situacao, periodo);
		this.itens.add(item);
	}
	
	public Set<ItemHistoricoEscolar> getItens() {
		return itens;
	}
	
	public Set<PeriodoLetivo> getPeriodosLetivosByItemHistoricoEscolar() {
		Set<PeriodoLetivo> set = new HashSet<>();
		
		for(ItemHistoricoEscolar item: itens) {
			set.add(item.getPeriodo());
		}
		
		return set;
	}
}
