<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
	<title>Ajouter un énoncé</title>
</head>

<c:set var="typeSubject" value="<%=ideliance.core.object.type.TripletType.SUBJET.getValue() %>"/>
<c:set var="typeTriplet" value="<%=ideliance.core.object.type.TripletType.TRIPLET.getValue() %>"/>
<c:set var="lang" value="${sessionScope.lang}" scope="page" />
<c:set var="subject" value="${sessionScope.subject}" scope="page" />
<c:set var="listSubject" value="${sessionScope.listSubject}" scope="page" />
<c:set var="listRelation" value="${sessionScope.listRelation}" scope="page" />

<div class="page-header">
	<h1>Ajouter un énoncé</h1>
</div>

<div class="row-fluid">
	<div class="span12">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />">Home</a> <span class="divider">/</span></li>
			<li><a href="<c:out value="${sessionScope.root}" />subject/view?id=<c:out value="${subject.id}" />&amp;type=<c:out value="${typeSubject}" />"><c:out value="${subject.getEntitled(lang)}" /></a> <span class="divider">/</span></li>
			<li class="active">Ajouter un énoncé</li>
		</ul>
	</div>
</div>

<form class="form-horizontal" action="" method="post">
	<div class="control-group">
		<label class="control-label">Sujet</label>
		<div class="controls">
			<input type="text" name="subject" disabled="disabled" value="<c:out value="${subject.getEntitled(lang)}" />" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Relation</label>
		<div class="controls ui-widget">
			<select id="combobox-relation" name="relation">
				<option value=""></option>
				<c:forEach items="${listRelation}" var="relation">
				<option value="<c:out value="${relation.id}" />"><c:out value="${relation.getEntitled(lang)}" /></option>
				</c:forEach>
			</select>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Complément</label>
		<div class="controls ui-widget">
			<select id="combobox-complement" name="complement">
				<option value=""></option>
				<c:forEach items="${listSubject}" var="subject">
				<option value="<c:out value="${subject.id}" />"><c:out value="${subject.getEntitled(lang)}" /></option>
				</c:forEach>
			</select>
		</div>
	</div>
	
	<div class="form-actions">
		<button type="submit" name="submit" class="btn btn-primary">Ajouter</button>
	</div>
</form>