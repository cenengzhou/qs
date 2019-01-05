<html>
<head>
<meta content="text/html; charset=UTF-8" http-equiv="content-type">
<title>Main Contract Certificate</title>
<style type="text/css">
table.simple {
	border: 1px solid black;
	padding: 5px;
	font-family: arial;
	font-size: 8pt;
	width: 472px;
}

tr.first {
	background-color: #ffffff;
	height: 25px;
}

tr.second {
	background-color: #ccffcc;
	height: 25px;
}

tr.separater {
	height: 25px;
}

td.T1C1cell01 {
	width: 150px;
	font-weight: bold;
}

td.T1C2cell01 {
	width: 100px;
}

td.T1C3cell01 {
	width: 210px;
}

td.T2C1cell01 {
	width: 250px;
	font-weight: bold;
}

td.T2C2cell01 {
	width: 210px;
	text-align: right;
}

td.T2C2cell02 {
	width: 210px;
	text-align: right;
	border-bottom: 1px solid black;
}

td.T2C2cell03 {
	width: 210px;
	text-align: left;
}

td.T3C1cell01 {
	width: 230px;
	font-weight: bold;
}

td.T3C2cell01 {
	width: 110px;
	text-align: right;
}

td.T3C3cell01 {
	width: 120px;
}
</style>
</head>
<body>
	<br>
	<TABLE border=0>
		<TBODY>
			<TR class="separater">
				<TD>&nbsp;&nbsp;</TD>
			</TR>
		</TBODY>
	</TABLE>
	<table class="simple" style="text-align: left;" cellpadding="2"
		cellspacing="0">
		<tbody>
			<tr class="first">
				<td class="T1C1cell01">Job Number:</td>
				<td class="T1C2cell01">${job.jobNumber}</td>
				<td class="T1C3cell01">${job.description}</td>
			</tr>
			<tr class="second">
				<td class="T1C1cell01">Client Number:</td>
				<td class="T1C2cell01">${job.employer}</td>
				<td class="T1C3cell01">${clientAddressBook.addressBookName}</td>
			</tr>
			<tr class="first">
				<td class="T1C1cell01">Certificate No.:</td>
				<td class="T1C2cell01">${mainCert.certificateNumber!""}</td>
				<td></td>
			</tr>
			<tr class="second">
				<td class="T1C1cell01">Client Certificate No.:</td>
				<td class="T1C2cell01">${mainCert.clientCertNo!""}</td>
				<td></td>
			</tr>
		</tbody>
	</table>

	<br>
	<TABLE border=0>
		<TBODY>
			<TR>
				<TD>&nbsp;&nbsp;</TD>
			</TR>
		</TBODY>
	</TABLE>
	<table class="simple" style="text-align: left;" cellpadding="2"
		cellspacing="0">
		<tbody>
			<tr class="first">
				<td class="T2C1cell01"></td>
				<td class="T2C2cell01"
					style="text-decoration: underline; font-weight: bold;">Certificate Movement</td>
			</tr>

			<tr class="second">
				<td class="T2C1cell01">Net Amount</td>
				<td class="T2C2cell01">${((mainCert.calculateCertifiedNetAmount()!0)-(previousCert.calculateCertifiedNetAmount()!0))?string["#,##0"]}</td>
			</tr>


		</tbody>
	</table>

	<br>
	<TABLE border=0>
		<TBODY>
			<TR>
				<TD>&nbsp;&nbsp;</TD>
			</TR>
		</TBODY>
	</TABLE>
	<table class="simple" style="text-align: left;" cellpadding="2"
		cellspacing="0">
		<tbody>
			<tr class="first">
				<td class="T2C1cell01"></td>
				<td class="T2C2cell01"
					style="text-decoration: underline; font-weight: bold;">Certificate Movement</td>
			</tr>
			<tr class="second">
				<td class="T2C1cell01">GST Amount(GST Receivable)</td>
				<td class="T2C2cell01">${((mainCert.gstReceivable!0)-(previousCert.gstReceivable!0))?string["#,##0"]}</td>
			</tr>
			<tr class="first">
				<td class="T2C1cell01">GST for Contra Charge(GST Payable}</td>
				<td class="T2C2cell01">${((mainCert.gstPayable!0)-(previousCert.gstPayable!0))?string["#,##0"]}</td>
			</tr>
		</tbody>
	</table>

	<br>
	<TABLE border=0>
		<TBODY>
			<TR class="separater">
				<TD>&nbsp;&nbsp;</TD>
			</TR>
		</TBODY>
	</TABLE>
	<table class="simple" style="text-align: left;" cellpadding="2"
		cellspacing="0">
		<tbody>
			<tr class="first">
				<td class="T1C1cell01">IPA Date:</td>
				<td class="T1C2cell01">${(mainCert.ipaSubmissionDate?string["dd.MM.yyyy"])!""}</td>
			</tr>
			<tr class="second">
				<td class="T1C1cell01">IPA Sent out Date:</td>
				<td class="T1C2cell01">${(mainCert.ipaSentoutDate?string["dd.MM.yyyy"])!""}</td>
			</tr>
			<tr class="first">
				<td class="T1C1cell01">Certificate Date:</td>
				<td class="T1C2cell01">${(mainCert.certIssueDate?string["dd.MM.yyyy"])!""}</td>
			</tr>
			<tr class="second">
				<td class="T1C1cell01">Certificate As At Date:</td>
				<td class="T1C2cell01">${(mainCert.certAsAtDate?string["dd.MM.yyyy"])!""}</td>
			</tr>
			<tr class="first">
				<td class="T1C1cell01">Certificate Due Date:</td>
				<td class="T1C2cell01">${(mainCert.certDueDate?string["dd.MM.yyyy"])!""}</td>
			</tr>
			<tr class="first">
				<td class="cell03">Currency:</td>
				<td>${currency!""}</td>
				<td></td>
			</tr>
		</tbody>
	</table>

	<br>
	<TABLE border=0>
		<TBODY>
			<TR class="separater">
				<TD>&nbsp;&nbsp;</TD>
			</TR>
		</TBODY>
	</TABLE>
	<table class="simple" style="text-align: left;" cellpadding="2"
		cellspacing="0">
		<tbody>
			<tr class="first">
				<td class="T1C1cell01">Remark:</td>
				<td class="T1C2cell01">${mainCert.remark!""}</td>
			</tr>
		</tbody>
	</table>

	<br>
	<TABLE border=0>
		<TBODY>
			<TR class="separater">
				<TD>&nbsp;&nbsp;</TD>
			</TR>
		</TBODY>
	</TABLE>
</body>
</html>
