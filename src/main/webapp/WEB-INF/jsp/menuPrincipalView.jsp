<!DOCTYPE html>
<%@page import="br.cefetrj.sca.web.controllers.UserController"%>
<%@include file="taglib.jsp"%>
<html>
<head>
<title>Welcome</title>

<link href="${rootURL}resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${rootURL}resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${rootURL}resources/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${rootURL}resources/js/app.js"></script>

</head>
<body>

	<h2>
		Bem-vindo,
		<%=UserController.getCurrentUser().getName()%>!
	</h2>
	<h3>
		Matr�cula:
		<sec:authentication property="name" />
	</h3>
	<h3>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<a href="${rootUrl}admin">Administration</a>
		</sec:authorize>
	</h3>

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

		<sec:authorize access="hasRole('ROLE_ALUNO')">
			<h3>Aluno</h3>
			<ul>
				<li><a
					href="${pageContext.request.contextPath}/avaliacaoTurma/avaliacaoTurmas">
						Avalia��o de Turmas por Discentes </a></li>
				<li><a
					href="${pageContext.request.contextPath}/inclusaoDisciplina/homeInclusao">
						Inclus�o de Disciplina Fora de Prazo</a></li>
				<li><a
					href="${pageContext.request.contextPath}/avaliacaoEgresso/escolherAvaliacao">
						Avalia��o de Curso por Egresso</a></li>

				<li><a
					href="${pageContext.request.contextPath}/isencaoDisciplinaController/isencaoDisciplina">
						Isen��o de Disciplina </a></li>
			</ul>
		</sec:authorize>

		<sec:authorize access="hasRole('ROLE_PROFESSOR')">
			<h3>Professor</h3>
			<ul>

				<li><a
					href="${pageContext.request.contextPath}/gradedisponibilidades/apresentarFormulario">
						Fornecimento de Grade de Disponibilidades
						(${requestScope.periodoLetivo})</a></li>

				<li><a
					href="${pageContext.request.contextPath}/professor/homeInclusao/">
						An�lise de Solicita��es de Matr�cula Fora do Prazo</a></li>
			</ul>
		</sec:authorize>

		<h3>
			<a href="logout">Logout</a>
		</h3>
	</div>
</body>
</html>