<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Connexion</title>
	<style type="text/css">
	body {
		padding-top: 60px;
	}
	</style>
	<link href="/public/css/bootstrap.css" rel="stylesheet">
	<link href="/public/css/bootstrap-responsive.min.css" rel="stylesheet">
</head>
<body>

	<div class="container">
		<div class="span6 offset3">
			<div class="page-header">
				<h1>Connection</h1>
			</div>
<%
String error = (String)request.getAttribute("error");

if (error != null && error.equals("login")) {
%>
			<div class="alert alert-error">
				<a class="close" data-dismiss="alert">×</a>
				<h4 class="alert-heading">Login failed</h4>
			</div>
<%
}
%>
			<form class="form-horizontal" action="/connection" method="post">
				<div class="control-group">
					<label class="control-label" for="form-login">Login</label>
					<div class="controls">
						<input class="input-xlarge focused" id="form-login" name="login" type="text" />
					</div>
				</div>
	
				<div class="control-group">
					<label class="control-label" for="form-password">Password</label>
					<div class="controls">
						<input class="input-xlarge focused" id="form-password" name="password" type="password" />
					</div>
				</div>
				
				<div class="control-group">
					<div class="controls">
						<label class="checkbox" for="form-auto-connect">
							<input id="form-auto-connect" name="auto_connect" type="checkbox" /> Remember me
						</label>
					</div>
				</div>
	
				<div class="form-actions" style="text-align: center; padding: 15px 0px;">
					<input class="btn btn-primary" type="submit" value="Connection" />
				</div>
			</form>
		</div>
	</div>
</body>
</html>