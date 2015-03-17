<nav class="navbar navbar-default">
  <div class="container-fluid">
    
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">iCrawler by matana</a>
    </div>

    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
     
      <form class="navbar-form navbar-left" role="search">
        
        <div class="form-group">
          <input type="text" id="searchPhrase" class="form-control" placeholder="Suchen">
        </div>
        
        <div class="form-group">
			<select class="form-control" id="topicSearch">
				<option data-topic="1">Überall</option>
				<option data-topic="2">Panorama</option>
				<option data-topic="3">Politik</option>
				<option data-topic="4">Kultur</option>
				<option data-topic="5">Wirtschaft</option>
				<option data-topic="6">Netzwelt</option>
				<option data-topic="7">Wissenschaft</option>
				<option data-topic="8">Sport</option>
			</select>
		</div>
					
        <button type="submit" id="do_search" class="btn btn-default">Suchen</button>
      </form>
      
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#">Login</a></li>

      </ul>
    </div>
  </div>
</nav>