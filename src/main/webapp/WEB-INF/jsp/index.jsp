<!DOCTYPE html> 
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
	<META HTTP-EQUIV="Expires" CONTENT="-1">
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
	<META HTTP-EQUIV="X-UA-Compatible" CONTENT="IE=EmulateIE8" />
		<style>
		body,td,a,div,.p{font-family:arial,sans-serif}
		div,td{color:#000000}
		a:link,.w,.w a:link{color:#0000cc}
		a:visited{color:#551a8b}
		a:active{color:#ff0000}
		#loading {
		  position: absolute;
		  left: 45%;
		  top: 40%;
		  margin-left: -45px;
		  padding: 2px;
		  z-index: 20001;
		  height: auto;
		  border: 1px solid #ccc;
		}
		#loading a {
		  color: #225588;
		}
		
		#loading .loading-indicator {
		  background: white;
		  color: #444;
		  font: bold 13px tahoma, arial, helvetica;
		  padding: 10px;
		  margin: 0;
		  height: auto;
		}
		
		#loading .loading-indicator img {
		  margin-right:8px;
		  float:left;
		  vertical-align:top;
		}
		
		#loading-msg {
		  font: normal 10px arial, tahoma, sans-serif;
		}
	</style>
	<SCRIPT src="<c:url value="/resources/scripts/jquery-1.12.2.min.js"/>"  type=text/javascript></SCRIPT>
	<SCRIPT src="<c:url value="/resources/scripts/dw_lib.js"/>" type=text/javascript></SCRIPT>
	<SCRIPT src="<c:url value="/resources/scripts/dw_glider.js"/>" type=text/javascript></SCRIPT>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/apple/main.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/styles/apple/user_profile.css" />
	
	  <!-- script src="<c:url value="/gammonqs/js/amathcontext.js"/>" type=text/javascript></SCRIPT -->
      <!-- script src="<c:url value="/gammonqs/js/bigdecimal.js"/>" type=text/javascript></SCRIPT -->
      <script src="<c:url value="/gammonqs/js/ext/adapter/ext/ext-base.js"/>" type=text/javascript></SCRIPT>
      <script src="<c:url value="/gammonqs/js/ext/ext-all.js"/>" type=text/javascript></SCRIPT>
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

	<div id="loading">
	    <div class="loading-indicator">
	    	<img src="gammonqs/images/blue-loading.gif" width="32" height="32"/>:: Gammon QS ::<br />
	    	<span id="loading-msg">Loading ...</span>
	    </div>
	</div>
	
	<script language='javascript' src='gammonqs/gammonqs.nocache.js'></script>
	
	<div id="gwt-main-panel"></div>
	<iframe src="javascript:''" id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>
	<iframe id="__printingFrame" style="width:0;height:0;border:0"></iframe>

	<script type="text/javascript" >
	var screenW = 630;
	
	if (parseInt(navigator.appVersion)>3) {
	 if (navigator.appName=="Netscape") {
	  screenW = window.innerWidth;
	  screenH = window.innerHeight-40;
	 }
	 if (navigator.appName.indexOf("Microsoft")!=-1) {
	  screenW = document.body.offsetWidth;
	  screenH = document.body.offsetHeight-40;
	 }
	}
	
	if (window.innerHeight) screenH = window.innerHeight-40;
			else if (document.documentElement && document.documentElement.clientHeight) 
				screenH = document.documentElement.clientHeight-40;
			else if (document.body && document.body.clientHeight) 
				screenH = document.body.clientHeight-40;
				
				
	//Specify the marquee's width (in pixels)
	var marqueewidth=(parseInt(screenW))+"px"
	//Specify the marquee's height
	var marqueeheight="30px"
	//Specify the marquee's marquee speed (larger is faster 1-10)
	var marqueespeed=2
	//configure background color:
	var marqueebgcolor="#CC0000"
	//Pause marquee onMousever (0=no. 1=yes)?
	var pauseit=1
	
	var msg = '';
	
	//Specify the marquee's content (don't delete <nobr> tag)
	//Keep all content on ONE line, and backslash any single quotations (ie: that\'s great):
	var marqueecontent = '<nobr>&nbsp;</nobr>'
	
	////NO NEED TO EDIT BELOW THIS LINE////////////
	marqueespeed=(document.all)? marqueespeed : Math.max(1, marqueespeed-1) //slow speed down by 1 for NS
	var copyspeed=marqueespeed
	var pausespeed=(pauseit==0)? copyspeed: 0
	var iedom=document.all||document.getElementById
	if (iedom)
		document.write('<span id="temp" style="visibility:hidden;position:absolute;top:-100px;left:-9000px">'+marqueecontent+'</span>')
	var actualwidth=''
	var cross_marquee, ns_marquee
	
	function populate(){
		if (iedom)
		{
			cross_marquee=document.getElementById? document.getElementById("iemarquee") : document.all.iemarquee
			cross_marquee.style.left=parseInt(marqueewidth)+8+"px"
			cross_marquee.style.height=70+"px"
			cross_marquee.style.top=7+"px"			
			cross_marquee.innerHTML=marqueecontent
			actualwidth=document.all? temp.offsetWidth : document.getElementById("temp").offsetWidth
		}
		else if (document.layers)
		{
			ns_marquee=document.ns_marquee.document.ns_marquee2
			ns_marquee.left=parseInt(marqueewidth)+8
			ns_marquee.document.write(marqueecontent)
			ns_marquee.document.close()
			actualwidth=ns_marquee.document.width
		}
		lefttime=setInterval("scrollmarquee()",20)
	}
	window.onload=populate
	
	function scrollmarquee(){
		if (iedom)
		{
			if (parseInt(cross_marquee.style.left)>(actualwidth*(-1)+8))
				cross_marquee.style.left=parseInt(cross_marquee.style.left)-copyspeed+"px"
			else
				cross_marquee.style.left=parseInt(marqueewidth)+8+"px"
		}
		else if (document.layers)
		{
			if (ns_marquee.left>(actualwidth*(-1)+8))
				ns_marquee.left-=copyspeed
			else
				ns_marquee.left=parseInt(marqueewidth)+8
		}
	}
	if (iedom||document.layers){
		with (document){
		document.write('<div id="glideDiv" style="position:absolute; z-index:200;"><table border="0" cellspacing="0" cellpadding="0"><td>')
			if (iedom){
				write('<div id="relativePos" style="position:relative;width:'+marqueewidth+';height:'+marqueeheight+';overflow:hidden">')
				write('<div id="absolutePos" style="position:absolute;width:'+marqueewidth+';height:'+marqueeheight+';background-color:'+marqueebgcolor+'" onMouseover="copyspeed=pausespeed" onMouseout="copyspeed=marqueespeed">')
				write('<div id="iemarquee" style="position:absolute;left:0px;top:0px"></div>')
				write('</div></div>')
			}
			else if (document.layers){
				write('<ilayer width='+marqueewidth+' height='+marqueeheight+' name="ns_marquee" bgColor='+marqueebgcolor+'>')
				write('<layer name="ns_marquee2" left=0 top=0 onMouseover="copyspeed=pausespeed" onMouseout="copyspeed=marqueespeed"></layer>')
				write('</ilayer>')
			}
		document.write('</td></table></div>')
		}
	}
	
	 // args: id, left, top, w, h, duration of glide to location onscroll, acceleration factor
	     // acceleration factor should be -1 to 1. -1 is full deceleration
	     var statLyr = new Glider("glideDiv",0,screenH,null,null,500,0);
	     statLyr.show();
	
	var resizeWindow = function (){
		
	   	if (parseInt(navigator.appVersion)>3) {
	   		if (navigator.appName=="Netscape") {
			  screenW = window.innerWidth;
			  screenH = window.innerHeight-61;
			 }
			 if (navigator.appName.indexOf("Microsoft")!=-1) {
			  screenW = document.body.offsetWidth;
			  screenH = document.body.offsetHeight-61;
			 }
			if (window.innerHeight) screenH = window.innerHeight-61;
			else if (document.documentElement && document.documentElement.clientHeight) 
				screenH = document.documentElement.clientHeight-61;
			else if (document.body && document.body.clientHeight) 
				screenH = document.body.clientHeight-61;
		}
		
		document.getElementById("relativePos").style.width = (parseInt(screenW)-55)+"px";
		document.getElementById("absolutePos").style.width = (parseInt(screenW)-55)+"px";
		marqueewidth=(parseInt(screenW)-55)+"px";
		statLyr = new Glider("glideDiv",0,screenH,null,null,500,0);
	     	statLyr.show();
	}
	
	     window.onresize = resizeWindow;
	
	     if(document.getElementById("glideDiv")!=null)
	     	document.getElementById("glideDiv").style.visibility="hidden";
	
	  if(document.getElementById("relativePos")!=null)
	     	document.getElementById("relativePos").style.visibility="hidden";     
	</script>
	<style>
	/* fix IE8 composite box textfield not align with icon */
	.ext-ie .x-form-text {
		height: 18px;
		line-height: 18px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
		margin-left: 0px;
	}
	.ext-ie TD .x-form-text {
		top: 0px;
		position: relative;
	}
	
	/* fix IE8 DateField show only partial DatePicker */
	.ext-ie .x-menu {
		zoom:1
		overflow: visible;
	}
	</style>
</body>
</html>