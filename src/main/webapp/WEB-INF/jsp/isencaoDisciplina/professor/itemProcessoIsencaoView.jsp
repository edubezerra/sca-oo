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


	<div class="table-responsive" align="center">
		<table class="table">
			<thead>
				<tr>
					<th>ID</th>
					<th>Código</th>
					<th>Disciplina</th>
					<th>Situacao</th>

				</tr>
			</thead>

			<tbody>

				<c:forEach items="${alunosItemIsencao}" var="alunosItemIsencao">
					
					<tr>
						<td>${alunosItemIsencao.id}</td>
						<td>${alunosItemIsencao.disciplina.codigo}</td>
						<td>${alunosItemIsencao.disciplina.nome}</td>
						<td>
						<div>
						<form action="">
						<label class="radio-inline"><input type="radio"
								name="optradio" value="${alunosItemIsencao.situacao}">DEFERIR
						</label> 
						<label class="radio-inline"><input type="radio"
								name="optradio" value="${alunosItemIsencao.situacao}">INDEFERIR
						</label>
						</form>
						</div>
						</td>
					</tr>
				</c:forEach>
			</tbody>

		</table>
	</div>
	
	<a href="${pageContext.request.contextPath}/avaliacaoTurma/menuPrincipal">
			<input class="btn btn-default" type="button" value="Voltar" />
		</a>

</body>
</html>