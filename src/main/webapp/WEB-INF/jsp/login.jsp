<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>

<html ng-app = "loginApp">
<head>
	<title>PCMS - Gammon Project Cost Management System</title>
	<META HTTP-EQUIV="X-UA-Compatible" CONTENT="IE=EmulateIE11" />
	<script>
		window.onerror = function(errorMsg, url, lineNumber) {
			window.location = 'login.htm';
		}
	</script>
	<link rel="icon" type="image/gif" href="resources/images/gammon.gif" sizes="128x128"/>
	<!-- Bootstrap 3.3.5 -->
	<link rel="stylesheet" href="plugins/bootstrap/3.3.6/css/bootstrap.min.css">
	<!-- Theme style -->
	<link rel="stylesheet" href="css/adminLTE.css">
	<!-- jQuery 2.1.4 -->
	<script src="plugins/jquery/jquery-1.9.1.min.js"></script>
	<!-- angularJS 1.4.9 -->
	<script src="plugins/angularjs/1.4.9/angular.js"></script>	

	<style type="text/css">
	body {
		background-image: url("resources/images/background.png");
		background-position: absolute;
		background-repeat: no-repeat;
		background-size: cover;
	}
	</style>
<%
request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
%>
	<script type="text/javascript">
	var loginApp = angular.module('loginApp', []);
	loginApp.controller('loginCtrl', ['$scope', '$http', '$window', function($scope, $http, $window){
		$scope.user = {};
		$scope.userIcon = 'resources/images/profile.png';
		$scope.imageServerAddress = 'http://gammon/PeopleDirectory_Picture/';
		$http.get('service/security/getCurrentUser')
		.then(function(response){
			if(angular.isObject(response.data)){
				$scope.user = response.data;
				$scope.loggedUsername = $scope.user.username;
				$scope.logged = true;
				$scope.userIcon = $scope.imageServerAddress+$scope.user.StaffID+'.jpg';
				angular.element('#password').focus();
			} else {
				angular.element('#username').focus();
			}
		});
		
		$scope.unifyCharacters = function(){
			if($scope.logged){
				$scope.user.username = $scope.loggedUsername.toLowerCase();
			}
			$http({
				method : 'POST',
				url : 'formlogin',
	            params: {
	            	username: $scope.user.username,
	            	password: $scope.user.password
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
</head>
<body ng-controller="loginCtrl">
	<br>
	<img alt="Brand" src="resources/images/gammon.png" style="width: 128px">
	<div class="login-box">
		<div class="login-logo">
			<font color="white"><b>Project Cost Management System</b></font>
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