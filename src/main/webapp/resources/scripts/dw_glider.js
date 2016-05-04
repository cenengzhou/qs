/*
		dw_glider.js - requires dw_lib.js
		glide to maintain window location on scroll
		version date: September 2003 

		This code is from Dynamic Web Coding at www.dyn-web.com
    See Terms of Use at http://www.dyn-web.com/bus/terms.html
    Permission granted to use this code 
    as long as this entire notice is included.	
    
    Resources: ypChaser by Aaron Boodman (www.youngpup.net)
    DHTML chaser tutorial at DHTML Lab - www.webreference.com/dhtml		
*/

Glider.holder = [];
function Glider(id,x,y,w,h,d,ac) {
	this.glideDur = d || 1000; this.origX = x; this.origY = y; this.ac = -ac || 0;
	this.baseObj = dynObj;
	this.baseObj(id,x,y,w,h);
  Glider.holder[Glider.holder.length] = this;
  if (!Glider.winHt) Glider.winHt = getWinHeight();
}
Glider.prototype = new dynObj;
Glider.prototype.onGlideInit = function () {}

Glider.prototype.checkGlider = function() {
	var destY = getScrollY() + this.origY;
	if (destY != this.y) {
		if (destY != this.dy) {
			this.dy = destY;
			this.glideInit();
      this.onGlideInit();
		} 
		this.glide();
	}
}

Glider.prototype.glideInit = function() {
	this.gt = new Date().getTime();
	var distY = this.dy - this.y;
	if ( Math.abs(distY) > Glider.winHt ) {	// distance greater than window height?
		this.gsy = (distY > 0)? this.dy - Glider.winHt: this.dy + Glider.winHt;
	} else this.gsy = this.y;
  this.g_yc1 = this.gsy + ( (1+this.ac) * (this.dy - this.gsy)/3 );
	this.g_yc2 = this.gsy + ( (2+this.ac) * (this.dy - this.gsy)/3 );
}

Glider.prototype.glide = function() {
	var elapsed = new Date().getTime() - this.gt;
  if (elapsed < this.glideDur) {
    var y = dw_Bezier.getValue( elapsed/this.glideDur, this.gsy, this.dy, this.g_yc1, this.g_yc2 );
    this.shiftTo(null,y);
  } else this.shiftTo(null,this.dy);
}

Glider.control = function() {
  for (var i=0; Glider.holder[i]; i++) {
    var curObj = Glider.holder[i];
    if (curObj) curObj.checkGlider();
  }
}
//Glider.timer = setInterval("Glider.control()",20);
dw_Animation.add(Glider.control);

// returns height of window
function getWinHeight() {
	var winHt = 0;
	if (window.innerHeight) winHt = window.innerHeight-18;
	else if (document.documentElement && document.documentElement.clientHeight) 
		winHt = document.documentElement.clientHeight;
	else if (document.body && document.body.clientHeight) 
		winHt = document.body.clientHeight;
	return winHt;
}	

// returns amount of vertical scroll
function getScrollY() {
	var sy = 0;
	if (document.documentElement && document.documentElement.scrollTop)
		sy = document.documentElement.scrollTop;
	else if (document.body && document.body.scrollTop) 
		sy = document.body.scrollTop; 
	else if (window.pageYOffset)
		sy = window.pageYOffset;
	else if (window.scrollY)
		sy = window.scrollY;
	return sy;
}

// onresize, get window height
if (window.addEventListener)
  window.addEventListener("resize", function(){ Glider.winHt = getWinHeight(); }, "false");
else if (window.attachEvent)
  window.attachEvent("onresize", function(){ Glider.winHt = getWinHeight(); } );