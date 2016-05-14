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
<title>Isen��o de Disciplina</title>
</head>
<body>
	<!-- <h4 align="left">
		<b> Professor: ${professor.nome }</b> <br></br> <b> Matr�cula:
			${professor.matricula }</b>
	</h4>  
	<br></br> -->
	<h3 align="left">
	Aluno: ${aluno.nome }
	
	<br>
	Matr�cula: ${aluno.matricula }
	
	</h3>
	<br>
	<h3 align="center">Pedidos de Isen��o de Disciplina</h3>
	<br></br>

	<form action="${pageContext.request.contextPath}/isencaoDisciplina/professorSucesso"
		  method="POST">
		  <input name="aluno" value="${aluno.matricula}" type="hidden" />
		<div class="table-responsive" align="center">
			<table class="table">
				<thead>
					<tr>
						<th>ID</th>
						<th>C�digo</th>
						<th>Disciplina</th>
						<th>Situacao</th>

					</tr>
				</thead>
				<tbody>
					<c:forEach varStatus="i" items="${alunosItemIsencao}" var="alunosItemIsencao" >
					<input name="alunosItemIsencao" value="${alunosItemIsencao.disciplina.codigo}-${i.index}"
								type="hidden" />
						<tr>
							<td>${alunosItemIsencao.id}</td>
							<td>${alunosItemIsencao.disciplina.codigo}</td>
							<td>${alunosItemIsencao.disciplina.nome}</td>
							<td>

							<select name="radio" class="form-control ">
										<option value="" label="Selecionar..."
													selected disabled>Selecionar</option>
										<option value="deferir-${i.index}" >DEFERIR</option>
										<option value="indeferir-${i.index}" >INDEFERIR</option>
							</select>
							</td>
						</tr>
					</c:forEach>
				</tbody>

			</table>
			<button class="btn btn-success custom-width" type="submit"
				name="matricula">Confirmar</button>
		</div>
	</form>
	<a
		href="${pageContext.request.contextPath}/avaliacaoTurma/menuPrincipal">
		<input class="btn btn-default" type="button" value="Voltar" />
	</a>
</body>
</html>