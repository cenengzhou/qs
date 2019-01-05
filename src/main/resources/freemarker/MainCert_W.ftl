<html>
<head>
<meta content="text/html; charset=UTF-8" http-equiv="content-type">
<meta name="Template" content="MainCert_W">
<title>Main Contract Certificate</title>
<style type="text/css">
table.simple {
	border: 1px solid black;
	padding: 10px;
	font-family: arial;
	font-size: 10pt;
	width: 602px;
}

tr.first {
	background-color: #ffffff;
	height: 25px;
}

tr.second {
	background-color: #ccffcc;
	height: 25px;
}

td.cell02 {
	border-bottom: 1px solid black;
}

td.cell03 {
	font-weight: bold;
}
</style>
</head>
<body>
	<table class="simple" style="text-align: left;" cellpadding="2"
		cellspacing="0">
		<tbody>
			<tr class="first">
				<td class="cell03" style="width: 160px;">Job Number:</td>
				<td style="width: 120px;">${job.jobNumber}</td>
				<td style="width: 300px;">${job.description}</td>
			</tr>
			<tr class="second">
				<td class="cell03" style="width: 160px;">Client Number:</td>
				<td style="width: 120px;">${job.employer}</td>
				<td style="width: 300px;">${clientAddressBook.addressBookName}</td>
			</tr>
			<tr class="first">
				<td class="cell03">Certificate No.:</td>
				<td>${mainCert.certificateNumber!""}</td>
				<td></td>
			</tr>
			<tr class="second">
				<td class="cell03">Client Certificate No.:</td>
				<td>${mainCert.clientCertNo!""}</td>
				<td></td>
			</tr>
			<tr class="first">
				<td class="cell03">Currency:</td>
				<td>${currency!""}</td>
				<td></td>
			</tr>
		</tbody>
	</table>
	<br>
	<table class="simple" style="text-align: left;" cellpadding="2" cellspacing="0">
		<tbody>
			<tr class="first">
				<td style="width: 300px;"></td>
				<td class="cell03" style="text-align: right; text-decoration: underline; width: 200px;">Certificate Movement</td>
				<td style="width: 20px;"></td>
				<td class="cell03" style="text-align: right; text-decoration: underline; width: 200px;">Total</td>
			</tr>
			<tr class="second">
				<td class="cell03">Net Amount</td>
				<td class="cell03" style="text-align: right;">${((mainCert.calculateCertifiedNetAmount()!0) - (previousCert.calculateCertifiedNetAmount()!0))?string["#,##0.00"]}</td>
				<td></td>
				<td class="cell03" style="text-align: right;">${(mainCert.calculateCertifiedNetAmount()!0)?string["#,##0.00"]}</td>
			</tr>
		</tbody>
	</table>
	<br>
	<table class="simple" style="text-align: left;" cellpadding="2"
		cellspacing="0">
		<tbody>
			<tr class="first">
				<td class="cell03" style="width: 100px;">IPA Date:</td>
				<td style="width: 120px;">${(mainCert.ipaSubmissionDate?string["dd.MM.yyyy"])!""}</td>
			</tr>
			<tr class="second">
				<td class="cell03">IPA Sent out Date:</td>
				<td>${(mainCert.ipaSentoutDate?string["dd.MM.yyyy"])!""}</td>
			</tr>
			<tr class="first">
				<td class="cell03">Certificate Date:</td>
				<td>${(mainCert.certIssueDate?string["dd.MM.yyyy"])!""}</td>
			</tr>
			<tr class="second">
				<td class="cell03">Certificate As At Date:</td>
				<td>${(mainCert.certAsAtDate?string["dd.MM.yyyy"])!""}</td>
			</tr>
			<tr class="first">
				<td class="cell03">Certificate Due Date:</td>
				<td>${(mainCert.certDueDate?string["dd.MM.yyyy"])!""}</td>
			</tr>
		</tbody>
	</table>
	<br>

	<table class="simple" style="text-align: left;" cellpadding="2"
		cellspacing="0">
		<tbody>
			<tr class="first">
				<td class="cell03">Remark:</td>
				<td>${mainCert.remark!""}</td>
			</tr>
		</tbody>
	</table>

	<br>
	<br>
</body>
</html>