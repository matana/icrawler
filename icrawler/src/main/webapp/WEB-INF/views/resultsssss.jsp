<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="de">
<head>

<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

<title>Results</title>
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
				<c:if test="${not empty docs}">
					<ul>
						<c:forEach var="doc" items="${docs}">
							<li>
								<p>
									<span>DOMAIN: </span><a href="${doc.url}">${doc.url}</a><br/>
									<span>CONTENT: <i>${fn:substring(doc.text, 0, 404)}...</i></span><br/>
									<span>LINKS: ${fn:length(doc.links)}</span>
								</p>
							</li>
							<c:set var="links" scope="session" value="${doc.links}"/>
								<ol>
									<c:forEach var="link" items="${links}">
										<li>
											<p>
												<a href="article/${link}">${link}</a>
											</p>
										</li>
									</c:forEach>
								</ol>
						</c:forEach>
					</ul>
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>