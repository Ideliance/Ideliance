<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
	<title>Fusionner une catégorie</title>
</head>

<c:set var="lang" value="${sessionScope.lang}" scope="page" />
<c:set var="category" value="${sessionScope.category}" scope="page" />

<div class="page-header">
	<h1>
		Fusionner une catégorie
		<small><c:out value="${category.getEntitled(lang)}" /></small>
	</h1>
</div>

<div class="row-fluid">
	<div class="span12">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />">Home</a> <span class="divider">/</span></li>
			<li><a href="<c:out value="${sessionScope.root}" />category/view?id=<c:out value="${category.id}" />"><c:out value="${category.getEntitled(lang)}" /></a> <span class="divider">/</span></li>
			<li class="active">Fusionner une catégorie</li>
		</ul>
	</div>
</div>

<form class="form-horizontal" action="" method="post">
	<div class="control-group">
		<label class="control-label">Catégorie</label>
		<div class="controls">
			<select name="category">
				<c:forEach items="${listCategory}" var="c">
					<c:if test="${c.SSubject.id != category.id}">
				<option value="<c:out value="${c.SSubject.id}" />"><c:out value="${c.SSubject.getEntitled(lang)}" /></option>
					</c:if>
				</c:forEach>
			</select>
		</div>
	</div>
	
	<div class="form-actions">
		<input type="hidden" name="id" value="<c:out value="${category.id}" />" />
		<button type="submit" name="submit" class="btn btn-primary">Fusionner</button>
	</div>
</form>