package br.cefetrj.sca.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.isencoes.FichaIsencaoDisciplinas;
import br.cefetrj.sca.dominio.isencoes.ItemPedidoIsencaoDisciplina;
import br.cefetrj.sca.dominio.isencoes.PedidoIsencaoDisciplinas;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositories.PedidoIsencaoDisciplinasRepositorio;
import br.cefetrj.sca.service.util.SituacaoAlunoAtividades;

@Component
public class PedidoIsencaoDisciplinasService {

	private static final int TAMANHO_MAXIMO_COMPROVANTE = 10000000;

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	private PedidoIsencaoDisciplinasRepositorio pedidoIsencaoDisciplinasRepo;

	@Autowired
	private DisciplinaRepositorio disciplinaRepo;

	@Autowired
	PedidoIsencaoDisciplinasRepositorio processoIsencaoRepo;

	/**
	 * Esse método é invocado quando um aluno solicita o registro de isenção de
	 * disciplinas.
	 * 
	 * @param matriculaAluno
	 *            CPF do aluno solicitante.
	 * 
	 * @return objeto com informações para construir formulário de registros de
	 *         isenções.
	 * 
	 */
	public FichaIsencaoDisciplinas obterFichaIsencaoDisciplinas(String matriculaAluno) {
		PedidoIsencaoDisciplinas pedido = pedidoIsencaoDisciplinasRepo.findByMatriculaAluno(matriculaAluno);
		Aluno aluno = getAlunoPorMatricula(matriculaAluno);
		List<Disciplina> disciplinas = disciplinaRepo.findAllEmVersaoCurso(aluno.getVersaoCurso());
		return new FichaIsencaoDisciplinas(aluno, pedido, disciplinas);
	}

	public SituacaoAlunoAtividades obterSituacaAluno(String matriculaAluno) {

		Aluno aluno = getAlunoPorMatricula(matriculaAluno);

		String totalCH = aluno.getCargaHorariaCumpridaAtiv().toHours() + "/"
				+ aluno.getVersaoCurso().getCargaHorariaMinAitvComp().toHours();

		SituacaoAlunoAtividades dadosAluno = new SituacaoAlunoAtividades(aluno.getNome(),
				aluno.getVersaoCurso().getCurso(), aluno.getVersaoCurso().getNumero(), aluno.temAtividadesSuficientes(),
				totalCH);
		return dadosAluno;
	}

	public void adicionarItemNoPedido(String matriculaAluno, Long idDisciplinaInterna, String nomeDisciplinaExterna,
			String notaFinalDisciplinaExterna, String cargaHoraria, String observacao, MultipartFile file)
					throws IOException {

		Aluno aluno = getAlunoPorMatricula(matriculaAluno);

		if (aluno != null) {

			PedidoIsencaoDisciplinas pedido = processoIsencaoRepo.findByAluno(aluno);

			if (pedido == null) {
				pedido = new PedidoIsencaoDisciplinas(aluno);
			}

			Disciplina disciplina = disciplinaRepo.findOne(idDisciplinaInterna);

			validaComprovante(file);
			Comprovante doc = new Comprovante(file.getContentType(), file.getBytes(), file.getOriginalFilename());

			pedido.comMaisUmItem(disciplina, nomeDisciplinaExterna,
					notaFinalDisciplinaExterna, cargaHoraria, observacao, doc);

			processoIsencaoRepo.save(pedido);
		}
	}

	public void removerItemDoPedido(String matriculaAluno, Long idItem) {

		Aluno aluno = getAlunoPorMatricula(matriculaAluno);

		if (aluno != null) {

			PedidoIsencaoDisciplinas pedido = processoIsencaoRepo.findByAluno(aluno);

			if (idItem == null || idItem <= 0) {
				throw new IllegalArgumentException("Erro: id de item de pedido de isenção inválido.");
			}

			pedido.removerItem(idItem);

			processoIsencaoRepo.save(pedido);
		}
	}

	private Aluno getAlunoPorMatricula(String matriculaAluno) {
		if (matriculaAluno == null || matriculaAluno.trim().equals("")) {
			throw new IllegalArgumentException("Matrícula deve ser fornecida!");
		}

		Aluno aluno = null;

		try {
			aluno = alunoRepo.findAlunoByMatricula(matriculaAluno);
		} catch (Exception e) {
			throw new IllegalArgumentException("Aluno não encontrado (" + matriculaAluno + ")", e);
		}

		return aluno;
	}

	public void validaComprovante(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("Erro: Comprovante não fornecido. "
					+ "Por favor, forneça o arquivo com o comprovante necessário para a isenção.");
		}

		if (file.getSize() > TAMANHO_MAXIMO_COMPROVANTE) {
			throw new IllegalArgumentException("O arquivo de comprovante deve ter 10mb no máximo");
		}
		String[] tiposAceitos = { "application/pdf", "image/jpeg", "image/png" };
		if (ArrayUtils.indexOf(tiposAceitos, file.getContentType()) < 0) {
			throw new IllegalArgumentException("O arquivo de comprovante deve ser no formato PDF, JPEG ou PNG");
		}
	}

	public Comprovante getComprovante(String matriculaAluno, Long idItem) {

		Aluno aluno = getAlunoPorMatricula(matriculaAluno);

		PedidoIsencaoDisciplinas pedido = processoIsencaoRepo.findByAluno(aluno);

		if (pedido != null) {
			Comprovante comprovante = pedido.getComprovanteDoItem(idItem);
			return comprovante;
		}

		return null;
	}

	/**
	 * A lista de disciplinas possíveis para isenção deve excluir as disciplinas
	 * para a quais o aluno já solicitou isenção.
	 * 
	 * @param matricula
	 * @return
	 */
	public List<Disciplina> getDisciplinasPossiveisParaIsencao(String matricula) {
		Aluno aluno = getAlunoPorMatricula(matricula);
		VersaoCurso versaoCurso = aluno.getVersaoCurso();

		PedidoIsencaoDisciplinas pedido = processoIsencaoRepo.findByAluno(aluno);

		List<Disciplina> disciplinasJaSolicitadas = new ArrayList<>();

		if (pedido != null) {
			List<ItemPedidoIsencaoDisciplina> itensPedido = pedido.getItens();
			for (ItemPedidoIsencaoDisciplina itemPedido : itensPedido) {
				disciplinasJaSolicitadas.add(itemPedido.getDisciplina());
			}
		}

		List<Disciplina> disciplinas = disciplinaRepo.findAllEmVersaoCurso(versaoCurso);

		disciplinas.removeAll(disciplinasJaSolicitadas);

		return disciplinas;
	}

}
