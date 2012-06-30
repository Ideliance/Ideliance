<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %> 
 <decorator:usePage id="thePage" /> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html> 
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><decorator:title default="Welcome!"/> | Ideliance<% //out.print(" - "+thePage.getProperty("meta.plop")); %></title> 
    <link href="/public/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
      .center {
      	text-align: center;
      }
    </style>
    <link href="/public/css/bootstrap-responsive.min.css" rel="stylesheet">
    <decorator:head/> 
</head>
<body>

	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<a class="brand" href="/">Ideliance</a>
				<div class="nav-collapse">
					<ul class="nav">
						<li class="active"><a href="/administration/">Home</a></li>
						<li><a href="<%=session.getAttribute("root")%>administration/parameters">Globals Parameters</a></li>
						<li><a href="<%=session.getAttribute("root")%>administration/collections">Collections</a></li>
						<li><a href="<%=session.getAttribute("root")%>administration/user/">Users</a></li>
						<li><a href="<%=session.getAttribute("root")%>administration/import">Importation</a></li>
					</ul>
					<p class="navbar-text pull-right">
						Logged in as <a href="<%=session.getAttribute("root")%>disconnection"><%=session.getAttribute("login")%></a>
					</p>
				</div>
			</div>
		</div>
	</div>
	
	<div class="container">
    	<div class="row-fluid">
			<div class="span12">
				<decorator:body/> 
			</div>
		</div>
		<%@ include file="/WEB-INF/decorators/footer.jsp" %>
	</div>
	<script src="/public/js/jquery-1.7.2.min.js"></script>
	<script src="/public/js/bootstrap.min.js"></script>
</body> 
</html> 