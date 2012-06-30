<%@page import="ideliance.core.object.type.TripletType"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://ideliance.fr/tag-lib" prefix="f"%>
<head>
	<title><c:out value="${requestScope.title}" /></title>
</head>

<c:set var="lang" value="${sessionScope.lang}" scope="page" />
<c:set var="subject" value="${requestScope.subject}" scope="page" />
<c:set var="category" value="${requestScope.category}" scope="page" />
<c:set var="selectCategories" value="${requestScope.selectCategories}" scope="page" />
<c:set var="listCategory" value="${requestScope.listCategory}" scope="page" />

<div class="page-header">
	<h1>
		<c:out value="${requestScope.title}" />
		<c:if test="${subject != null}">
			<small><c:out value="${subject.getEntitled(lang)}" /></small>
		</c:if>
	</h1>
</div>

<div class="row-fluid">
	<div class="span12">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />">Home</a> <span class="divider">/</span></li>
			<c:choose>
				<c:when test="${category != null}">
			<li><a href="<c:out value="${sessionScope.root}" />category/view?id=<c:out value="${category.id}" />"><c:out value="${category.getEntitled(lang)}" /></a> <span class="divider">/</span></li>
				</c:when>
				<c:when test="${subject != null}">
			<li><a href="<c:out value="${sessionScope.root}" />subject/view?id=<c:out value="${subject.id}" />&amp;type=<%=TripletType.SUBJET.getValue() %>"><c:out value="${subject.getEntitled(lang)}" /></a> <span class="divider">/</span></li>
				</c:when>
			</c:choose>
			<li class="active"><c:out value="${requestScope.title}" /></li>
		</ul>
	</div>
</div>

<form class="form-horizontal" action="" method="post">
	<div class="control-group">
		<label class="control-label">Intitulé</label>
		<div class="controls">
			<input class="input-xxlarge" type="text" name="entitled" value="<c:out value="${subject.getEntitled(lang)}" />" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Texte</label>
		<div class="controls">
			<textarea class="input-xxlarge" name="text" rows="4"><c:out value="${subject.getFreeText(lang)}" /></textarea>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Catégorie</label>
		<div class="controls">
			<select class="input-xxlarge" name="category[]" multiple="multiple" size="5">
				<c:forEach items="${listCategory}" var="c">
					<c:choose>
						<c:when test="${f:contains(selectCategories, c.SSubject.id) || category.id == c.SSubject.id}">
				<option value="<c:out value="${c.SSubject.id}" />" selected="selected"><c:out value="${c.SSubject.getEntitled(lang)}" /></option>
						</c:when>
						<c:otherwise>
				<option value="<c:out value="${c.SSubject.id}" />"><c:out value="${c.SSubject.getEntitled(lang)}" /></option>
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
		<input type="hidden" name="id" value="<c:out value="${subject.id}" />" />
		<button type="submit" name="submit" class="btn btn-primary">Modifier</button>
			</c:otherwise>
		</c:choose>
	</div>
</form>