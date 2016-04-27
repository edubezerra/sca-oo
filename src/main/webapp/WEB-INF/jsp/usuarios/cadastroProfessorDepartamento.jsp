<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SCA - Lista de Usu�rios</title>

<link href="<c:url value='/resources/bootstrap/css/bootstrap.css' />"
	rel="stylesheet" />
<link href="<c:url value='/resources/css/usuarios.css' />"
	rel="stylesheet" />
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

</head>

<body>
	<div class="generic-container">
		<div class="panel panel-default">
			<!-- Default panel contents -->
			<div class="panel-heading">
				<span class="lead">Lista de Usu�rios</span>
			</div>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Nome</th>
						<th>Login</th>
						<th>Email</th>
						<th>Departamento</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${professores}" var="professor">
						<tr>
						    <td>${professor.pessoa.nome}</td>
							<td>${professor.matricula}</td>
							<td>${professor.pessoa.email}</td>

							<td><select class="selectpicker">
									<c:forEach items="${departamentos}" var="departamento">
										<option>${departamento.sigla}</option>
									</c:forEach>
							</select></td>

						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="well">
			<a href="<c:url value='/usuarios/newuser' />">Adicionar Novo
				Usu�rio</a>
		</div>
	</div>
</body>
</html>