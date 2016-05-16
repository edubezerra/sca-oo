<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<title>Isenção de Disciplina</title>
</head>
<body>
	<b> Aluno: ${aluno.nome }</b>
	<br></br>
	<b> Matrícula: ${aluno.matricula }</b>
	<br></br>
	<br></br>

	<c:if test="${aluno.processoIsencao != null}">
		<div class="table-responsive" >
				<table class="table">
					<thead>
						<tr>
							<th>ID</th>
							<th>Disciplina</th>
							<th>Situação</th>
							<th>Data Analise</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach items="${itemIsencaoByProcessoIsencao}"
							var="itemIsencaoByProcessoIsencao">
							<tr>
								<td>${itemIsencaoByProcessoIsencao.id}</td>
								<td>${itemIsencaoByProcessoIsencao.disciplina.nome}</td>
								<td>${itemIsencaoByProcessoIsencao.situacao}</td>
								<td>${itemIsencaoByProcessoIsencao.dataAnalise}</td>
							</tr>
						</c:forEach>
					</tbody>

				</table>
			</div>
			
					<div>				
				<a class="btn btn-success custom-width" type="submit"
		href="${pageContext.request.contextPath}/isencaoDisciplina/alunoView"> <i
		class="fa fa-arrow-left"> </i> Criar Nova Isenção
				</a>
			</div>
	</c:if>
	<c:if test="${aluno.processoIsencao == null}">
		<div>				
				<a class="btn btn-success custom-width" type="submit"
		href="${pageContext.request.contextPath}/isencaoDisciplina/alunoView"> <i
		class="fa fa-arrow-left"> </i> Criar Isenção
				</a>
			</div>
	</c:if>


	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/menuPrincipalView"> <i
		class="fa fa-arrow-left"> </i> Voltar
	</a>
</body>
</html>