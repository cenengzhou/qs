<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="sun.misc.BASE64Encoder" %>

<html>
<head>
<title>Gammon ERP - QS Modules</title>
	<META HTTP-EQUIV="X-UA-Compatible" CONTENT="IE=EmulateIE8" />
<style>
div.center {
	background-color:#ffffff;
	border-width:1px;
	border-style:solid;
	border-color:#666666;
	width:800px;
	height:400px;
	margin-left:auto;
	margin-right:auto;
	margin-top: auto;
	margin-bottom: auto;
}
h4.title {
	color:#333366;
	text-decoration: italic;
	font-family: Verdana, Arial;
	text-align: right;
	line-height: 10%;
}
p.description {
	color:#333366;
	font-size: 10px;
	font-family: Verdana, Arial;
}
p.versionDate {
	color:#333366;
	font-size: 8px;
	font-family: Verdana, Arial;
}
span.error {
	color:#ff0000;
	font-size: 10px;
	font-family: Verdana, Arial;
}
span.label {
	font-size: 13px;
	color:#333366;
	font-weight: bold;
	font-family: Verdana, Arial;
	text-align: right;
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
//out.println("//domain: " + domainString);
//out.println("//username: " + usernameString);
//out.println("//disableTextBox:" + disableTextBox);
%>
function unifyCharacters(){
	window.document.getElementById("username").value = <%= getElementString%>.toLowerCase();
}
document.execCommand("ClearAuthenticationCache"); 
</script>

<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/styles/apple/main.css" />
</head>
<body class="login">
<div class="center">
<table style="margin-left: auto; margin-right:auto;margin-top:8%;border-collapse:collapse;border-spacing:0px;" height="200" width="600">
	<tr>
		<td valign="top" style="text-align:right;border-width:0px 1px 0px 0px;border-style:dotted;border-color:#666666;padding-top:10px;padding-right:30px;">
			<img src="<c:url value="/resources/images/gammon_logo.gif"/>"/>
			<br/>
			<h4 class="title">Gammon ERP - QS Modules</h4>
			<p class="description">[<b>${jdeEnvironment}</b>|<b>${serverEnvironment}</b>]
			<p class="description">To begin, please enter your Windows username and password.<br/><br/></p>
			<br><br>
			<p class="versionDate">${versionDescription}</p>
			
		</td>
		<td valign="top" style="padding-left:30px;padding-top:10px;">
			<form method="POST" action="<c:url value="/formlogin"/>" onsubmit="unifyCharacters()">
				<c:if test="${not empty param.error }">
					<span class="error">Login Failed. <br/>
					${sessionScope["A_SPRING_SECURITY_LAST_EXCEPTION"].message}
					</span>
				</c:if>
				<table>
					<tr>
						<td><span class="label">Username</span></td>
					</tr>
					<tr>
						<td><input id="username" type="text" name="username" class="g-input-form-text" <% if(disableTextBox){ out.print("readonly"); out.print(" value=\""+usernameString+"\"");}%>/></td>
					</tr>
					<tr>
						<td><span class="label">Password</span></td>
					</tr>
					<tr>
						<td><input type="password" id="password" name="password" class="g-input-form-text"/></td>
					</tr>
					<tr>
						<td align="right">
							<input type="submit" value="Login"/>
							<input type="reset" value="Reset"/>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
</table>
</div>

<script language="javascript">
window.document.getElementById('<% if(disableTextBox){out.print("password");} else {out.print("username");}%>').focus();
</script>

</body>
</html>