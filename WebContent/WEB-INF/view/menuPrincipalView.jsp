<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>SCA - Home</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/vendor/bootstrap.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="menu-principal">
	<div class="container">
		<div class="row">
			<h1 class="text-center">Menu principal</h1>

			<c:if test="${requestScope.error != null}">
				<span class="label label-danger">${requestScope.error}</span>
			</c:if>

			<c:if test="${requestScope.info != null}">
				<span class="label label-default">${requestScope.info}</span>
			</c:if>
		</div>
		<hr />
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<h1>Aluno</h1>
				<ul class="nav nav-pills nav-stacked text-center">
					<li><a class="btn-default"
						href="${pageContext.request.contextPath}/avaliacaoTurma/avaliacaoTurmas">
							Avaliação de Turmas por Discentes </a></li>
					<li><a class="btn-default"
						href="${pageContext.request.contextPath}/inclusaoDisciplina/homeInclusao">
							Inclusão de Disciplina Fora de Prazo</a></li>

					<li><a class="btn-default"
						href="${pageContext.request.contextPath}/avaliacaoEgresso/escolherAvaliacao">
							Avaliação de Curso por Egresso</a></li>

					<!-- 					<li><a class="btn-default" -->
					<%-- 						href="${pageContext.request.contextPath}/isencaoDisciplinas/visualizarResultados">Visualização --%>
					<!-- 							de Resultados de Solicitações de Isenção de Disciplinas</a></li> -->

					<!-- 					<li><a class="btn-default" -->
					<%-- 						href="${pageContext.request.contextPath}/isencaoDisciplinas/homeInclusao">Solicitações de --%>
					<!-- 							Isenção de Disciplinas</a></li> -->



				</ul>
				<h1>Professor</h1>
				<ul class="nav nav-pills nav-stacked text-center">

					<li><a class="btn-default"
						href="${pageContext.request.contextPath}/gradedisponibilidades/apresentarFormulario">
							Fornecimento de Grade de Disponibilidades
							(${requestScope.periodoLetivo})</a></li>

					<li><a class="btn-default"
						href="${pageContext.request.contextPath}/professor/homeInclusao/">
							Análise de Solicitações de Matrícula Fora do Prazo</a></li>
							
				    <li><a class="btn-default"
	                    href="${pageContext.request.contextPath}/relatorioEvasao/homeEvasao">
					        Relatório de Evasão</a></li>

				</ul>

				<h1>Geral</h1>
				<ul class="nav nav-pills nav-stacked text-center">
					<li class="active"><a
						href="${pageContext.request.contextPath}/logout"> Sair do
							sistema</a></li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>
