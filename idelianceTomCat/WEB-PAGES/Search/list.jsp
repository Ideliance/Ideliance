<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://ideliance.fr/tag-lib" prefix="f"%>
<head>
	<title><c:out value="${requestScope.title}" /></title>
</head>

<c:set var="typeSubject" value="<%=ideliance.core.object.type.TripletType.SUBJET.getValue() %>"/>
<c:set var="lang" value="${sessionScope.lang}" scope="page" />
<c:set var="listSubject" value="${sessionScope.listSubject}" scope="page" />
<c:set var="listRelation" value="${sessionScope.listRelation}" scope="page" />

<div class="page-header">
	<h1><c:out value="${requestScope.title}" /></h1>
</div>

<div class="row-fluid">
	<div class="span12">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />">Home</a> <span class="divider">/</span></li>
			<li class="active">Liste des sujets</li>
		</ul>
	</div>
</div>

<h3>Liste des relations</h3>

<table class="table table-striped tablesorter" id="table-relation-search">
	<thead>
		<tr>
			<th>Intitulé</th>
			<th class="span2"></th>
		</tr>
	</thead>
	
	<tbody>
	<c:forEach items="${listRelation}" var="relation">
		<tr>
			<td>
				<a href="<c:out value="${sessionScope.root}" />relation/view?id=<c:out value="${relation.id}" />"><c:out value="${relation.getEntitled(lang)}" /></a>
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

<h3>Liste des sujets</h3>

<table class="table table-striped tablesorter" id="table-subject-search">
	<thead>
		<tr>
			<th>Intitulé</th>
			<th class="span2"></th>
		</tr>
	</thead>
	
	<tbody>
	<c:forEach items="${listSubject}" var="subject">
		<tr>
			<td>
				<a href="<c:out value="${sessionScope.root}" />subject/view?id=<c:out value="${subject.id}" />&amp;type=<c:out value="${typeSubject}" />"><c:out value="${subject.getEntitled(lang)}" /></a>
			</td>
			<td>
				<div class="btn-group pull-right">
					<a href="#" class="btn" rel="popover" data-original-title="Informations" data-content="<strong>Création :</strong> <c:out value="${f:dateFormat(subject.dateCreation)}" /> by <c:out value="${subject.authorCreation}" /><br/><strong>Modification :</strong> <c:out value="${f:dateFormat(subject.dateModification)}" /> by <c:out value="${subject.authorModification}" />"><i class="icon-info-sign"></i></a>
					<button class="btn dropdown-toggle" data-toggle="dropdown">
						<i class="icon-chevron-down"></i>
					</button>
					<ul class="dropdown-menu">
						<li><a href="<c:out value="${sessionScope.root}" />subject/modify?id=<c:out value="${subject.id}" />"><i class="icon-pencil"></i> Modifier</a></li>
						<li><a href="<c:out value="${sessionScope.root}" />subject/delete?id=<c:out value="${subject.id}" />"><i class="icon-trash"></i> Supprimer</a></li>
					</ul>
				</div>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>