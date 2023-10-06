<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url value="/login.htm" var="loginUrl">
	<c:param name="error" value="true"/>
</c:url>
<%
	request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
	request.getSession().setAttribute("A_SPRING_SECURITY_LAST_EXCEPTION", new Exception("Bad credentials."));
	
	System.out.println((String)pageContext.getAttribute("loginUrl"));
	response.sendRedirect((String)pageContext.getAttribute("loginUrl"));
%>
