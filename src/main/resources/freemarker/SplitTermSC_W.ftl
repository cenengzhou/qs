<html>
<head>
<meta content="text/html; charset=UTF-8" http-equiv="content-type">
<meta name="Template" content="SplitTermSC_W">
<title>SC Addendum</title>
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
				<td style="width: 120px;">${jobNumber}</td>
				<td style="width: 300px;">${jobHeaderInfo.description}</td>
			</tr>
			<tr class="second">
				<td class="cell03">Subcontract:</td>
				<td>${subcontractNumber}</td>
				<td>${scPackage.description!""}</td>
			</tr>
			<tr class="first">
				<td class="cell03">Subcontractor:</td>
				<td>${scPackage.vendorNo}</td>
				<td>${vendorName}</td>
			</tr>
			<tr class="second">
				<td class="cell03">Current Revised SC Sum:</td>
				<td style="text-align: right;">${(scPackage.getSubcontractSum()!0)?string["#,##0.00"]}</td>
				<td></td>
			</tr>
			<tr class="first">
				<td class="cell03">New Revised SC Sum:</td>
				<td style="text-align: right;">${newSCSum?string["#,##0.00"]}</td>
				<td></td>
			</tr>
		</tbody>
	</table>
</body>
</html>