<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://ideliance.fr/tag-lib" prefix="f"%>
<head>
	<title><c:out value="${requestScope.title}" /></title>
</head>

<c:set var="typeSubject" value="<%=ideliance.core.object.type.TripletType.SUBJET.getValue() %>"/>
<c:set var="lang" value="${sessionScope.lang}" scope="page" />
<c:set var="subject" value="${sessionScope.subject}" scope="page" />
<c:set var="lastRelation" value="0" scope="page" />
<c:set var="listTriplet" value="${sessionScope.listTriplet}" scope="page" />
<c:set var="listSimplification" value="${sessionScope.listSimplification}" scope="page" />
<c:set var="subjectFlag" value="${sessionScope.subjectFlag}" scope="page" />

<div class="page-header">
	<h1>
		Fiche de sujet
		<small><c:out value="${subject.getEntitled(lang)}" /></small>
	</h1>
</div>

<div class="row-fluid">
	<div class="span12">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />">Home</a> <span class="divider">/</span></li>
			<li class="active">Fiche de sujet</li>
		</ul>
	</div>
</div>
					
<div class="subnav">
	<ul class="nav nav-pills">
		<li class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				Sujet
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li><a href="<c:out value="${sessionScope.root}" />subject/display?id=<c:out value="${subject.id}" />&type=<c:out value="${subjectFlag}" />"><i class="icon-eye-open"></i> Visualiser</a></li>
				<li><a href="<c:out value="${sessionScope.root}" />subject/modify?id=<c:out value="${subject.id}" />"><i class="icon-pencil"></i> Modifier</a></li>
				<li><a href="<c:out value="${sessionScope.root}" />subject/delete?id=<c:out value="${subject.id}" />"><i class="icon-trash"></i> Supprimer</a></li>
			</ul>
		</li>
		<li class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				Actions
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li><a href="<c:out value="${sessionScope.root}" />subject/modify?id=<c:out value="${subject.id}" />"><i class="icon-pencil"></i> Catégoriser</a></li>
				<li><a href="<c:out value="${sessionScope.root}" />subject/merge?id=<c:out value="${subject.id}" />"><i class="icon-list-alt"></i> Fusionner</a></li>
				<li><a href="<c:out value="${sessionScope.root}" />subject/duplicate?id=<c:out value="${subject.id}" />"><i class="icon-tags"></i> Dupliquer</a></li>
				<li><a href="<c:out value="${sessionScope.root}" />subject/add-triplet?subject=<c:out value="${subject.id}" />"><i class="icon-plus"></i> Ajouter énoncé</a></li>
			</ul>
		</li>
		<li class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				Exporter
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li><a href="<c:out value="${sessionScope.root}" />export/excel?subject=<c:out value="${subject.id}" />"><i class="icon-th"></i> Excel</a></li>
				<li><a href="<c:out value="${sessionScope.root}" />export/xml?subject=<c:out value="${subject.id}" />"><i class="icon-th-list"></i> XML</a></li>
				<li><a href="<c:out value="${sessionScope.root}" />export/svc?subject=<c:out value="${subject.id}" />"><i class="icon-list-alt"></i> SVC</a></li>
			</ul>
		</li>
		<li class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				Outils
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li><a href="#"><i class="icon-eye-close"></i> IIA</a></li>
				<li><a href="#"><i class="icon-eye-close"></i> Segmentation du texte</a></li>
				<li><a href="#"><i class="icon-eye-close"></i> Ressemblance</a></li>
			</ul>
		</li>
		<li class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				Recherche
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li><a href="#"><i class="icon-screenshot"></i> Google</a></li>
				<li><a href="#"><i class="icon-screenshot"></i> Trends</a></li>
				<li><a href="#"><i class="icon-screenshot"></i> Blogs</a></li>
				<li><a href="#"><i class="icon-screenshot"></i> Clusters</a></li>
				<li><a href="#"><i class="icon-screenshot"></i> Wikipedia</a></li>
			</ul>
		</li>
	</ul>
</div>

<table class="table table-striped tablesorter" id="table-subject">
	<thead>
		<tr>
			<th class="span5">Relation</th>
			<th class="span5">Complement</th>
			<th class="span2"></th>
		</tr>
	</thead>
	
	<tbody>
	<c:forEach items="${listTriplet}" var="triplet">
		<tr>
			<td>
				<a href="<c:out value="${sessionScope.root}" />relation/view?id=<c:out value="${triplet.relation.id}" />"><c:out value="${triplet.relation.getEntitled(lang)}" /></a>
			</td>
		
			<c:choose>
				<c:when test="${triplet.typeComplement.value == typeSubject}">
			<td><a href="<c:out value="${sessionScope.root}" />subject/view?id=<c:out value="${triplet.SComplement.id}" />&amp;type=<c:out value="${triplet.typeComplement.value}" />"><c:out value="${triplet.SComplement.getEntitled(lang)}" /></a></td>
				</c:when>
				<c:otherwise>
			<td>(triplet) <c:out value="${triplet.TComplement.id}" /></td>
				</c:otherwise>
			</c:choose>
			
			<td>
				<div class="btn-group pull-right">
					<a href="#" class="btn" rel="popover" data-original-title="Informations" data-content="<strong>Création :</strong> <c:out value="${f:dateFormat(triplet.dateCreation)}" /> by <c:out value="${triplet.authorCreation}" /><br/><strong>Modification :</strong> <c:out value="${f:dateFormat(triplet.dateModification)}" /> by <c:out value="${triplet.authorModification}" />"><i class="icon-info-sign"></i></a>
					<button class="btn dropdown-toggle" data-toggle="dropdown">
						<i class="icon-chevron-down"></i>
					</button>
					<ul class="dropdown-menu">
						<li><a href="<c:out value="${sessionScope.root}" />triplet/modify?id=<c:out value="${triplet.id}" />"><i class="icon-pencil"></i> Edit</a></li>
						<li><a href="<c:out value="${sessionScope.root}" />triplet/delete?id=<c:out value="${triplet.id}" />"><i class="icon-trash"></i> Delete</a></li>
					</ul>
				</div>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>

<c:if test="${subject.getFreeText(lang) != ''}">
	<h4>Free Text</h4>
	
	<pre><c:out value="${subject.getFreeText(lang)}" /></pre>
</c:if>

</body>
</html>