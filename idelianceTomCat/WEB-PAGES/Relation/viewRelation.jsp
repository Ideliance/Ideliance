<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://ideliance.fr/tag-lib" prefix="f"%>
<head>
	<title><c:out value="${requestScope.title}" /></title>
</head>

<c:set var="typeSubject" value="<%=ideliance.core.object.type.TripletType.SUBJET.getValue() %>"/>
<c:set var="lang" value="${sessionScope.lang}" scope="page" />
<c:set var="relation" value="${sessionScope.relation}" scope="page" />
<c:set var="listTriplet" value="${sessionScope.listTriplet}" scope="page" />

<div class="page-header">
	<h1><c:out value="${relation.getEntitled(lang)}" /></h1>
</div>

<div class="row-fluid">
	<div class="span9">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />">Home</a> <span class="divider">/</span></li>
			<li><a href="<c:out value="${sessionScope.root}" />relation/list">Liste des relations</a> <span class="divider">/</span></li>
			<li class="active">Fiche de relation</li>
		</ul>
	</div>
</div>

<c:if test="${relation.inverse != null}">
<div class="well">
	La relation a un inverse : <a href="<c:out value="${sessionScope.root}" />relation/view?id=<c:out value="${relation.inverse.id}" />"><c:out value="${relation.inverse.getEntitled(lang)}" /></a>
</div>
</c:if>

<table class="table table-striped tablesorter" id="table-relation">
	<thead>
		<tr>
			<th class="span5">Subject</th>
			<th class="span1"></th>
			<th class="span4">Complement</th>
			<th class="span2"></th>
		</tr>
	</thead>
	
	<tbody>
	<c:forEach items="${listTriplet}" var="triplet">
		<tr>
			<c:choose>
				<c:when test="${triplet.typeSubject.value == typeSubject}">
			<td><a href="<c:out value="${sessionScope.root}" />subject/view?id=<c:out value="${triplet.SSubject.id}" />&amp;type=<c:out value="${triplet.typeSubject.value}" />"><c:out value="${triplet.SSubject.getEntitled(lang)}" /></a></td>
				</c:when>
				<c:otherwise>
			<td>(triplet) <c:out value="${triplet.TSubject.id}" /></td>
				</c:otherwise>
			</c:choose>
		
			<td><i class="icon-arrow-right"></i></td>
		
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

</body>
</html>