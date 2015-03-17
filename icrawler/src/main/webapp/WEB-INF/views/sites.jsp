<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="de">
<head>

<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

<title>Sites</title>
<meta name="description" content="The allmost intelligent web crawler" />

<!-- jQuery -->
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>

<!-- Bootstrap -->
<link rel="stylesheet" href="css/bootstrap.min.css" />

<script src="js/bootstrap.min.js"></script>

<script src="js/ajax.js"></script>

</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12" id="crawl_results">
				<c:if test="${not empty sites}">
					<ul>
						<c:forEach var="site" items="${sites}">
							<li>
								<p>
									<span>DOMAIN: ${site.name}</span><br/>
									<span>ERSTELLT AM: ${site.simpleDate}</span><br/>
									<span>RESULTATE: ${fn:length(site.docs)} </span> - <a href="/icrawler/">Zeige alle Resultate dieser Seite</a>
								</p>
							</li>
						</c:forEach>
					</ul>
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>