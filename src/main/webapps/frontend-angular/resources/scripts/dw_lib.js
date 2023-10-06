/*************************************************************************
  This code is from Dynamic Web Coding at www.dyn-web.com
  Copyright 2003-4 by Sharon Paine 
  See Terms of Use at www.dyn-web.com/bus/terms.html
  regarding conditions under which you may use this code.
  This notice must be retained in the code as is!
*************************************************************************/

/*
  dw_lib.js - used with dw_glide.js, dw_glider.js, ...
  version date July 2004 
*/

dynObj.holder = {}; 
// constructor
function dynObj(id,x,y,w,h) {
  var el = dynObj.getElemRef(id);
  if (!el) return;  this.id = id; 
  dynObj.holder[this.id] = this; this.animString = "dynObj.holder." + this.id;
  var px = window.opera? 0: "px";
	this.x = x || 0;	if (x) el.style.left = this.x + px;
	this.y = y || 0;	if (y) el.style.top = this.y + px;
	this.w = w || el.offsetWidth || 0;	this.h = h || el.offsetHeight || 0;
	// if w/h passed, set style width/height
	if (w) el.style.width = w + px; if (h) el.style.height = h + px;
}

dynObj.getElemRef = function(id) { 
  var el = document.getElementById? document.getElementById(id): null;
  return el;
} 

dynObj.getInstance = function(id) {
  var obj = dynObj.holder[id];
  if (!obj) obj = new dynObj(id);
  else if (!obj.el) obj.el = dynObj.getElemRef(id);
  return obj;
}

dynObj.prototype.shiftTo = function(x,y) {
  var el = this.el? this.el: dynObj.getElemRef(this.id)? dynObj.getElemRef(this.id): null;
  if (el) {
    if (x != null) el.style.left = (this.x = x) + "px";
    if (y != null) el.style.top = (this.y = y) + "px";
  }
}

dynObj.prototype.shiftBy = function(x,y) { this.shiftTo(this.x+x, this.y+y); }

dynObj.prototype.show = function() { 
  var el = this.el? this.el: dynObj.getElemRef(this.id)? dynObj.getElemRef(this.id): null;
  if (el) el.style.visibility = "visible"; 
}
dynObj.prototype.hide = function() { 
  var el = this.el? this.el: dynObj.getElemRef(this.id)? dynObj.getElemRef(this.id): null;
  if (el) el.style.visibility = "hidden"; 
}


// for time-based animations
// resources: www.13thparallel.org and www.youngpup.net (accelimation)
var dw_Bezier = {
  B1: function (t) { return t*t*t },
  B2: function (t) { return 3*t*t*(1-t) },
  B3: function (t) { return 3*t*(1-t)*(1-t) },
  B4: function (t) { return (1-t)*(1-t)*(1-t) },
  // returns current value based on percentage of time passed
  getValue: function (percent,startVal,endVal,c1,c2) {
    return endVal * this.B1(percent) + c2 * this.B2(percent) + c1 * this.B3(percent) + startVal * this.B4(percent);
  }
}

// adapted from accelimation.js by Aaron Boodman of www.youngpup.net
dw_Animation = {
  instances: [],
  add: function(fp) {
    this.instances[this.instances.length] = fp;
  	if (this.instances.length == 1) this.timerID = window.setInterval("dw_Animation.control()", 10);
  },
  
  remove: function(fp) {
    for (var i = 0; this.instances[i]; i++) {
  		if (fp == this.instances[i]) {
  			this.instances = this.instances.slice(0,i).concat( this.instances.slice(i+1) );
  			break;
  		}
  	}
  	if (this.instances.length == 0) {
  		window.clearInterval(this.timerID);	this.timerID = null;
  	}
  },
  
  control: function() {
    for (var i = 0; this.instances[i]; i++) {
  		if (typeof this.instances[i] == "function" ) this.instances[i]();
      else eval(this.instances[i]);
    }
  }
}
