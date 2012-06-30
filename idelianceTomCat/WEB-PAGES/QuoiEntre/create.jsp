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
			<li class="active"><c:out value="${requestScope.title}" /></li>
		</ul>
	</div>
</div>
<form class="form-horizontal" action="<c:out value="${sessionScope.root}" />quoiEntre/preDisplay" method="GET">
	<div class="control-group">
		<label class="control-label">Subject A</label>
		<div class="controls">
			<select name="subjectA">
				<option value="null">---</option>
				<c:forEach items="${listSubject}" var="subject">
					
				<option value="<c:out value="${subject.id}" />"><c:out value="${subject.getEntitled(lang)}" /></option>
						
				</c:forEach>
			</select>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Subject B </label>
		<div class="controls">
			<select name="subjectB">
				<option value="null">---</option>
				<c:forEach items="${listSubject}" var="subject">
					
				<option value="<c:out value="${subject.id}" />"><c:out value="${subject.getEntitled(lang)}" /></option>
						
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="control-group">
            <label class="control-label" for="multiSelect">Excluded subjects</label>
            <div class="controls">
              <select name="excludedSubject" multiple="multiple" id="multiSelect">
               <c:forEach items="${listSubject}" var="subject">
					
				<option value="<c:out value="${subject.id}" />"><c:out value="${subject.getEntitled(lang)}" /></option>
						
				</c:forEach>
              </select>
            </div>
     </div>
     <div class="control-group">
            <label class="control-label" for="multiSelect">Excluded relations</label>
            <div class="controls">
              <select name="excludedRelation" multiple="multiple" id="multiSelect">
               <c:forEach items="${listRelation}" var="relation">
					
				<option value="<c:out value="${relation.id}" />"><c:out value="${relation.getEntitled(lang)}" /></option>
						
				</c:forEach>
              </select>
            </div>
     </div>
     
	<input type="hidden" value="1" name="minNbRelations" />
	<!-- <div class="control-group">
		<label class="control-label">Min Number of relations</label>
		<div class="controls">
			<select name="minNbRelations">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
				<option value="9">9</option>
			</select>
		</div>
	</div> -->
	<div class="control-group">
		<label class="control-label">Max Number of relations</label>
		<div class="controls">
			<select name="maxNbRelations">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
				<option value="9">9</option>
			</select>
		</div>
	</div>
	
	<div class="form-actions">
		<button type="submit" name="submit" class="btn btn-primary">Créer</button>
	</div>
</form>