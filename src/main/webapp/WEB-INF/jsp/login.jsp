<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>

<html ng-app = "loginApp">
<head>
	<title>QS 2.0</title>
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-store" />
	<meta http-equiv="cache-control" content="max-age=0" />
	<meta http-equiv="expires" content="0" />
	<meta charset="utf-8">
	<META HTTP-EQUIV="X-UA-Compatible" CONTENT="IE=edge" />
	<script>
		window.onerror = function(errorMsg, url, lineNumber) {
			console.log('Line:' + lineNumber + ' message:' + errorMsg);
// 			window.location = 'login.htm';
		}
	</script>
	<script src="plugins/jquery/jquery-1.9.1.min.js"></script>
	<script src="plugins/angularjs/1.4.9/angular.js"></script>	
	<script src="plugins/angular-block-ui/v0.2/js/angular-block-ui.js"></script>

	<style type="text/css">
	body {
		padding: 0 0 5em 0 ;
		background-image: -moz-linear-gradient(top, rgba(0,0,0,0.5), rgba(0,0,0,0.5)), url("resources/images/banner.jpg");
		background-image: -webkit-linear-gradient(top, rgba(0,0,0,0.5), rgba(0,0,0,0.5)), url("resources/images/banner.jpg");
		background-image: -ms-linear-gradient(top, rgba(0,0,0,0.5), rgba(0,0,0,0.5)), url("resources/images/banner.jpg");
		background-image: linear-gradient(top, rgba(0,0,0,0.5), rgba(0,0,0,0.5)), url("resources/images/banner.jpg");
		background-position: fixed;
		background-repeat: no-repeat;
		background-size: cover;
		/* background-image: url("resources/images/banner.jpg");
		background-position: absolute;
		background-repeat: no-repeat;
		background-size: cover; */
	}
	</style>
<%
request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
%>
	<script type="text/javascript">
	var loginApp = angular.module('loginApp', ['blockUI'])
	.config(function(blockUIConfig) {		
		blockUIConfig.autoBlock = true;
		blockUIConfig.delay = 100;
		blockUIConfig.autoInjectBodyBlock = true;
	})
	.controller('loginCtrl', 
				['$scope', '$http', '$window', 
		function($scope, $http, $window){
		$scope.user = {};
		$scope.userIcon = 'resources/images/profile.png';
		$scope.imageServerAddress = 'http://gammon/PeopleDirectory_Picture/';
		$scope.getCurrentUser = function(){
			if($window.location.toString().substr('ValidateCurrentSessionFailed') > 0){
				$http.get('service/security/getCurrentUser')
				.then(function(response){
					if(angular.isObject(response.data)){
						$scope.user = response.data;
						$scope.loggedUsername = $scope.user.username;
						$scope.logged = true;
						$scope.userIcon = $scope.imageServerAddress+$scope.user.StaffID+'.jpg';
						angular.element('#password').focus();
					} 
				});
			} 
			angular.element('#username').focus();
		};
		$scope.getCurrentUser();
		$scope.unifyCharacters = function(){
			if($scope.logged){
				$scope.user.username = $scope.loggedUsername.toLowerCase();
			}
			$http({
				method : 'POST',
				url : 'formlogin',
	            data: "username="+$scope.user.username+"&password="+$scope.user.password,
	            headers : {
	            	 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
				}
	    	}).then(function(response){
				if(response.headers('login') === 'Sucess') {
					$window.location = 'home.html';
				} else {
					$scope.loginError = true;
				}
		    });
		};
		
		if(new String(window.location).indexOf("error=true")>=0){
			$scope.loginError = true;
		}
	}]);
	
	
	document.execCommand("ClearAuthenticationCache"); 
	if(new String(window.location).indexOf("/login.htm")<0){
		window.location = "login.htm";
	};
	</script>
	<link rel="icon" type="image/gif" href="resources/images/gammon.gif" sizes="128x128"/>
	<!-- Bootstrap 3.3.5 -->
	<link rel="stylesheet" href="plugins/bootstrap/3.3.6/css/bootstrap.min.css">
	<!-- Theme style -->
	<link rel="stylesheet" href="css/adminLTE.css">
	<link rel="stylesheet" href="plugins/angular-block-ui/v0.2/css/angular-block-ui.min.css"/>
	
</head>
<body ng-controller="loginCtrl">
	<br>
	<img alt="Brand" ng-src="resources/images/gammon.png" style="width: 128px"/>
	<div class="login-box">
		<div class="login-logo">
			<font color="white"><b>QS 2.0</b></font>
		</div>
		<!-- /.login-logo -->
		<div class="login-box-body">
			<div align="center">
				<img class="img-circle" id="img_logo" data-ng-src="{{userIcon}}" style="width: 128px; height: 128px">
			</div>
			<br>
			<form ng-submit="unifyCharacters()">
				<div class="form-group has-feedback">
					<input id="username" type="text" name="username" class="form-control" ng-model="user.username" ng-readonly="logged"> 
					<span class="glyphicon glyphicon-user form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="password" id="password" name="password" class="form-control" ng-model="user.password"> 
					<span class="glyphicon glyphicon-lock form-control-feedback"></span>
				</div>
				<div class="row">
					<div class="col-md-12">
						<button type="submit" class="btn btn-info btn-block">Sign In</button>
					</div>
				</div>
					<div align="center" ng-if="loginError">
						<p class="text-red">Login Failed.</p> 
						<p class="text-red">Bad credentials.</p>
					</div>
			</form>
			<br>
			<div align="center">
				<a href="http://gammon/BMS/Project%20Delivery/PDS%2018%20IMS%20Final%20Documents/PDS_18%20Form%201%20User%20Account%20Administration%20Form.pdf" class="text-center">Create account</a> or <a href="https://eportal.gammonconstruction.com/WindowsAccountSelfService/">Forgot
					password</a>
			</div>
		</div>
	</div>
</body>
</html>