<%@page import="ideliance.core.object.type.TripletType"%>
<%@page import="ideliance.core.dao.DAOFactory"%>
<%@ page
	import="java.util.List,ideliance.core.dao.SubjectDAO,ideliance.core.dao.RelationDAO,ideliance.core.object.Subject,ideliance.core.object.Relation,ideliance.core.dao.TripletDAO,ideliance.core.object.Triplet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
SubjectDAO subjectDao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getSubjectDAO();
RelationDAO relationDao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getRelationDAO();
TripletDAO tripletDao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getTripletDAO();

Subject sCategory = subjectDao.selectSystem("CATEGORY");
Relation rIs = relationDao.selectSystem("ISA");

List<Triplet> listCategory = tripletDao.selectAll(-1, rIs.getId(), sCategory.getId(), TripletType.SUBJET, TripletType.SUBJET, false, true);

session.setAttribute("listCategory", listCategory);
%>

<c:set var="lang" value="${sessionScope.lang}" scope="page" />
<c:set var="listCategory" value="${sessionScope.listCategory}" scope="page" />

<div class="well sidebar-nav">
	<ul class="nav nav-list">
		<li class="nav-header">Recherche
			<form class="form-search" action="<c:out value="${sessionScope.root}" />search" method="post">
				<input type="text" class="search-query" name="search" />
				<button type="submit" class="btn">
					<i class="icon-search"></i> Search
				</button>
			</form>
		</li>
		
		<li class="nav-header">
			<a data-toggle="modal" href="#addRelation"><i class="icon-plus"></i> Ajouter une relation</a>
		</li>
		<li class="nav-header">
			<a data-toggle="modal" href="#addCategorie"><i class="icon-plus"></i> Ajouter une catégorie</a>
		</li>
		
		<li>
		<table class="tablesorter" id="table-sidebar">
			<thead>
				<tr>
					<th class="nav-header" style="text-align: left;">Catégories</th>
				</tr>
			</thead>
			<tbody>
		<c:forEach items="${listCategory}" var="triplet">
			<tr><td><a href="<c:out value="${sessionScope.root}" />category/view?id=<c:out value="${triplet.SSubject.id}" />"><c:out value="${triplet.SSubject.getEntitled(lang)}" /></a></td></tr>
		</c:forEach>
			</tbody>
		</table>
		</li>
	</ul>
</div>

<div class="modal hide fade" id="addCategorie">
	<div class="modal-header">
		<a class="close" data-dismiss="modal">×</a>
		<h3>Add a new category</h3>
	</div>
	<div class="modal-body">
		<p>Please enter the new categorie's name in the field bellow</p>
		<input id="addCategorieInput" class="xlarge" type="text" name="addCategorieInput" />
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal">Close</a> <a href="#" class="btn btn-primary" id="addCategorieButton">Add category</a>
	</div>
</div>

<div class="modal hide fade" id="addRelation">
	<div class="modal-header">
		<a class="close" data-dismiss="modal">×</a>
		<h3>Add a new relation</h3>
	</div>
	<div class="modal-body">
		<p>Please enter the new relation's name in the field bellow</p>
		<input id="addRelationEntitled1" class="xlarge" type="text" name="addRelationEntitled1" />
		<p>You can also specify the relation's inverse</p>
		<input id="addRelationEntitled2" class="xlarge" type="text" name="addRelationEntitled2" />
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal">Close</a> <a href="#" class="btn btn-primary" id="addRelationButton">Add relation</a>
	</div>
</div>
