<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
	<title><c:out value="${requestScope.title}" /></title>
</head>

<c:set var="typeSubject" value="<%=ideliance.core.object.type.TripletType.SUBJET.getValue() %>"/>
<c:set var="typeTriplet" value="<%=ideliance.core.object.type.TripletType.TRIPLET.getValue() %>"/>
<c:set var="lang" value="${sessionScope.lang}" scope="page" />
<c:set var="triplet" value="${sessionScope.triplet}" scope="page" />
<c:set var="listSubject" value="${sessionScope.listSubject}" scope="page" />
<c:set var="listRelation" value="${sessionScope.listRelation}" scope="page" />
<c:set var="listTriplet" value="${sessionScope.listTriplet}" scope="page" />

<div class="page-header">
	<h1><c:out value="${requestScope.title}" /></h1>
</div>

<div class="row-fluid">
	<div class="span9">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />">Home</a> <span class="divider">/</span></li>
			<li><a href="<c:out value="${sessionScope.root}" />triplet/list">Liste des triplets</a> <span class="divider">/</span></li>
			<li class="active"><c:out value="${requestScope.title}" /></li>
		</ul>
	</div>
</div>

<form class="form-horizontal" action="" method="post">
	<div class="control-group">
		<label class="control-label">Subject "Subject"</label>
		<div class="controls">
			<select name="subjectSubject">
				<option value="null">---</option>
				<c:forEach items="${listSubject}" var="subject">
					<c:choose>
						<c:when test="${triplet != null && triplet.typeSubject.value == typeSubject && triplet.SSubject.id == subject.id}">
				<option value="<c:out value="${subject.id}" />" selected="selected"><c:out value="${subject.getEntitled(lang)}" /></option>
						</c:when>
						<c:otherwise>
				<option value="<c:out value="${subject.id}" />"><c:out value="${subject.getEntitled(lang)}" /></option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Triplet "Subject"</label>
		<div class="controls">
			<select name="subjectTriplet">
				<option value="null">---</option>
				<c:forEach items="${listTriplet}" var="t">
					<c:choose>
						<c:when test="${triplet != null && triplet.typeSubject.value == typeTriplet && triplet.TSubject.id == t.id}">
				<option value="<c:out value="${t.id}" />" selected="selected"><c:out value="${t.toString(lang)}" /></option>
						</c:when>
						<c:otherwise>
				<option value="<c:out value="${t.id}" />"><c:out value="${t.toString(lang)}" /></option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Relation</label>
		<div class="controls">
			<select name="relation">
				<c:forEach items="${listRelation}" var="relation">
					<c:choose>
						<c:when test="${triplet != null && triplet.relation.id == relation.id}">
				<option value="<c:out value="${relation.id}" />" selected="selected"><c:out value="${relation.getEntitled(lang)}" /></option>
						</c:when>
						<c:otherwise>
				<option value="<c:out value="${relation.id}" />"><c:out value="${relation.getEntitled(lang)}" /></option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Subject "Complement"</label>
		<div class="controls">
			<select name="complementSubject">
				<option value="null">---</option>
				<c:forEach items="${listSubject}" var="subject">
					<c:choose>
						<c:when test="${triplet != null && triplet.typeComplement.value == typeSubject && triplet.SComplement.id == subject.id}">
				<option value="<c:out value="${subject.id}" />" selected="selected"><c:out value="${subject.getEntitled(lang)}" /></option>
						</c:when>
						<c:otherwise>
				<option value="<c:out value="${subject.id}" />"><c:out value="${subject.getEntitled(lang)}" /></option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Triplet "Complement"</label>
		<div class="controls">
			<select name="complementTriplet">
				<option value="null">---</option>
				<c:forEach items="${listTriplet}" var="t">
					<c:choose>
						<c:when test="${triplet != null && triplet.typeComplement.value == typeTriplet && triplet.TComplement.id == t.id}">
				<option value="<c:out value="${t.id}" />" selected="selected"><c:out value="${t.toString(lang)}" /></option>
						</c:when>
						<c:otherwise>
				<option value="<c:out value="${t.id}" />"><c:out value="${t.toString(lang)}" /></option>
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
		<input type="hidden" name="id" value="<c:out value="${triplet.id}" />" />
		<button type="submit" name="submit" class="btn btn-primary">Modifier</button>
			</c:otherwise>
		</c:choose>
	</div>
</form>