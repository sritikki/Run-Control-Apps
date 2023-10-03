			<div class="w3-card-4 w3-margin-top " id="HelpMe" style="display: none;">
				<header class="w3-purple"><h2 class="w3-margin-left">So what's this test framework thing all about?</h2></header>
				<#list QandA as qa>
 				<div class="w3-card-4 w3-margin-top " >
					<header><h3><a class="w3-button" href="javascript:toggleDiv(&quot;Q${qa?index}&quot;)">${qa.question} <i class="fa fa-angle-down"></i></a></h3> </header>
					<h4 id="Q${qa?index}" class="w3-margin-left" style="display: none;">${qa.answer}</h4>
				</div>
				</#list>  
			</div>
	