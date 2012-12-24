<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://ideliance.fr/tag-lib" prefix="f"%>
<head>
	<title><c:out value="${requestScope.title}" /></title>
</head>

<c:set var="lang" value="${sessionScope.lang}" scope="page" />
<c:set var="category" value="${sessionScope.category}" scope="page" />
<c:set var="listTriplet" value="${sessionScope.listTriplet}" scope="page" />

<div class="page-header">
	<h1>
		Fiche de catégorie
		<small><c:out value="${category.getEntitled(lang)}" /></small>
	</h1>
</div>

<div class="row-fluid">
	<div class="span12">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />">Home</a> <span class="divider">/</span></li>
			<li class="active"><c:out value="${category.getEntitled(lang)}" /></li>
		</ul>
	</div>
</div>

<form action="" method="post" id="category">
	<input type="hidden" name="type" value="" />
	
	<div class="subnav">
		<ul class="nav nav-pills">
			<li class="dropdown">
				<a class="dropdown-toggle" data-toggle="dropdown" href="#">
					Catégorie
					<span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<li><a href="<c:out value="${sessionScope.root}" />category/modify?id=<c:out value="${category.id}" />"><i class="icon-pencil"></i> Renommer</a></li>
					<li><a href="<c:out value="${sessionScope.root}" />category/delete?id=<c:out value="${category.id}" />"><i class="icon-trash"></i> Supprimer</a></li>
					<li><a href="<c:out value="${sessionScope.root}" />category/merge?id=<c:out value="${category.id}" />"><i class="icon-list-alt"></i> Fusionner</a></li>
				</ul>
			</li>
			<li class="dropdown">
				<a class="dropdown-toggle" data-toggle="dropdown" href="#">
					Sujet
					<span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<li><a href="<c:out value="${sessionScope.root}" />subject/add?category=<c:out value="${category.id}" />"><i class="icon-plus"></i> Nouveau</a></li>
					<!-- <li><a href="<c:out value="${sessionScope.root}" />category/add-subject?category=<c:out value="${category.id}" />"><i class="icon-flag"></i> Ajouter</a></li> -->
					<li onclick="document.getElementById('category').type.value='remove';document.getElementById('category').submit()"><a href="#"><i class="icon-minus"></i> Enlever</a></li>
					<li onclick="document.getElementById('category').type.value='delete';document.getElementById('category').submit()"><a href="#"><i class="icon-trash"></i> Supprimer</a></li>
				</ul>
			</li>
			<li class="dropdown">
				<a class="dropdown-toggle" data-toggle="dropdown" href="#">
					Filtre
					<span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<li><a href="#"><i class="icon-pencil"></i> Modifier</a></li>
					<li><a href="#"><i class="icon-ban-circle"></i> Remettre à zéro</a></li>
				</ul>
			</li>
			<li class="dropdown">
				<a class="dropdown-toggle" data-toggle="dropdown" href="#">
					Exporter
					<span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<li><a href="<c:out value="${sessionScope.root}" />export/excel?category=<c:out value="${category.id}" />"><i class="icon-th"></i> Excel</a></li>
					<li><a href="<c:out value="${sessionScope.root}" />export/xml?category=<c:out value="${category.id}" />"><i class="icon-th-list"></i> XML</a></li>
					<li><a href="<c:out value="${sessionScope.root}" />export/svc?category=<c:out value="${category.id}" />"><i class="icon-list-alt"></i> SVC</a></li>
				</ul>
			</li>
		</ul>
	</div>
	
	<table class="table table-striped tablesorter" id="table-category">
		<thead>
			<tr>
				<th class="span1"></th>
				<th>Intitulé</th>
				<th class="span2"></th>
			</tr>
		</thead>
		
		<tbody>
		<c:forEach items="${listTriplet}" var="triplet">
			<tr>
				<td><input type="checkbox" name="subject[]" value="<c:out value="${triplet.SSubject.id}" />" /></td>
				<td><a href="<c:out value="${sessionScope.root}" />subject/view?id=<c:out value="${triplet.SSubject.id}" />&amp;type=<%=ideliance.core.object.type.TripletType.SUBJET.getValue() %>"><c:out value="${triplet.SSubject.getEntitled(lang)}" /></a></td>
				<td>
					<div class="btn-group pull-right">
						<a href="#" class="btn" rel="popover" data-original-title="Informations" data-content="<strong>Création :</strong> <c:out value="${f:dateFormat(triplet.dateCreation)}" /> by <c:out value="${triplet.authorCreation}" /><br/><strong>Modification :</strong> <c:out value="${f:dateFormat(triplet.dateModification)}" /> by <c:out value="${triplet.authorModification}" />"><i class="icon-info-sign"></i></a>
						<button class="btn dropdown-toggle" data-toggle="dropdown">
							<i class="icon-chevron-down"></i>
						</button>
						<ul class="dropdown-menu">
							<li><a href="<c:out value="${sessionScope.root}" />subject/modify?id=<c:out value="${triplet.SSubject.id}" />"><i class="icon-pencil"></i> Modifier</a></li>
							<li><a href="<c:out value="${sessionScope.root}" />triplet/delete?id=<c:out value="${triplet.id}" />"><i class="icon-minus"></i> Enlever</a></li>
							<li><a href="<c:out value="${sessionScope.root}" />subject/delete?id=<c:out value="${triplet.SSubject.id}" />"><i class="icon-trash"></i> Supprimer</a></li>
						</ul>
					</div>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</form>

</body>
</html>