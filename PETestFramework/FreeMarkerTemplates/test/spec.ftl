<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>PM Test Result</title>
		<link rel="stylesheet" type="text/css" href="${cssPath}">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
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
	<#assign SpecHeaderColour>w3-indigo</#assign>
	<#assign TestHeaderColour>w3-blue</#assign>
	<#assign TestFileColour>w3-light-blue</#assign>
	<#assign TestFileDetailColour>w3-pale-blue</#assign>
	<#macro testAndWrite tag="">${tag}</#macro>
   	<body>
		<div class="w3-card w3-margin">
			<header class="w3-container ${SpecHeaderColour}">
				<h2>${pmspec.name} - ${pmspec.title} <span class="w3-right">${.now}</span></h2>
			</header>
		   	<h3 class="w3-margin">${pmspec.description}</h3>
		   	<h4 class="w3-margin-left">Category: ${pmspec.category}</h4>
		   	<#-- RTC number stuff? -->
		   	<h4 class="w3-margin-left">Number of tests: ${pmspec.tests?size}</h4>

 	   	<#list pmspec.tests as test>
			<div class="w3-card w3-margin" >	
				<header class="w3-container ${TestHeaderColour}">
		   			<h3>${test.name} - ${test.description} 
		   			    <a href="javascript:toggleDiv(&quot;${test.name}&quot;)"><i class="fa fa-angle-down"></i></a>
		   			</h3>
				</header>
				<div id="${test.name}" style="display: none;">
		   		<h4 class="w3-margin">Source: ${test.source} see the <a href="${test.name}/view.html"> view details</a></h4>
				<div class="w3-card w3-margin">
					<header class="w3-container ${TestFileColour}">
			   			<h3>Input Files
			   				
			   			</h3>
					</header>
					<div class="w3-card w3-margin">
						<header class="w3-container ${TestFileDetailColour}">
				   			<h3>Event Files <span class="w3-badge">${test.eventfiles?size}</span>
				   			    <a href="javascript:toggleDiv(&quot;${test.name}.Event&quot;)"><i class="fa fa-angle-down"></i></a>
				   			</h3>
						</header>
						<div id="${test.name}.Event" style="display: none;">
	   					<table class="w3-table-all" >
		   				<tr>
		   					<th>ddname</td>
		   					<th>Detail</th>
		   				</tr>
		   				<#list test.eventfiles as eventFile>
		   				<tr >
		   					<td >${eventFile.ddname}</td>
		   					<#if eventFile.filename??>
		   					<td >${eventFile.filename}</td>
		   					<#elseif eventFile.dsn??>
		   					<td >${eventFile.dsn}</td>
		   					</#if>
		   				</tr>
		   				</#list>
	   					</table>
	   					</div>
					</div>
					<div class="w3-card w3-margin">
						<header class="w3-container ${TestFileDetailColour}">
				   			<h3>Reference Files <span class="w3-badge">${test.reffiles?size}</span>
				   			    <a href="javascript:toggleDiv(&quot;${test.name}.Refs&quot;)"><i class="fa fa-angle-down"></i></a>
							</h3>
						</header>
						<div id="${test.name}.Refs" style="display: none;">
	   					<table class="w3-table-all" >
		   				<tr>
		   					<th>ddname</td>
		   					<th>Detail</th>
		   				</tr>
		   				<#list test.reffiles as refFile>
		   				<tr >
		   					<td >${refFile.ddname}</td>
		   					<#if refFile.filename??>
		   					<td >${refFile.filename}</td>
		   					<#elseif refFile.dsn??>
		   					<td >${refFile.dsn}</td>
		   					</#if>
		   				</tr>
		   				</#list>
	   					</table>
	   					</div>
					</div>
				</div>
				<div class="w3-card w3-margin">
					<header class="w3-container ${TestFileColour}">
			   			<h3>Output Files</h3>
					</header>
					<div class="w3-card w3-margin">
						<header class="w3-container ${TestFileDetailColour}">
				   			<h3>Extract Files <span class="w3-badge">${test.extractfiles?size}</span>
				   			<a href="javascript:toggleDiv(&quot;${test.name}.Extract&quot;)"><i class="fa fa-angle-down"></i></a>
				   			</h3>
						</header>
						<div id="${test.name}.Extract" style="display: none;">
	   					<table class="w3-table-all">
			   				<tr>
								<th >DD Name</th>
								<th >Base dsn</th>
								<th >dsn</th>
								<th >Space</th>       
								<th >Primary</th>       
								<th >Secondary</th>       
								<th >Record Format</th>       
								<th >Length Record</th>       
								<th >start</th>       
								<th >stop</th>
							</tr>       
				   			<#list test.extractfiles as extractFile>
			   				<tr >
			   					<td ><@testAndWrite extractFile.ddname/></td>
			   					<td ><@testAndWrite extractFile.basedsn/></td>
			   					<td ><@testAndWrite extractFile.dsn/></td>
			   					<td ><@testAndWrite extractFile.space/></td>
			   					<td ><@testAndWrite extractFile.primary/></td>
			   					<td ><@testAndWrite extractFile.secondary/></td>
			   					<td ><@testAndWrite extractFile.recfm/></td>
			   					<td ><@testAndWrite extractFile.lrecl/></td>
			   					<td ><@testAndWrite extractFile.start/></td>
			   					<td ><@testAndWrite extractFile.stop/></td>
			   				</tr>
				   			</#list>
			   			</table>
						</div>				
					</div>
					<div class="w3-card w3-margin">
						<header class="w3-container ${TestFileDetailColour}">
				   			<h3>Format Files <span class="w3-badge">${test.formatfiles?size}</span>
				   			<a href="javascript:toggleDiv(&quot;${test.name}.Format&quot;)"><i class="fa fa-angle-down"></i></a>
				   			</h3>
						</header>
						<div id="${test.name}.Format" style="display: none;">
			   			<table class="w3-table-all">
			   				<tr>
								<th >DD Name</th>
								<th >Base dsn</th>
								<th >dsn</th>
								<th >Space</th>       
								<th >Primary</th>       
								<th >Secondary</th>       
								<th >Record Format</th>       
								<th >Length Record</th>       
								<th >Work File</th>       
								<th >start</th>       
								<th >stop</th>       
			   				</tr>
				   			<#list test.formatfiles as formatFile>
			   				<tr >
			   					<td ><@testAndWrite formatFile.ddname/></td>
			   					<td ><@testAndWrite formatFile.basedsn/></td>
			   					<td ><@testAndWrite formatFile.dsn/></td>
			   					<td ><@testAndWrite formatFile.space/></td>
			   					<td ><@testAndWrite formatFile.primary/></td>
			   					<td ><@testAndWrite formatFile.secondary/></td>
			   					<td ><@testAndWrite formatFile.recfm/></td>
			   					<td ><@testAndWrite formatFile.lrecl/></td>
			   					<td ><@testAndWrite formatFile.workfile/></td>
			   					<td ><@testAndWrite formatFile.start/></td>
			   					<td ><@testAndWrite formatFile.stop/></td>
			   				</tr>
				   			</#list>
			   			</table>
			   			</div>
					</div>
				</div>
				</div>
		   	</div>
<#--
	   		<div>
	   			<h2>Expected Result</h2>
	   			<h3>
	   				<#if test.Result[0].Success[0]??>
   					Success
	   				<#elseif test.Result[0].BadRC[0]??>
   					Return Code: ${test.Result[0].BadRC[0].RC[0]}
	   				</#if>
	   			</h3> -->
	   	</#list>
   		</div>
   	</body>
</html>