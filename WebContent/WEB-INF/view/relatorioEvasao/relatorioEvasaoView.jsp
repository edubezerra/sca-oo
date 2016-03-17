<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html>
<head>
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

.chart{

    margin: 0 auto;

}

</style>

<body>
    
    <div class="content">

    <h1 class="text-center">Relatório de Evasão</h1>
    <hr />
    <br>
    <c:if test="">
	<div id="chart" class="chart"></div>
	</c:if>	
	
	</div>
</body>

<script type="text/javascript">

var data = [{"Periodo Letivo":"2013/1","Alunos":16},{"Periodo Letivo":"2013/2","Alunos":16},{"Periodo Letivo":"2014/1","Alunos":16},{"Periodo Letivo":"2014/2","Alunos":15},{"Periodo Letivo":"2015/1","Alunos":15},{"Periodo Letivo":"2015/2","Alunos":13},{"Periodo Letivo":"2016/1","Alunos":0}];

var svg = dimple.newSvg("#chart", 590, 400);

var myChart = new dimple.chart(svg, data);
myChart.setBounds(65, 30, 505, 330)
myChart.addCategoryAxis("x", "Periodo Letivo");
myChart.addMeasureAxis("y", "Alunos");
myChart.addSeries("Info", dimple.plot.bubble);
myChart.addLegend(70, 10, 510, 20, "right");
myChart.draw();  

</script>

</html>