<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>PM Test Result</title>
		<link rel="stylesheet" type="text/css" href="${cssPath}">
		<script language="JavaScript">
						function toggleDiv(divname) {
							var ele = document.getElementById(divname);
							if (ele.style.display == "none") {
								ele.style.display = "block";
							}
							else {
								ele.style.display = "none";
							}
						}  
          </script>
	</head>
	<#macro getTestColour result><#switch result>
								<#case "SUCCESS">w3-green<#break>
								<#case "FAILCOMPARE">w3-red<#break>
								<#case "FAILJES">w3-red<#break>
								<#case "unexpectedPass">w3-amber<#break>
								<#default>w3-light-grey</#switch></#macro> 
   	<body>
		<div class="w3-card-4" id="RunDetails" style="display: block;">
			<header class="w3-container <@getTestColour result/>">
				<h2>Test ${testName} from spec ${specName}</h2>
			</header>
			<div class="w3-container">
				<h4>Date: ${.now}</h4>
			</div>
			<footer class="w3-container w3-green">
				<h3>Result ${result}</h3>
			</footer>
			<#if result == "FAILCOMPARE">
			<div class="w3-card-4" id="RunDetails" style="display: block;">
				<header class="w3-container <@getTestColour result/>">
					<h2>Differences</h2>
				</header>
	      		<#list outFiles as outFile>
	      		    <#if outFile.comparable == "Y">
	      				<h2><a href="javascript:toggleDiv(&quot;Diff${outFile.ddname}&quot;)">
	                            ${outFile.ddname}
	                        </a></h2>
	                    <#--  <div id="Diff${outFile.ddname}" style="display: none;">
	      					<pre><p class="comp">${outFile.diffText}</p></pre>
	      				</div>  -->
	      			</#if>
	      		</#list>
	  		</div>
	
			<div class="w3-card-4" id="RunDetails" style="display: block;">
				<header class="w3-container <@getTestColour result/>">
					<h2>Base Files</h2>
				</header>
	      		<#list outFiles as outFile>
	      		    <#if outFile.comparable == "Y">
	      				<h2><a href="javascript:toggleDiv(&quot;Base${outFile.ddname}&quot;)">
	                            ${outFile.ddname}
	                        </a></h2>
	                    <#--  <div id="Base${outFile.ddname}" style="display: none;">
	      					<pre><p class="comp">${outFile.baseTextContent}</p></pre>
	      				</div>  -->
	      			</#if>
	      		</#list>
			</div>
			</#if>
			
			<div class="w3-card-4" id="RunDetails" style="display: block;">
				<header class="w3-container <@getTestColour result/>">
					<h2>Comparable OutFiles</h2>
				</header>
		  		<#list outFiles as outFile>
		  		    <#if outFile.comparable == "Y">
		  				<h2>
		  					<a href="javascript:toggleDiv(&quot;${outFile.ddname}&quot;)">
		                        ${outFile.ddname}
		                    </a>
		                </h2>
		                <#--  <div id="${outFile.ddname}" style="display: none;">
		  					<pre><p class="comp">${outFile.textContent}</p></pre>
		  				</div>  -->
		  			</#if>
		  		</#list>
			</div>
		</div>		
   </body>
</html>