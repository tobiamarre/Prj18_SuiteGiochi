<%@page import="controller.LanciaDadi"%>
<%
LanciaDadi.play();

	int faccia1 = LanciaDadi.result1;
	int faccia2 = LanciaDadi.result2;
	
	String img1 = "img/f"+ faccia1 +".jpg";
	String img2 = "img/f"+ faccia2 +".jpg";
%>


<div id="dadi">
	<img src="<%=img1%>" alt="">
	<img src="<%=img2%>" alt="">

</div>


<div id="play">

<button onClick="window.location.href=window.location.href">Refresh Page</button>

<h2><%=LanciaDadi.risultato%></h2>
<h2>Vittorie: <%=LanciaDadi.vittorie%></h2>

</div>