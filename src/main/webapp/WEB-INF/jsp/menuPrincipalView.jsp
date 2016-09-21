<!DOCTYPE html>
<%@page import="br.cefetrj.sca.web.controllers.UsuarioController"%>
<%@include file="taglib.jsp"%>
<html>
<head>
<title>SCA</title>

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
		<%=UsuarioController.getCurrentUser().getNome()%>!
	</h2>
	<h3>
		Matrícula:
		<%=UsuarioController.getCurrentUser().getMatricula()%>
		<%-- 		<sec:authentication property="name" /> --%>
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
						Avaliação de Professores </a></li>
				<li><a
					href="${pageContext.request.contextPath}/registroAtividades/registroAtividades">
						Atividades Complementares </a></li>
				<li><a
					href="${pageContext.request.contextPath}/matriculaForaPrazo/requerimento/visualizarRequerimentos">
						Matrícula Fora de Prazo</a></li>
				<li><a
					href="${pageContext.request.contextPath}/avaliacaoEgresso/escolherAvaliacao">
						Avaliação de Curso por Egresso</a></li>
				<li><a
					href="${pageContext.request.contextPath}/realizarInscricao/realizarInscricao">
						Realização de Inscriçôes</a></li>
				<li><a
					href="${pageContext.request.contextPath}/isencaoDisciplina/visualizarProcessoIsencao">
						Isenção de Disciplinas </a></li>
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
					href="${pageContext.request.contextPath}//visualizacaoAvaliacaoDiscente/turma">
						Selecao da Turma
						(${requestScope.periodoLetivo})</a></li>
			</ul>
		</sec:authorize>

		<sec:authorize access="hasRole('ROLE_COORDENADOR_CURSO')">
			<h3>Coordenador de Curso</h3>
			<ul>
				<li><a
					href="${pageContext.request.contextPath}/matriculaForaPrazo/analise/homeInclusao/">
						Matrícula Fora do Prazo</a></li>
				<li><a
					href="${pageContext.request.contextPath}/isencaoDisciplina/visualizaAlunosSolicitantes">
						Isenção de Disciplinas </a></li>

				<li><a
					href="${pageContext.request.contextPath}/relatorioEvasao/homeEvasao">
						Relatório de Evasão</a></li>

				<li><a
					href="${pageContext.request.contextPath}/relatorioRetencao/homeRetencao">
						Relatório de Retenção</a></li>

				<li><a
					href="${pageContext.request.contextPath}/relatorioReprovacaoDisciplina/homeReprovacaoDisciplina">
						Relatório de Reprovação por Disciplina</a></li>
			</ul>
		</sec:authorize>

		<sec:authorize access="hasRole('ROLE_COORDENADOR_ATIVIDADES')">
			<h3>Coordenador de Atividades Complementares</h3>
			<ul>
				<li><a
					href="${pageContext.request.contextPath}/analiseAtividades/homeAnalise">
						Análise de Registros de Atividade Complementar </a></li>
			</ul>
		</sec:authorize>

		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<h3>Administrador</h3>
			<a href="${rootUrl}admin">Administration</a>
			<ul>
				<li><a href="${pageContext.request.contextPath}/usuarios/list">
						Listar usuários </a></li>
				<li><a
					href="${pageContext.request.contextPath}/alocacaoDisciplinaDepartamento/homeAlocacaoDisciplinas">
						Alocar Disciplinas a Departamentos</a></li>
				<li><a
					href="${pageContext.request.contextPath}/alocacaoProfessorDepartamento/homeAlocacaoProfessores">
						Alocar Professores a Departamentos</a></li>
				<li><a
					href="${pageContext.request.contextPath}/coordenacaoAtividades/homeAlocacaoCoordenadorAtividades">
						Alocar Coordenadores de Atividades Complementares </a></li>
				<li><a
					href="${pageContext.request.contextPath}/importacaoDados/homeImportacaoDados">
						Importar Dados</a></li>
			</ul>
		</sec:authorize>

		<h3>
			<a href="${pageContext.request.contextPath}/logout">Logout</a>
		</h3>
	</div>
</body>
</html>