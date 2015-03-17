<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="row">
	<div class="col-md-12">
		<h3>Indexaufbau</h3>
		<form role="form">
			<div class="form-group">
				<label for="url_input">Gib eine beliebige URL ein...</label> <input
					type="text" class="form-control" id="url_input"
					placeholder="bspw... http://www.spiegel.de/panorama/leute/">
			</div>
			<div class="form-group">
				<label for="typeSelection">Wähle einen Dokumententyp</label> <select
					class="form-control" id="typeSelection">
					<c:forEach var="type" items="${docTypes}">
						<option data-type="${type.key}">${type.value}</option>
					</c:forEach>
				</select>
			</div>
			<button type="submit" id="do_crawl" class="btn btn-default">Index aufbauen</button>
		</form>
	</div>
</div>