<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://ideliance.fr/tag-lib" prefix="f"%>
<head>
	<title>User List</title>
</head>

<c:set var="listUser" value="${sessionScope.listUser}" scope="page" />

<div class="page-header">
	<h1>User List</h1>
</div>

<div class="row">
	<div class="span9">
		<ul class="breadcrumb">
			<li><a href="<c:out value="${sessionScope.root}" />administration/">Home</a> <span class="divider">/</span></li>
			<li class="active">User List</li>
		</ul>
	</div>

	<div class="span3">
		<a class="btn btn-primary pull-right" href="<c:out value="${sessionScope.root}" />administration/user/add">
			<i class="icon-plus icon-white"></i> Add User
		</a>
	</div>
</div>

<table class="table table-striped">
	<thead>
		<tr>
			<th class="span1">#</th>
			<th class="span2">Login</th>
			<th class="span2">Level</th>
			<th class="span3">Creation</th>
			<th class="span3">Modification</th>
			<th class="span1"></th>
		</tr>
	</thead>
	
	<tbody>
	<c:forEach items="${listUser}" var="user">
		<tr>
			<td><c:out value="${user.id}" /></td>
			<td><c:out value="${user.login}" /></td>
			<td><c:out value="${user.level.getValue()}" /></td>
			<td><c:out value="${f:dateFormat(user.dateCreation)}" /> by <c:out value="${user.authorCreation}" /></td>
			<td><c:out value="${f:dateFormat(user.dateModification)}" /> by <c:out value="${user.authorModification}" /></td>
			<td>
				<div class="btn-group pull-right">
					<a class="btn btn-mini dropdown-toggle" data-toggle="dropdown" href="#">
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="<c:out value="${sessionScope.root}" />administration/user/modify?id=<c:out value="${user.id}" />"><i class="icon-pencil"></i> Edit</a></li>
						<li><a href="<c:out value="${sessionScope.root}" />administration/user/delete?id=<c:out value="${user.id}" />"><i class="icon-trash"></i> Delete</a></li>
					</ul>
				</div>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>