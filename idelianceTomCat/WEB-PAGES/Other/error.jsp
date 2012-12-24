<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Ideliance | Error</title>
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
		<div class="span12">
			<div class="page-header">
				<h1>
					Ideliance
					<small><a href="<c:out value="${sessionScope.root}" />">Back to home</a></small>
				</h1>
			</div>

			<div class="alert alert-error">
				<h4 class="alert-heading">ERROR</h4>
				<%=request.getAttribute("error") %>
			</div>
		</div>
	</div>
	
</body>
</html>