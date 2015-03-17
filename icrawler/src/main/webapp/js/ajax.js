$(function() {
	addSubmitHandlerCrawl();
	addSubmitHandlerSearch();
	addSubmitHandlerClassification();
});

function addSubmitHandlerClassification() {
	$('#do_classification').bind('click', function(event) {
		var site = $('input#classification_input').val();
		console.log('do_classification clicked: ' + site);
		$.ajax({
			type : 'POST',
			url : 'classify/',
			data : { 'site' : site },
			success: function(response) {
				window.location.href = response;
			},
			error : function(xhr, status, error) {
				console.log("ERROR: " + error); 
			}
		});
		event.preventDefault();
	});
}

function addSubmitHandlerCrawl() {
	$('#do_crawl').bind('click', function(event) {
		var url = $('input#url_input').val();
		var type = $('#typeSelection option:selected').data( "type" );
		var data = {
				'url' : url,
				'type' : type
		}
		loadResultsForURLs(data, false);
		event.preventDefault();
	});
}

function addSubmitHandlerSearch() {
	$('#do_search').bind('click', function(event) {
		var searchPhrase = $('input#searchPhrase').val();
		var topic = $('#topicSearch option:selected').data( "topic" );
		var query = {
				'searchPhrase' : searchPhrase,
				'topic' : topic
		}
		loadResultsForURLs(query, true);
		event.preventDefault();
	});
}

function loadResultsForURLs(data, isQuery) {
	if (notEmpty(data) && length(data)) {
		console.log(data);
		var requestUrl = isQuery ? 'search/' : 'crawl/';
		$.ajax({
			type : 'POST',
			url : requestUrl,
			data : JSON.stringify(data),
			contentType: 'application/json',
			success: function(response) {
				window.location.href = response;
			},
			error : function(xhr, status, error) {
				console.log("ERROR: " + error); 
			}
		});
	}
}

function notEmpty(obj) {
	return obj && obj !== 'null' && obj !== 'undefined';
}
function length(obj) {
	return obj.length > 0 || obj !== '';
}