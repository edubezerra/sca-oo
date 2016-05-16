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
	<b>   Aluno: ${aluno.nome }</b>
	<br></br>
	<b>   Matrícula: ${aluno.matricula }</b>
	<br></br>
	<br></br>

	<div class="table-responsive" align="center">
		<form action="${pageContext.request.contextPath}/isencaoDisciplina/validaComprovante"  enctype="multipart/form-data" method="POST">
		 <label for="inputFile">Anexar comprovante de solicitação
								de matrícula do período atual (Formatos aceitos: PDF, JPEG ou
								PNG. Tamanho Máximo: 10mb)</label>
							<input type="file" name="file" id="inputFile" required>
		<table class="table">
			<thead>
				<tr>
					<th>Disciplina</th>
					<th>Selecionar</th>

				</tr>
			</thead>
			
			<tbody>
			
				<c:forEach items="${disciplinas}" var="disciplina">
					<tr>
						<td>
						    ${disciplina.nome}
						</td>
						
                        <td> 
                          <input type="checkbox" name="choice" value="${disciplina.id}"> 
                        </td>
					</tr>
				</c:forEach>
			</tbody>
			
		</table>
		
	     <button class="btn btn-success custom-width" type="submit"
				name="matricula">Confirmar</button></form>
	</div>
<a class="btn btn-default"
			href="${pageContext.request.contextPath}/menuPrincipalView"> <i
			class="fa fa-arrow-left"> </i> Voltar
		</a>
</body>
</html>