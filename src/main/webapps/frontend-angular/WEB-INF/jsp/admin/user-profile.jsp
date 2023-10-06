<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty user}">
	Welcome ${user.username} | 
	<a href="<c:url value="/logout"/>">Logout</a>
</c:if>