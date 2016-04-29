package br.cefetrj.sca.dominio;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

@Embeddable
public final class PeriodoLetivo implements Comparable<PeriodoLetivo> {
	public enum EnumPeriodo {
		PRIMEIRO, SEGUNDO
	};

	final private Integer ano;

	@Enumerated(EnumType.ORDINAL)
	final private EnumPeriodo periodo;

	@Transient
	public static final PeriodoLetivo PERIODO_CORRENTE;

	/**
	 * Lógica para obtenção do valor do semestre letivo corrente (atual).
	 */
	static {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		EnumPeriodo periodo;
		if (calendar.get(Calendar.MONTH) <= Calendar.JUNE)
			periodo = EnumPeriodo.PRIMEIRO;
		else
			periodo = EnumPeriodo.SEGUNDO;
		PERIODO_CORRENTE = new PeriodoLetivo(calendar.get(Calendar.YEAR),
				periodo);
	}

	public Integer getAno() {
		return ano;
	}

	public EnumPeriodo getPeriodo() {
		return periodo;
	}

	@SuppressWarnings("unused")
	private PeriodoLetivo() {
		this.ano = null;
		this.periodo = null;
	}

	public PeriodoLetivo(Integer ano, EnumPeriodo periodo) {
		super();
		if (ano == null) {
			throw new IllegalArgumentException("Ano deve ser fornecido.");
		}
		if (periodo == null) {
			throw new IllegalArgumentException("Período deve ser fornecido.");
		}
		this.ano = ano;
		this.periodo = periodo;
	}

	public PeriodoLetivo(int ano, int periodo) {
		this.ano = ano;
		if (periodo == 1) {
			this.periodo = EnumPeriodo.PRIMEIRO;
		} else if (periodo == 2) {
			this.periodo = EnumPeriodo.SEGUNDO;
		} else {
			throw new IllegalArgumentException("Valor inválido para o período!");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ano == null) ? 0 : ano.hashCode());
		result = prime * result + ((periodo == null) ? 0 : periodo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeriodoLetivo other = (PeriodoLetivo) obj;
		if (ano == null) {
			if (other.ano != null)
				return false;
		} else if (!ano.equals(other.ano))
			return false;
		if (periodo != other.periodo)
			return false;
		return true;
	}

	@Override
	protected Object clone() {
		return new PeriodoLetivo(ano, periodo);
	}

	@Override
	public String toString() {
		return "" + ano + "/" + (periodo.ordinal() + 1);
	}

	public PeriodoLetivo proximo() {
		EnumPeriodo outroPeriodo;
		Integer outroAno;
		if (this.periodo == EnumPeriodo.PRIMEIRO) {
			outroPeriodo = EnumPeriodo.SEGUNDO;
			outroAno = this.ano;
		} else {
			outroPeriodo = EnumPeriodo.PRIMEIRO;
			outroAno = this.ano + 1;
		}
		return new PeriodoLetivo(outroAno, outroPeriodo);
	}

	@Override
	public int compareTo(PeriodoLetivo outro) {
		return 2* (this.ano - outro.ano) + this.periodo.ordinal() - outro.periodo.ordinal();
	}

	public PeriodoLetivo sucessor(PeriodoLetivo outro) {
		return null;
	}

	public PeriodoLetivo antecessor(PeriodoLetivo outro) {
		return null;
	}
}
