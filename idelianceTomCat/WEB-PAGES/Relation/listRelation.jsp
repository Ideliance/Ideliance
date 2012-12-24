<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://ideliance.fr/tag-lib" prefix="f"%>
<head>
	<title><c:out value="${requestScope.title}" /></title>
</head>

<c:set var="lang" value="${sessionScope.lang}" scope="page" />
<c:set var="listRelation" value="${sessionScope.listRelation}" scope="page" />

<div class="page-header">
	<h1><c:out value="${requestScope.title}" /></h1>
</div>

<div class="row-fluid">
	<div class="span12">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />">Home</a> <span class="divider">/</span></li>
			<li class="active">Liste des relations</li>
		</ul>
	</div>
</div>

<div class="subnav">
	<ul class="nav nav-pills">
		<li class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				Relation
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li><a href="<c:out value="${sessionScope.root}" />relation/add"><i class="icon-plus"></i> Ajouter</a></li>
				<li><a href="#"><i class="icon-trash"></i> Supprimer</a></li>
			</ul>
		</li>
	</ul>
</div>

<table class="table table-striped">
	<thead>
		<tr>
			<th></th>
			<th>Intitulé</th>
			<th>Inverse</th>
			<th class="span2"></th>
		</tr>
	</thead>
	
	<tbody>
	<c:forEach items="${listRelation}" var="relation">
		<tr>
			<td><input type="checkbox" name="relation[]" value="<c:out value="${relation.id}" />" /></td>
			<td>
				<a href="<c:out value="${sessionScope.root}" />relation/view?id=<c:out value="${relation.id}" />">
					<c:out value="${relation.getEntitled(lang)}" />
				</a>
			</td>
			<td>
				<c:if test="${relation.inverse != null}">
				<a href="<c:out value="${sessionScope.root}" />relation/view?id=<c:out value="${relation.inverse.id}" />">
					<c:out value="${relation.inverse.getEntitled(lang)}" />
				</a>
				</c:if>
			</td>
			<td>
				<div class="btn-group pull-right">
					<a href="#" class="btn" rel="popover" data-original-title="Informations" data-content="<strong>Création :</strong> <c:out value="${f:dateFormat(relation.dateCreation)}" /> by <c:out value="${relation.authorCreation}" /><br/><strong>Modification :</strong> <c:out value="${f:dateFormat(relation.dateModification)}" /> by <c:out value="${relation.authorModification}" />"><i class="icon-info-sign"></i></a>
					<button class="btn dropdown-toggle" data-toggle="dropdown">
						<i class="icon-chevron-down"></i>
					</button>
					<ul class="dropdown-menu">
						<li><a href="<c:out value="${sessionScope.root}" />relation/modify?id=<c:out value="${relation.id}" />"><i class="icon-pencil"></i> Modifier</a></li>
						<li><a href="<c:out value="${sessionScope.root}" />relation/delete?id=<c:out value="${relation.id}" />"><i class="icon-trash"></i> Supprimer</a></li>
					</ul>
				</div>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>