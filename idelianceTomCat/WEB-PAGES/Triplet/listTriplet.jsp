<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://ideliance.fr/tag-lib" prefix="f"%>
<head>
	<title><c:out value="${requestScope.title}" /></title>
</head>

<c:set var="typeSubject" value="<%=ideliance.core.object.type.TripletType.SUBJET.getValue() %>"/>
<c:set var="lang" value="${sessionScope.lang}" scope="page" />
<c:set var="listTriplet" value="${sessionScope.listTriplet}" scope="page" />
<c:set var="pageNb" value="${requestScope.pageNb}" scope="page" />
<c:set var="pageCount" value="${requestScope.pageCount}" scope="page" />

<div class="page-header">
	<h1><c:out value="${requestScope.title}" /></h1>
</div>

<div class="row-fluid">
	<div class="span12">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />">Home</a> <span class="divider">/</span></li>
			<li class="active">Liste des triplets</li>
		</ul>
	</div>
</div>

<div class="subnav">
	<ul class="nav nav-pills">
		<li class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				Triplet
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li><a href="<c:out value="${sessionScope.root}" />triplet/add"><i class="icon-plus"></i> Ajouter</a></li>
				<li><a href="#"><i class="icon-trash"></i> Supprimer</a></li>
			</ul>
		</li>
	</ul>
</div>

<table class="table table-striped tablesorter" id="table-triplet">
	<thead>
		<tr>
			<th></th>
			<th>#</th>
			<th class="span3">Sujet</th>
			<th class="span3">Relation</th>
			<th class="span3">Complément</th>
			<th class="span2"></th>
		</tr>
	</thead>
	
	<tbody>
	<c:forEach items="${listTriplet}" var="triplet">
		<tr>
			<td><input type="checkbox" name="triplet[]" value="<c:out value="${triplet.id}" />" /></td>
			<td><c:out value="${triplet.id}" /></td>
			<c:choose>
				<c:when test="${triplet.typeSubject.value == typeSubject}">
			<td><a href="<c:out value="${sessionScope.root}" />subject/view?id=<c:out value="${triplet.SSubject.id}" />&amp;type=<c:out value="${triplet.typeSubject.value}" />"><c:out value="${triplet.SSubject.getEntitled(lang)}" /></a></td>
				</c:when>
				<c:otherwise>
			<td>(triplet) <c:out value="${triplet.TSubject.id}" /></td>
				</c:otherwise>
			</c:choose>
		
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
						<li><a href="<c:out value="${sessionScope.root}" />triplet/modify?id=<c:out value="${triplet.id}" />"><i class="icon-pencil"></i> Modifier</a></li>
						<li><a href="<c:out value="${sessionScope.root}" />triplet/delete?id=<c:out value="${triplet.id}" />"><i class="icon-trash"></i> Supprimer</a></li>
					</ul>
				</div>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
<div class="pagination pagination-right">
    <ul>
    	<c:choose>
			<c:when test="${pageNb > 1}">
				<li><a href="<c:out value="${sessionScope.root}" />triplet/list?page=<c:out value="${pageNb-1}"/>">« Previous</a></li>
			</c:when>
			<c:otherwise>
				<li class="disabled"><a href="#">« Previous</a></li>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${pageNb-2 > 1}">
				<li><a href="<c:out value="${sessionScope.root}" />triplet/list?page=1">1</a></li>
				<li class="disabled"><a href="#">...</a></li>
			</c:when>
		</c:choose>
    	<c:forEach var="pagin" begin="${(pageNb-2>1)?pageNb-2:1}" end="${(pageNb+2>pageCount)?pageCount:pageNb+2}">
    		<c:choose>
				<c:when test="${pagin == pageNb}">
					<li class="active"><a href="<c:out value="${sessionScope.root}" />triplet/list?page=<c:out value="${pagin}"/>"><c:out value="${pagin}"/></a></li>
				</c:when>
				<c:when test="${pagin < 1 || pagin > pageCount}">
					<li class="active"><a href="<c:out value="${sessionScope.root}" />triplet/list?page=<c:out value="${pagin}"/>"><c:out value="${pagin}"/></a></li>
				</c:when>
				<c:otherwise>
					<li><a href="<c:out value="${sessionScope.root}" />triplet/list?page=<c:out value="${pagin}"/>"><c:out value="${pagin}"/></a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
    	<c:choose>
			<c:when test="${pageNb+2 < pageCount}">
				<li class="disabled"><a href="#">...</a></li>
				<li><a href="<c:out value="${sessionScope.root}" />triplet/list?page=<c:out value="${pageCount}"/>"><c:out value="${pageCount}"/></a></li>
			</c:when>
    	</c:choose>
    	<c:choose>
			<c:when test="${pageNb < pageCount}">
				<li><a href="<c:out value="${sessionScope.root}" />triplet/list?page=<c:out value="${pageNb+1}"/>">Next »</a></li>
			</c:when>
			<c:otherwise>
				<li class="disabled"><a href="#">Next »</a></li>
			</c:otherwise>
		</c:choose>
    </ul>
</div>
