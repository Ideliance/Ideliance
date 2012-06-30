<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
	<title><c:out value="${requestScope.title}" /></title>
</head>

<c:set var="user" value="${sessionScope.user}" scope="page" />
<c:set var="listLevel" value="${sessionScope.listLevel}" scope="page" />

<div class="page-header">
	<h1><c:out value="${requestScope.title}" /></h1>
</div>

<div class="row">
	<div class="span9">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />administration/">Home</a> <span class="divider">/</span></li>
			<li><a href="<c:out value="${sessionScope.root}" />administration/user/">User List</a> <span class="divider">/</span></li>
			<li class="active"><c:out value="${requestScope.title}" /></li>
		</ul>
	</div>
</div>

<form class="form-horizontal" action="" method="post">
	<div class="control-group">
		<label class="control-label" for="form-login">Login</label>
		<div class="controls">
			<input class="input-xlarge focused" id="form-login" type="text" name="new-login" value="<c:out value="${user.login}" />" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="form-password">Password</label>
		<div class="controls">
			<input class="input-xlarge focused" id="form-password" type="text" name="new-password" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="form-level">Level</label>
		<div class="controls">
			<select name="new-level" id="form-level">
			<c:forEach items="${listLevel}" var="level">
				<c:choose>
					<c:when test="${user.level == level}">
				<option value="<c:out value="${level.getValue()}" />" selected="selected"><c:out value="${level.getValue()}" /></option>
					</c:when>
					<c:otherwise>
				<option value="<c:out value="${level.getValue()}" />"><c:out value="${level.getValue()}" /></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			</select>
		</div>
	</div>
	
	<div class="form-actions">
		<c:choose>
			<c:when test="${empty paramValues['id']}">
		<button type="submit" name="submit" class="btn btn-primary">Créer</button>
			</c:when>
			<c:otherwise>
		<input type="hidden" name="id" value="<c:out value="${user.id}" />" />
		<button type="submit" name="submit" class="btn btn-primary">Modifier</button>
			</c:otherwise>
		</c:choose>
	</div>
</form>