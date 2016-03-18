<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta content="charset=UTF-8">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/vendor/bootstrap.min.css">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/style.css">
		<script src='//assets.codepen.io/assets/common/stopExecutionOnTimeout.js?t=1'></script>
		<script src='//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
		<script src='http://d3js.org/d3.v3.min.js'></script>
		<script src="http://dimplejs.org/dist/dimple.v2.1.6.min.js"></script>	
   <title>Relatório de Evasão</title>
</head>

<style>

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
 
 <h1 class="text-center">Relatório de Evasão</h1>
 <hr />
 <br>
 <form action="${pageContext.request.contextPath}/relatorioEvasao/relatorioEvasao" method="post">
        
        <label for="curso">Curso:</label> 
        <select id="curso" name="curso" class="form-inputs">
         <option value="BCC">BCC</option>
         <option value="WEB">WEB</option>
        </select>
        <label for="periodo">Período Letivo:</label> <input type="text" id="periodo" placeholder="Ex: 2013/1" name="periodoLetivo" class="form-inputs">
        <br><br><br>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Gerar Relatório</button>
 
 </form>
      <br>
      <hr />
      <br>
      
      <div id="chart" class="chart"></div>
     
 </div>

</body>
 
<script type="text/javascript">

var data = ${data};

if(data != ""){
	
	 var curso = "${curso}";
		
	 var svg = dimple.newSvg("#chart", 590, 400);
	
	 var myChart = new dimple.chart(svg, data);
	 myChart.setBounds(65, 30, 505, 330)
	 myChart.addCategoryAxis("x", "Periodo Letivo");
	 myChart.addMeasureAxis("y", "Alunos");
	 myChart.addSeries("Info. " + curso, dimple.plot.bubble);
	 myChart.addLegend(70, 10, 510, 20, "right");
	 myChart.draw();

}
</script>
</html>