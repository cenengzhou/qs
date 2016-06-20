<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="sun.misc.BASE64Encoder" %>

<html>
<head>
<title>Gammon ERP - QS Modules</title>
	<META HTTP-EQUIV="X-UA-Compatible" CONTENT="IE=EmulateIE8" />
	<link rel="icon" type="image/gif" href="resources/images/gammon.gif" sizes="128x128"/>
	<!-- Bootstrap 3.3.5 -->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/bootstrap/3.3.6/css/bootstrap.min.css">
	<!-- Theme style -->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/adminLTE.css">


<style type="text/css">
body {
	background-image: url("resources/images/background.png");
	background-position: absolute;
	background-repeat: no-repeat;
	background-size: cover;
}
</style>

<script type="text/javascript" language="javascript">
<%
request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
String usernameString = (String) request.getAttribute("username");
String domainString = (String) request.getAttribute("domain");
String getElementString="document.getElementById(\"username\").value";
boolean disableTextBox=false;
String auth = request.getHeader("Authorization");
System.out.println("auth:"+auth);
if (auth == null) {
	response.setStatus(response.SC_UNAUTHORIZED);
	response.addHeader("WWW-Authenticate", "Negotiate");
	return;
}

System.out.println("username:" + usernameString);
System.out.println("domain:" + domainString);
if(domainString != null){
	getElementString = "\""+ usernameString + "\"";
	disableTextBox = true;
} else {
	disableTextBox = false;
}

%>
function unifyCharacters(){
	window.document.getElementById("username").value = <%= getElementString%>.toLowerCase();
}
document.execCommand("ClearAuthenticationCache"); 
if(new String(window.location).indexOf("/login.htm")<0){
	window.location = "login.htm";
};
</script>

</head>
<body>
	<br>
	<img alt="Brand" src="resources/images/gammon.png" style="width: 128px">
	<div class="login-box">
		<div class="login-logo">
			<font color="white"><b>Project Cost Management System</b></font>
		</div>
		<!-- /.login-logo -->
		<div class="login-box-body">
			<div align="center">
				<img class="img-circle" id="img_logo" src="resources/images/profile.png" style="width: 128px">
			</div>
			<br>
			<form method="POST" action="<c:url value="/formlogin"/>" onsubmit="unifyCharacters()">
				<div class="form-group has-feedback">
					<input id="username" type="text" name="username" class="form-control" <% if(disableTextBox){ out.print("readonly"); out.print(" value=\""+usernameString+"\"");}%>> 
					<span class="glyphicon glyphicon-user form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="password" id="password" name="password" class="form-control"> 
					<span class="glyphicon glyphicon-lock form-control-feedback"></span>
				</div>
				<div class="row">
				<%-- <p class="">[<b>${jdeEnvironment}</b>|<b>${serverEnvironment}</b>]
				<p class="">${versionDescription}</p> --%>
					<div class="col-md-12">
						<button type="submit" class="btn btn-info btn-block">Sign In</button>
					</div>
				</div>
				<c:if test="${not empty param.error }">
					<div align="center">
						<p class="text-red">Login Failed.</p> 
						<p class="text-red">${sessionScope["A_SPRING_SECURITY_LAST_EXCEPTION"].message}</p>
					</div>
				</c:if>
				<!-- <div class="col-md-6">
					<button type="reset" class="btn btn-info btn-block">Reset</button>
				</div> -->
			</form>


			<br>
			<div align="center">
				<a href="" class="text-center">Create account</a> or <a href="#">Forgot
					password</a>
			</div>
		</div>
	</div>


<script language="javascript">
window.document.getElementById('<% if(disableTextBox){out.print("password");} else {out.print("username");}%>').focus();
</script>
</body>

</html>