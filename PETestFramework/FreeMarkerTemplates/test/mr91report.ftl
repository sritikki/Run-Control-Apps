<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>MR91 Test Report</title>
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
		<h1>MR91 Test Report</h1>
        <h1>Comparisons</h1>
		<table style="border-style: solid; border-width: 1px; border-collapse:collapse; margin-top: 10px;">
			<tr style="border-style: solid; border-width: 1px;padding: 3;">
				<th style="border-style: solid; border-width: 1px;padding: 3;">Test Set Name</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">Views</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">Views Generated</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">Target Views</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">View XLT Matches</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">View JLT Matches</th>
			</tr>       
 			<#list testSetResults as testSet>
 			<tr class="<#if testSet.views?size = testSet.numViewsGenerated && testSet.passes?size = testSet.numPassesGenerated>w3-green<#else>w3-red</#if>">
				<td class="w3-teal"><a href="javascript:toggleDiv(&quot;${testSet.name}&quot;)">${testSet.name} ${testSet.comparison}</a></td>
				<td style="text-align: right; border-style: solid; border-width: 1px;padding: 3;">${testSet.views?size}</td>
				<td style="text-align: right; border-style: solid; border-width: 1px;padding: 3;"><a href="javascript:toggleDiv(&quot;${testSet.name}_Views&quot;)">${testSet.numViewsGenerated}</a></td>
				<td class="<#if testSet.numViewsGenerated == testSet.numTargetViewsGenerated>w3-green<#else>w3-red</#if>" style="text-align: right; border-style: solid; border-width: 1px;padding: 3;"><a href="javascript:toggleDiv(&quot;${testSet.name}_Views&quot;)">${testSet.numTargetViewsGenerated}</a></td>
				<td class="<#if testSet.numViewsGenerated == testSet.numXltMatches>w3-green<#else>w3-red</#if>" style="text-align: right; border-style: solid; border-width: 1px;padding: 3;"><a href="javascript:toggleDiv(&quot;${testSet.name}_Views&quot;)">${testSet.numXltMatches}</a></td>
				<td class="<#if testSet.numJlts == testSet.numJltMatches>w3-green<#else>w3-red</#if>" style="text-align: right; border-style: solid; border-width: 1px;padding: 3;"><a href="javascript:toggleDiv(&quot;${testSet.name}_Views&quot;)">${testSet.numJltMatches}/${testSet.numJlts}</a></td>
			</tr> 
   			</#list>
		</table>

		<#list testSetResults as testSet>
       	<div id="${testSet.name}_Views" style="display: none;">
       	    <h2>${testSet.name} Views</h2>
 				<table style="border-style: solid; border-width: 1px; border-collapse:collapse; margin-top: 10px;">
			<tr style="border-style: solid; border-width: 1px;padding: 3;">
				<th style="border-style: solid; border-width: 1px;padding: 3;">Name</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">MR91RPT</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">MR91LOG</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">MR91TRAC</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">Target MR91RPT</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">Target MR91LOG</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">Target MR91TRAC</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">XLT Match</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">JLT Match</th>
				<th style="border-style: solid; border-width: 1px;padding: 3;">Notes</th>
			</tr>       
 			<#list testSet.views as tr>
			<tr class="<#if tr.sourceVDPBuilt = true>w3-green<#else>w3-red</#if>">
				<td class="w3-teal"><a href="${tr.sourcePathName}/WBXMLI/${tr.testFileName}">${tr.testName}</a></td>
				<td style="text-align: right; border-style: solid; border-width: 1px;padding: 3;"><a href="${tr.sourcePathName}/MR91RPT">report</a></td>
				<td style="text-align: right; border-style: solid; border-width: 1px;padding: 3;"><a href="${tr.sourcePathName}/MR91LOG">log</a></td>
				<td class="<#if tr.sourceVDPBuilt = true>w3-green<#else>w3-red</#if>" style="text-align: right; border-style: solid; border-width: 1px;padding: 3;"><a href="${tr.sourcePathName}/MR91TRAC">trace</a></td>
				<td class="<#if tr.targetVDPBuilt = true>w3-green<#else>w3-red</#if>" style="text-align: right; border-style: solid; border-width: 1px;padding: 3;"><a href="${tr.targetPathName}/MR91RPT">report</a></td>
				<td class="<#if tr.targetVDPBuilt = true>w3-green<#else>w3-red</#if>" style="text-align: right; border-style: solid; border-width: 1px;padding: 3;"><a href="${tr.targetPathName}/MR91LOG"> log</a></td>
				<td class="<#if tr.targetVDPBuilt = true>w3-green<#else>w3-red</#if>" style="text-align: right; border-style: solid; border-width: 1px;padding: 3;"><a href="${tr.targetPathName}/MR91TRAC">trace</a></td>
				<td class="<#if tr.allXLTMatch = true>w3-green<#else><#if tr.note?length gt 0>w3-tooltip w3-purple<#else>w3-red</#if></#if>" style="text-align: right; border-style: solid; border-width: 1px;padding: 3;">
				<#list tr.xltPaths as xlt>
 				<table style="border-style: solid; border-width: 1px; border-collapse:collapse; margin-top: 10px;">
					<tr style="border-style: solid; border-width: 1px;padding: 3;">
						<td class="<#if xlt?contains("pass") = true>w3-green<#else>w3-red</#if>" style="text-align: right; border-style: solid; border-width: 1px;padding: 3;">
						<a href="${xlt}">XLT</a></td>

					</tr>	
				</table>	
				</#list>			
 				<td class="<#if tr.jltPath?contains("jltna")>
 							w3-grey"
 				           <#else>
 								<#if tr.jltMatch = true>w3-green<#else>w3-red</#if>"
 							</#if>
 				    style="text-align: right; border-style: solid; border-width: 1px;padding: 3;">
 				    <#if tr.jltPath?contains("jltna")>
 				       n/a
 				    <#else>
					<a <#if tr.overrideJLTPass = true>class="w3-red w3-margin"</#if> 
						href="<#if tr.jltMatch = true && tr.overrideJLTPass == false>${tr.sourcePathName}/jltpass.txt
						      <#else>${tr.sourcePathName}/jltdiff.html</#if>">
						JLT</a></td>
				    </#if>
				<td class="<#if tr.note?length gt 0>w3-purple</#if>" style="text-align: left; border-style: solid; border-width: 1px;padding: 3;">
				    <#if tr.note?length gt 0>${tr.note}</#if>
					</td>
			</tr>
   			</#list>
		</table>
		</div>
		</#list> 
   </body>
</html>