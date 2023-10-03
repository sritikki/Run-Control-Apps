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
	<#assign HeaderColour>w3-teal</#assign>
	<#assign ViewsColour>w3-green</#assign>
	<#assign ViewColour>w3-pale-green</#assign>
	<#macro testAndWrite tag><#if tag[0]??>${tag[0]}</#if></#macro>
	<#macro columnID viewid>${view.safrxml["View-Column/Record[VIEWID='${viewid}']/VIEWCOLUMNID"]}</#macro>
	<#macro header   viewid>${view.safrxml["View-Column/Record[VIEWID='${viewid}']/HDRLINE1"]}</#macro>
	<#macro colLogic colid srcid>${view.safrxml["View-Column-Source/Record[VIEWCOLUMNID='${colid}' and VIEWSOURCEID='${srcid}']/EXTRACTCALCLOGIC"]}</#macro>
   	<body>
		<header class="${HeaderColour}">
			<h3>WBXML ${view.safrxml.Generation.Record.FILENAME[0]}</h3>
		</header>
		<h3>Generated on ${view.safrxml.Generation.Record.CREATEDTIMESTAMP[0]} type ${view.safrxml.Generation.Record.TYPE[0]} from ${view.safrxml.Generation.Record.PROGRAM[0]}</h3>
		<div class="w3-margin-bottom">
			<header class="${ViewsColour}">
				<h4 class="w3-margin-left">The file contains ${view.safrxml["View/Record"]?size} views</h4>
			</header>
		
   			<#list view.safrxml["View/Record"] as vw>
			<div class="w3-margin-bottom">
				<header class="${ViewColour}">
					<h5 ><a class="w3-button w3-hover-teal" href="javascript:toggleDiv(&quot;${vw.NAME[0]}&quot;)">View ${vw.NAME[0]} From Env:${vw.ENVIRONID[0]} ID:${vw.VIEWID[0]}</a></h5>
				</header>
				<div id="${vw.NAME[0]}" style="display: none; overflow-x: auto">
 	   			<table class="w3-table-all">
	   				<tbody>
	   				<tr>
	    		   		<th class="spec">Header</th>
		   			<#list view.safrxml["View-Column/Record[VIEWID='${vw.VIEWID[0]}']"] as column>
		   				<td class="spec">${column.HDRLINE1[0]}</td> 
		   			</#list>
	    		   	</tr>
	   				<tr>
		           		<th class="spec">Column</th>
		   			<#list view.safrxml["View-Column/Record[VIEWID='${vw.VIEWID[0]}']"] as column>
		   				<td class="spec">${column.VIEWCOLUMNID[0]}</td>
		   			</#list>
	    		   	</tr>
	    		   	<#-- There can be many view sources -->
		   			<#list view.safrxml["View-Source/Record[VIEWID='${vw.VIEWID[0]}']"] as source>
	   				<tr>
	    		   		<th class="spec">Logic</th>
 		   			<#list view.safrxml["View-Column/Record[VIEWID='${vw.VIEWID[0]}']"] as column>
		   					<td class="spec"><@colLogic column.VIEWCOLUMNID[0] source.VIEWSOURCEID[0] /></td>
		   			</#list>
	    		   	</tr>
		   			</#list>
	   				<tr>
		           		<th class="spec">Target Type</th>
		   			<#list view.safrxml["View-Column/Record[VIEWID='${vw.VIEWID[0]}']"] as column>
		   				<td class="spec">${column.FLDFMTCD[0]} signed ${column.SIGNEDIND[0]}</td>
		   			</#list>
	    		   	</tr>
	   				<tr>
		           		<th class="spec">Pos and Len</th>
		   			<#list view.safrxml["View-Column/Record[VIEWID='${vw.VIEWID[0]}']"] as column>
		   				<td class="spec">${column.STARTPOSITION[0]} ${column.MAXLEN[0]}</td>
		   			</#list>
	    		   	</tr>
	   				<tr>
		           		<th class="spec">Content Code</th>
		   			<#list view.safrxml["View-Column/Record[VIEWID='${vw.VIEWID[0]}']"] as column>
		   				<td class="spec">${column.FLDCONTENTCD[0]}</td>
		   			</#list>
	    		   	</tr>
		   			</tbody>
	   			</table>
	   			</div>
	   		</div>
	   		</#list>
   		</div>
	</body>
</html>