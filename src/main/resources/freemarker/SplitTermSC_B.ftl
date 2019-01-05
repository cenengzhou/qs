<html>
<head>
<meta content="text/html; charset=UTF-8" http-equiv="content-type">
<title>SC Addendum</title>
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
	width: 300px;
	font-weight: bold;
}

td.T2C1cell02 {
	width: 300px;
}

td.T2C2cell01 {
	width: 160px;
	font-weight: bold;
	text-align: right;
}

td.T2C2cell02 {
	width: 160px;
	text-align: right;
}

td.T2C2cell03 {
	width: 160px;
	text-align: left;
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
				<td class="T1C2cell01">${jobNumber}</td>
				<td class="T1C3cell01">${jobHeaderInfo.description}</td>
			</tr>
			<tr class="second">
				<td class="T1C1cell01">Subcontract:</td>
				<td class="T1C2cell01">${subcontractNumber}</td>
				<td class="T1C3cell01">${scPackage.description}</td>
			</tr>
			<tr class="first">
				<td class="T1C1cell01">Subcontractor:</td>
				<td class="T1C2cell01">${scPackage.vendorNo}</td>
				<td class="T1C3cell01">${vendorName}</td>
			</tr>
			<tr class="second">
				<td class="T1C1cell01">Current Revised SC Sum:</td>
				<td class="T1C2cell01" style="text-align: right;">${(scPackage.getSubcontractSum()!0)?string["#,##0"]}</td>
				<td class="T1C3cell01"></td>
			</tr>
			<tr class="first">
				<td class="T1C1cell01">New Revised SC Sum:</td>
				<td class="T1C2cell01" style="text-align: right;">${newSCSum?string["#,##0"]}</td>
				<td class="T1C3cell01"></td>
			</tr>
		</tbody>
	</table>
</body>
</html>