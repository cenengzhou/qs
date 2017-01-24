<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<style>
body {
	margin: 15px;
}
</style>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/apple/main.css" />

<h3>User Preferences</h3>

<p>Users may customize the application by modifying the following settings:</p>

<c:if test="${!empty success}">
	<p class="g-success">Preferences saved successfully. You have to refresh the application to apply the changes.</p>
</c:if>

<form:form action="preferences.htm" modelAttribute="generalPreferences" htmlEscape="false" method="post">
	<table class="g-input-table">
		<tr>
			<td>Default Job: </td>
			<td><form:input path="defaultJob" cssClass="g-input-text"/></td>
		</tr>
		<tr>
			<td>Decimal places(Amount):</td>
			<td><form:select path="amountDecimalPlaces" cssClass="g-input-select"><form:options items="${decimalPlacesOptions}"/></form:select></td>
		</tr>
		<tr>
			<td>Decimal places(Rate):</td>
			<td><form:select path="rateDecimalPlaces" cssClass="g-input-select"><form:options items="${decimalPlacesOptions}"/></form:select>
		</tr>
		<tr>
			<td>Decimal places(Quantity):</td>
			<td><form:select path="quantityDecimalPlaces" cssClass="g-input-select"><form:options items="${quantDecimalPlacesOptions}"/></form:select></td>
		</tr>
		<tr>
			<td colspan="2" align="right">
				<input type="submit" value="Save"/>
			</td>
		</tr>
	</table>
</form:form>
		