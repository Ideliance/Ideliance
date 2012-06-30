<%@page import="ideliance.core.object.type.UserLevel"%>
<header>
	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</a>
				<a class="brand" href="<%=session.getAttribute("root")%>">Ideliance</a>
				<div class="nav-collapse">
					<ul class="nav">
						<li class="active"><a href="<%=session.getAttribute("root")%>">Home</a></li>
						<li><a href="<%=session.getAttribute("root")%>subject/list">Sujets</a></li>
						<li><a href="<%=session.getAttribute("root")%>relation/list">Relations</a></li>
						<li><a href="<%=session.getAttribute("root")%>triplet/list">Triplets</a></li>
						<li><a href="<%=session.getAttribute("root")%>quoiEntre/create">Quoi Entre</a></li>
						<!--
						<li><a href="#">Requêtes</a></li>
						<li><a href="#">Tableaux</a></li>
						-->
<%
if (((UserLevel) session.getAttribute("level")) == UserLevel.ADMINISTRATOR) {
	out.println("<li><a href=\"" + session.getAttribute("root") + "administration/\">Administration</a></li>");
}
%>
					</ul>
					<p class="navbar-text pull-right">
						Logged in as <a href="<%=session.getAttribute("root")%>disconnection"><%=session.getAttribute("login")%></a>
					</p>
				</div>
			</div>
		</div>
	</div>
</header>