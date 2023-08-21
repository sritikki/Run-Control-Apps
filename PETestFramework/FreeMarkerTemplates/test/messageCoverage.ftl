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
	<#macro testAndWrite tag><#if tag[0]??>${tag[0]}</#if></#macro>
   	<body>
		<h1>Test Framework Message Coverage</h1>
        <div>
            <h2>
                PM Build: ${env["GERS_ENV_HLQ"]}
            </h2>
            <h2>
                High Level Qualifier: ${env["GERS_TEST_HLQ"]}
            </h2>
            <h2>
                Date: ${.now}
            </h2>
        </div>
        <h1>Message Types</h1>
		<table style="border-style: solid; border-width: 1px; border-collapse:collapse; margin-top: 10px;">
			<tr style="border-style: solid; border-width: 1px;padding: 3;">
				<th style="border-style: solid; border-width: 1px;padding: 3;">Type</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">Number of Messages</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">Hits</th>
			</tr>      
 	   	<#list coverage["levels/level"] as level>
 	   	<#assign hits = level["count(Messages/entry/hits[. > 0])"]>
 	   	<#assign entries = level["count(Messages/entry)"]>
  			<tr class="<#if entries = hits>pass<#else><#if hits gt 0 >some<#else>fail</#if></#if>">
				<td class="spec"><a href="javascript:toggleDiv(&quot;${level.name[0]}&quot;)">${level.name[0]}</a></td>
				<td class="spec">${entries}</td>
				<td class="spec">${hits}</td>
			</tr>
 	   	</#list>
		</table> 
<#-- Show/Hide toggle Divs for each level -->
			<#list coverage["levels/level"] as level>
			<div id="${level.name[0]}" style="display: none;">
				<h1><a href="javascript:toggleDiv(&quot;${level.name[0]}&quot;)">${level.name[0]}</a></h1>
				<table style="border-style: solid; border-width: 1px; border-collapse:collapse; margin-top: 10px;">
					<tr style="border-style: solid; border-width: 1px;padding: 3;">
						<th style="border-style: solid; border-width: 1px;padding: 3;">Code</th>
						<th style="border-style: solid; border-width: 1px;padding: 3;">Message</th>
						<th style="border-style: solid; border-width: 1px;padding: 3;">Hits</th>
					</tr>      
				<#list level["Messages/entry"] as entry>
 					<tr class="<#if entry.hits[0] != "0" >pass<#else>fail</#if>" style="border-style: solid; border-width: 1px;padding: 3;">
						<td  style="border-style: solid; border-width: 1px;padding: 3;">
							${entry.code[0]}
						</td>
						<td  style="border-style: solid; border-width: 1px;padding: 3;">
							${entry.message[0]}
						</td>
						<td  style="border-style: solid; border-width: 1px;padding: 3;">
							${entry.hits[0]}
						</td>
					</tr>       
				</#list>
				</table>
			</div>
   			</#list>
   </body>
</html>