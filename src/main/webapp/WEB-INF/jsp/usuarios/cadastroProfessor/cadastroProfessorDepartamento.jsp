<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SCA - Lista de Usuários</title>

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
			<form
				action="${pageContext.request.contextPath}/usuarios/setListProfessorDepartamento"
				method="POST">
				<!-- Default panel contents -->
				<div class="panel-heading">
					<span class="lead">Lista de Usuários</span>
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

						<c:forEach varStatus="i" items="${professores}" var="professor">
							<input name="matricula" value="${professor.matricula}-${i.index}"
								type="hidden" />
							<tr>
								<td>${professor.pessoa.nome}</td>
								<td>${professor.matricula}</td>
								<td>${professor.pessoa.email}</td>

								<td><select name="departamento">
										<c:forEach items="${profDep}" var="prof">
											<c:choose>
												<c:when test="${prof.matricula == professor.matricula}">
													<option value="" class="form-control" label="Departamento"
														selected disabled>Departamento</option>
												</c:when>
												<c:otherwise>
													<option value="" class="form-control" label="Selecionar..."
														selected disabled>Selecionar</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
										<c:forEach items="${departamentos}" var="departamento">
											<option value="${departamento.sigla}-${i.index}">${departamento.sigla}</option>
										</c:forEach>
								</select></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<button type="submit" class="btn btn-default">Adicionar</button>

			</form>
		</div>
	</div>
</body>
</html>