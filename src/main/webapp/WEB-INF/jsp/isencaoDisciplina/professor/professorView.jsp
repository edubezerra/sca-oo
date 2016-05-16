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
	<h4 align="center">
		<b> Professor: ${professor.nome }</b> <br></br> <b> Matrícula:
			${professor.matricula }</b>
	</h4>
	<br></br>
	<h3 align="center">Pedidos de Isenção de Disciplina</h3>
	<br></br>
	<div class="panel panel-default">
	<form action="${pageContext.request.contextPath}/isencaoDisciplina/itemProcessoIsencaoView"
				method="POST">
	<div class="table-responsive" align="center">
		<table class="table">
			<thead>
				<tr>
					<th>Matricula</th>
					<th>Nome</th>
					<th></th>

				</tr>
			</thead>

			<tbody>

				<c:forEach items="${alunosProcessoIsencao}" var="alunosProcessoIsencao">
					
					<tr>
						<td>${alunosProcessoIsencao.matricula}</td>
						<td>${alunosProcessoIsencao.nome}</td>						
						<td>
						<button class="btn btn-info btn-sm custom-width" type="submit"
								value="${alunosProcessoIsencao.matricula}" name="matricula">Visualizar</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>

		</table>
	</div>
	
	</form>
	</div>
<a class="btn btn-default"
			href="${pageContext.request.contextPath}/menuPrincipalView"> <i
			class="fa fa-arrow-left"> </i> Voltar
		</a>
</body>
</html>