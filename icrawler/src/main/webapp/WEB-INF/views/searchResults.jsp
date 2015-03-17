<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="row">
	<div class="col-md-12">
	<h3> ${fn:length(results)} Suchergebniss(e) für '${query}'</h3>
		<ul>
			<c:forEach var="result" items="${results}">
				 <li>
				 	<span>${result.topic} </span><a href="${result.url}" target="_blank">${result.title}</a>
				 </li>
			</c:forEach>
		</ul>
	</div>
</div>