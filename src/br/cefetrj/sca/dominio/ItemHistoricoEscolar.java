package br.cefetrj.sca.dominio;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ItemHistoricoEscolar {

	@Id
	@GeneratedValue
	Long id;

	@OneToOne
	Disciplina disciplina;

	@Enumerated(EnumType.ORDINAL)
	EnumSituacaoAvaliacao situacao;

	private PeriodoLetivo periodo;

	private ItemHistoricoEscolar() {
	}

	public ItemHistoricoEscolar(Disciplina disciplina,
			EnumSituacaoAvaliacao situacao, PeriodoLetivo periodo) {
		this.disciplina = disciplina;
		this.situacao = situacao;
		this.periodo = periodo;
	}

	public Long getId() {
		return id;
	}

	public EnumSituacaoAvaliacao getSituacao() {
		return situacao;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}
	
	public PeriodoLetivo getPeriodo() {
		return periodo;
	}

	@Override
	public String toString() {
		return "ItemHistoricoEscolar [disciplina=" + disciplina + ", situacao=" + situacao + ", periodo=" + periodo
				+ "]";
	}
	
}
