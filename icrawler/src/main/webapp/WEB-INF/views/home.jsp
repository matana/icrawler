<!DOCTYPE html>
<html lang="de">
<head>

<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

<title>Home</title>
<meta name="description" content="The allmost intelligent web crawler" />

<!-- jQuery -->
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>

<!-- Bootstrap -->
<link rel="stylesheet" href="css/bootstrap.min.css" />
<link rel="stylesheet" href="css/bootstrap-theme.min.css" />
<script src="js/bootstrap.min.js"></script>

<script src="js/ajax.js"></script>


</head>
<body>
	<%@ include file="navigation.jsp" %>
	<div class="container-fluid">
		<%@ include file="buildIndex.jsp" %>
		<%@ include file="classifyEntry.jsp" %>
		<%@ include file="searchResults.jsp" %>
	</div>
</body>
</html>