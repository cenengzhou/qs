<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>User Function</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/apple/user_profile.css" />
</head>
<body style="background-image: url(<%=request.getContextPath()%>/resources/images/gammon_logo.gif);background-repeat:no-repeat;background-position:0px 0px">


<table border="0" cellspacing="0" cellpadding="0" class="user-profile-table" width="100%">
	<tr>	
		<td align="left" >&nbsp;</td>		
		<td align="right" class="user-profile-cell">
		<c:if test="${not empty user}">
			Welcome <img border="0" src="<%=request.getContextPath()%>/resources/images/user.png" /><b>${user.username}</b> | 
			[${jdeEnvironment}|${serverEnvironment}] | 
			<a class="user-profile-blue-link" href="#" onClick="window.open('<c:url value="/user/preferences.htm"/>')"><img border="0" src="<%=request.getContextPath()%>/resources/images/preferences.png" />Preferences</a> | 
			<a class="user-profile-blue-link" href="#" onClick="window.open('http://gammon/hk/ims/Pages/QSELearning.aspx')"><img border="0" src="<%=request.getContextPath()%>/resources/images/elearning-logo.png" /></a> | 
			<a class="user-profile-helpdesk-link" href="#" onClick="window.open('http://helpdesk/helpdesk.aspx?System=QS')"><img border="0" src="<%=request.getContextPath()%>/resources/images/help.png" />Helpdesk</a> |
			<a title="Logout" href="#" onclick="location.href='<c:url value="/logout"/>'"><img border="0" src="<%=request.getContextPath()%>/resources/images/exit.png" /></a>
		</c:if>
		</td>
	</tr>
</table>
</body>
</html>