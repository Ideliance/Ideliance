<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<decorator:usePage id="thePage" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title><decorator:title default="Welcome!" /> | Ideliance</title>
	<link href="/public/css/bootstrap.css" rel="stylesheet">
	<link href="/public/css/bootstrap-responsive.min.css" rel="stylesheet">
	<link href="/public/css/tablesorter.css" rel="stylesheet">
	<link href="/public/css/docs.css" rel="stylesheet">
	<link href="/public/css/jquery-ui-1.8.21.custom.css" rel="stylesheet">
	<script type="text/javascript" src="/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="/public/js/jquery.ui.core.js"></script>
	<script type="text/javascript" src="/public/js/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="/public/js/jquery.ui.button.js"></script>
	<script type="text/javascript" src="/public/js/jquery.ui.position.js"></script>
	<script type="text/javascript" src="/public/js/jquery.ui.autocomplete.js"></script>
	<script type="text/javascript" src="/public/js/autocomplete.js"></script>
	<style type="text/css">
	.sidebar-nav {
		padding: 9px 0;
	}
	.ui-combobox {
		position: relative;
		display: inline-block;
	}
	.ui-combobox-toggle {
		position: absolute;
		top: 0;
		bottom: 0;
		margin-left: -1px;
		padding: 0;
		/* adjust styles for IE 6/7 */
		*height: 1.7em;
		*top: 0.1em;
	}
	.ui-no-corner-right {
		-moz-border-radius-topright: 0px;
		-webkit-border-top-right-radius: 0px;
		-khtml-border-top-right-radius: 0px;
		border-top-right-radius: 0px;
		-moz-border-radius-bottomright: 0px;
		-webkit-border-bottom-right-radius: 0px;
		-khtml-border-bottom-right-radius: 0px;
		border-bottom-right-radius: 0px;
	}
	</style>
	<decorator:head />
</head>
<body>
	<%@ include file="/WEB-INF/decorators/header.jsp"%>

	<div class="container">

		<div class="row-fluid">
			<div class="span4">
				<%@ include file="/WEB-INF/decorators/sidebar.jsp"%>
			</div>
			<div class="span8">
				<decorator:body />
			</div>
		</div>

		<%@ include file="/WEB-INF/decorators/footer.jsp"%>
	</div>
	
	<script type="text/javascript" src="/public/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/public/js/AJAX/layout.js"></script>
	<script type="text/javascript" src="/public/js/jquery.tablesorter.js"></script>
	<decorator:getProperty property="page.additionalJavascript"/>
</body>
</html>