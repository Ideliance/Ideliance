<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
	<title><c:out value="${requestScope.title}" /></title>
</head>
<div class="page-header">
	<h1><c:out value="${requestScope.title}" /></h1>
</div>
<div class="row-fluid">
	<div class="span9">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />">Home</a> <span class="divider">/</span></li>
			<li><a href="<c:out value="${sessionScope.root}" />quoiEntre">Quoi Entre</a> <span class="divider">/</span></li>
			<li><a href="<c:out value="${sessionScope.root}" />quoiEntre/create">Créer</a> <span class="divider">/</span></li>
			<li class="active"><c:out value="${requestScope.title}" /></li>
		</ul>
	</div>
</div>

<c:set var="resultsTable" value="${requestScope.resultsTable}" scope="page" />
<c:set var="maxNbRelations" value="${requestScope.maxNbRelations}" scope="page" />
<c:set var="nbResultats" value="${requestScope.nbResultats}" scope="page" />

<div>
<h3><c:out value="${nbResultats}" /> chemins ont été trouvés. </h3>
<c:choose>
				<c:when test="${maxNbRelations > 0}">
					<a class="btn" type="submit" href="/quoiEntre/display">Visualiser</a>
					</c:when>
			</c:choose>
</div>
<c:choose>
				<c:when test="${maxNbRelations > 0}">
					
				
<table class="table table-striped">
	<thead>
		<c:forEach var="columnNb" begin="0" end="${(maxNbRelations-1)*2}">
			<c:choose>
				<c:when test="${columnNb%2 == 0}">
					<th>Subject ${columnNb/2}</th>
				</c:when>
				<c:otherwise>
					<th>Relation ${(columnNb+1)/2}</th>
				</c:otherwise>
			</c:choose>
			
		</c:forEach>
	</thead>
	<tbody>
		<c:forEach var="tabLine" items="${resultsTable}">
			<tr>
			<c:forEach var="item" items="${tabLine}">
				
				<c:choose>
					<c:when test="${item.getRelation() != Null}">
						<td>
						<a href="<c:out value="${sessionScope.root}" />relation/view?id=${item.getRelation().getId()}" title="${item.getRelation().getEntitled(lang)}">
							<c:out value="${item.getRelation().getEntitled(lang)}" />
						</a>
						</td>
					</c:when>
				</c:choose>
				<td>
				<a href="<c:out value="${sessionScope.root}" />subject/view?id=<c:out value="${item.getSubject().getId()}&type=0" />" title="<c:out value="${item.getSubject().getEntitled(lang)}" />">
					<c:out value="${item.getSubject().getEntitled(lang)}" />
				</a>
				</td>
			</c:forEach>
			</tr>
		</c:forEach>
	</tbody>
</table>
</c:when>
			</c:choose>