<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta content="charset=UTF-8">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="${rootURL}resources/bootstrap/js/bootstrap.js"></script>
	<link href="${rootURL}resources/bootstrap/css/bootstrap.css" media="screen" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${rootURL}resources/jquery/jquery-1.10.2.js"></script>
	<script src='//assets.codepen.io/assets/common/stopExecutionOnTimeout.js?t=1'></script>
	<script src='//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	<script src='http://d3js.org/d3.v3.min.js'></script>
	<script src="http://dimplejs.org/dist/dimple.v2.1.6.min.js"></script>	
   <title>Relatório de Evasão</title>
</head>

<style>

ul
{
    list-style-type: none;
}

#RecallsChart {
	position:relative;
	display:inline-block;
}
#RecallDetails {
	display:inline-block;
	height:540px;
	width:500px;
	padding:.8em;
	position:absolute;
	margin-left:10px;
	overflow-y:auto;
  font-size:.8em;
}
.maker {
	font-weight:700;
	font-size:1.3em;
}

.content {

    text-align: center;
    padding: 10px;
    border-radius: 6px;
    margin: 0 auto; 

}

button[type=submit]{

    margin: 0 auto;
    width: 25%;
    height: 25%;

}

.form-inputs{

	padding: 10px;
	margin-right: 10px;
	font-size: 14px;
	line-height: 1.42857143;
	color: #555;
	background-color: #fff;
	background-image: none;
	border: 1px solid #ccc;
	border-radius: 4px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	-webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow
		ease-in-out .15s;
	-o-transition: border-color ease-in-out .15s, box-shadow ease-in-out
		.15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s
   
}

.form-inputs:focus {
	border-color: #66afe9;
	outline: 0;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px
		rgba(102, 175, 233, .6);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px
		rgba(102, 175, 233, .6)
}

</style>

<body>

 <div class="content">
 
 <h1 class="text-center">Relatório de Reprovação por Turma</h1>
 <hr />
 <br>
 <form action="${pageContext.request.contextPath}/relatorioReprovacaoTurma/relatorioReprovacaoTurma" method="post">
        
        <label for="turma">Turma:</label> 
        <input type="text" id="turma" name="turma" class="form-inputs"/>
        <br><br><br>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Gerar Relatório</button>
 </form>
      <br>
      <hr />
      <br>
   
     <div id="RecallsChart" class="chart"></div><div id="RecallDetails"></div>
	 
 </div>

</body>
 
<script type="text/javascript">
var data = ${data};

if(data != ""){
	
	 var turma = "${turma}";
		
	 var svg = dimple.newSvg("#RecallsChart", 590, 420);
	
	 var myChart = new dimple.chart(svg, data);
	 myChart.setBounds(65, 30, 505, 330)
	 
	 var x =  myChart.addCategoryAxis("x", "Periodo Letivo");
	 myChart.addMeasureAxis("y", "Alunos");
	 
	 var mySeries = myChart.addSeries(["Periodo Letivo", "Alunos", "Lista de Alunos"] , dimple.plot.line); 
	 
	 mySeries.getTooltipText = function(e) {
		 var html = '';
		 html += '<span class="marker"> Período Letivo: ' + e.aggField[0]+'</span>';
		 html += '<br/> <br/>';
		
		 var stringJson = e.aggField[2].toString();
		 var array = stringJson.split(",");			 
		 
		 html += '<strong>Lista de Alunos</strong>';
		 html += '<ul align="left">';
		 for(var i = 0; i < array.length; i++){
			 html += "<li>" + array[i] + "</li>";
		 }
		 html += '</ul>';
		 
		 $("#RecallDetails").html(html);
		 
		 return [
		         	"Turma: " + turma,
		        	"Periodo Letivo: " + e.aggField[0],
		        	"Quantidade de Alunos: " + e.aggField[1]
		        ];
	 }
	 
	 myChart.draw();
}
</script>
</html>