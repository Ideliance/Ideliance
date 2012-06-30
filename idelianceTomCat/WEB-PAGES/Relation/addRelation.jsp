<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
	<title><c:out value="${requestScope.title}" /></title>
</head>

<c:set var="lang" value="${sessionScope.lang}" scope="page" />
<c:set var="relation" value="${sessionScope.relation}" scope="page" />

<div class="page-header">
	<h1><c:out value="${requestScope.title}" /></h1>
</div>

<div class="row-fluid">
	<div class="span9">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />">Home</a> <span class="divider">/</span></li>
			<li><a href="<c:out value="${sessionScope.root}" />relation/list">Liste des relations</a> <span class="divider">/</span></li>
			<li class="active"><c:out value="${requestScope.title}" /></li>
		</ul>
	</div>
</div>

<form class="form-horizontal" action="" method="post">
	<div class="control-group">
		<label class="control-label">Intitul�</label>
		<div class="controls">
			<input class="input-xlarge focused" type="text" name="entitled1" value="<c:out value="${relation.getEntitled(lang)}" />" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Intitul� inverse</label>
		<div class="controls">
			<input class="input-xlarge focused" type="text" name="entitled2" value="<c:out value="${relation.inverse.getEntitled(lang)}" />" />
		</div>
	</div>
	
	<div class="form-actions">
		<c:choose>
			<c:when test="${empty paramValues['id']}">
		<button type="submit" name="submit" class="btn btn-primary">Cr�er</button>
			</c:when>
			<c:otherwise>
		<input type="hidden" name="id" value="<c:out value="${relation.id}" />" />
		<button type="submit" name="submit" class="btn btn-primary">Modifier</button>
			</c:otherwise>
		</c:choose>
	</div>
</form>