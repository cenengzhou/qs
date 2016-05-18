<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="sun.misc.BASE64Encoder" %>

<html>
<head>
<title>Gammon ERP - QS Modules</title>
	<META HTTP-EQUIV="X-UA-Compatible" CONTENT="IE=EmulateIE8" />
	<link rel="icon" type="image/gif" href="image/gammon.gif" sizes="128x128"/>
	<!-- Bootstrap 3.3.5 -->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/bootstrap/3.3.6/css/bootstrap.min.css">
	<!-- Theme style -->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/adminLte/2.3.0/css/AdminLTE.min.css">


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
String domainString="";
String usernameString="";
String getElementString="document.getElementById(\"username\").value";
boolean disableTextBox=false;
//out.println("//"+request.getServerName());
//out.println("//"+request.getAttribute("javax.servlet.forward.servlet_path"));
//out.println("//bypass NTLM check:"+request.getAttribute("bypassNTLMCheck"));
if(!"true".equals(request.getAttribute("bypassNTLMCheck"))){
//out.println("//check:"+(!"true".equals(request.getAttribute("bypassNTLMCheck"))));
String auth = request.getHeader("Authorization");
String s = "";
//out.println("//auth:"+auth);
if (auth == null || auth.startsWith("Negotiate")) {
	response.setStatus(response.SC_UNAUTHORIZED);
	response.setHeader("WWW-Authenticate", "NTLM");
	return;
}

if (auth.startsWith("NTLM ")) { 
	byte[] msg = new sun.misc.BASE64Decoder().decodeBuffer(auth.substring(5));
	int off = 0, length, offset;
	if (msg[8] == 1) { 
		off = 18;		
		byte z = 0;
		byte[] msg1 = {
			(byte)'N', (byte)'T', (byte)'L', (byte)'M', (byte)'S',(byte)'S', (byte)'P', z,
			(byte)2, z, z, z, z, z, z, z,
			(byte)40, z, z, z, (byte)1, (byte)130, z, z,
			z, (byte)2, (byte)2, (byte)2, z, z, z, z, 
			z, z, z, z, z, z, z, z
		};
		response.setStatus(response.SC_UNAUTHORIZED);
		response.setHeader("WWW-Authenticate", "NTLM " + new sun.misc.BASE64Encoder().encodeBuffer(msg1).trim());
		return;
	} else if (msg[8] == 3) {
		off = 30;
		//domain
		length = msg[off+1]*256 + msg[off];
		offset = msg[off+3]*256 + msg[off+2];
		s = new String(msg, offset, length);
		domainString = "";
		for(int i=0; i<s.length(); i+=2){
			domainString += s.charAt(i);
		}
		//username
		length = msg[off+9]*256 + msg[off+8];
		offset = msg[off+11]*256 + msg[off+10];
		s = new String(msg, offset, length);
		usernameString = "";
		for(int i=0; i<s.length(); i+=2){
			usernameString += s.charAt(i);
		}
	} else {
		return;
	}
}
}
if(domainString.equals("GAMSKA")){
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
				<c:if test="${not empty param.error }">
					<span class="text">Login Failed. <br/>
					${sessionScope["A_SPRING_SECURITY_LAST_EXCEPTION"].message}
					</span>
				</c:if>
				<div class="form-group has-feedback">
					<input id="username" type="text" name="username" class="form-control" <% if(disableTextBox){ out.print("readonly"); out.print(" value=\""+usernameString+"\"");}%> 
					<span class="glyphicon glyphicon-user form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="password" id="password" name="password" class="form-control"> 
					<span class="glyphicon glyphicon-lock form-control-feedback"></span>
				</div>
				<div class="row">
					<div class="col-md-12">
						<button type="submit" class="btn btn-info btn-block">Sign In</button>
					</div>
				</div>
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