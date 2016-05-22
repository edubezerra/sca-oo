package br.cefetrj.sca.web.controllers;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;

public class GerenteArquivos {

	public static void downloadFile(String cpf, Long solicitacaoId, HttpServletRequest request,
			HttpServletResponse response, Comprovante comprovante) throws IOException {
		response.setContentType(comprovante.getContentType());
		response.setHeader("Content-Disposition", "attachment;filename=\"" + comprovante.getNome() + "\"");
		OutputStream out = response.getOutputStream();
		response.setContentType(comprovante.getContentType());
		out.write(comprovante.getData());
		out.flush();
		out.close();
	} 
	
	
	public static void downloadFileNovo(Long solicitacaoId, HttpServletRequest request,
			HttpServletResponse response, Comprovante comprovante) throws IOException {
		response.setContentType(comprovante.getContentType());
		response.setHeader("Content-Disposition", "attachment;filename=\"" + comprovante.getNome() + "\"");
		OutputStream out = response.getOutputStream();
		response.setContentType(comprovante.getContentType());
		out.write(comprovante.getData());
		out.flush();
		out.close();
	}
}
