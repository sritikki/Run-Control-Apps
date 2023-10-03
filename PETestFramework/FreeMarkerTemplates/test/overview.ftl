<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>PM Test Result</title>
		<link rel="stylesheet" type="text/css" href="${cssPath}">
		<link rel="stylesheet" href="../w3.css"> 
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
			function copyTestName(tn) {
				navigator.clipboard.writeText(tn);
			} 
          </script>
	</head>
	<#assign specHeadColor>w3-dark-gray</#assign>
	<#assign helpColors>w3-dark-gray</#assign>
	<#assign ideasColors>w3-dark-gray</#assign>
	<#macro testAndWrite tag><#if tag[0]??>${tag[0]}</#if></#macro>
	<#macro getColour cat><#if cat.totalNumTests = cat.numPassed>w3-green
						  <#elseif cat.totalNumTests = cat.numUnknown>w3-sand
						  <#else><#if cat.numPassed gt 0 >w3-amber<#else>w3-red</#if>
						  </#if></#macro> 
	<#macro getTestColour test><#switch test.result>
								<#case "pass">w3-green<#break>
								<#case "fail">w3-red<#break>
								<#case "nobase">w3-pale-blue<#break>
								<#case "unexpectedPass">w3-amber<#break>
								<#default>w3-light-grey</#switch></#macro> 
<#-- 	<#macro RTC testResult>boo<#if testResult.RTC[0]??> (${testResult.RTC[0]})</#if></#macro> -->
	<#macro RTC testResult><#if testResult.rtc?length gt 0>
	(<a target="_blank" href="https://swgjazz.ibm.com:8078/ccm/web/projects/Performance%20Engine#action=com.ibm.team.workitem.viewWorkItem&id=${testResult.rtc}">RTC ${testResult.rtc})</a></#if></#macro>
   	<body>
		<!-- Sidebar -->
		<div class="w3-sidebar w3-bar-block w3-light-grey" style="width:25%">
			<div class="w3-container w3-dark-grey">
			<h3>GenevaERS Test Results</h3>
			</div>
			<h4><a class="w3-bar-item w3-button w3-green w3-hover-light-green" href="javascript:toggleDiv(&quot;RunDetails&quot;)">Run Details</a></h4>
			<h4>Categories</h4>
			<#list categories as cat>
			<a class="w3-bar-item w3-button w3-hover-blue <@getColour cat/>" href="javascript:toggleDiv(&quot;${cat.name}&quot;)">${cat.name}
				<span class="w3-badge w3-right">${cat.numPassed} of ${cat.totalNumTests}</span></a>
			</#list>
			<h4>Other Stuff</h4>
			<a class="w3-bar-item w3-button " href="javascript:toggleDiv(&quot;HelpMe&quot;)">Help</a>
			<a class="w3-bar-item w3-button " href="https://swgjazz.ibm.com:8078/ccm/web/projects/Performance%20Engine#action=com.ibm.team.workitem.viewWorkItem&id=21218">Ideas</a>
		</div>
		<!-- Page Content -->
		<div style="margin-left:25%">
		<div class="w3-row-padding" style="margin:0 16px">
			<div class="w3-col s12">
				<div class="w3-card-4" id="RunDetails" style="display: block;">
					<header class="w3-container w3-green">
						<h2>PM Build: ${env["GERS_ENV_HLQ"]}</h2>
					</header>
					<div class="w3-container">
						<h4>Date: ${.now}</h4>
						<h4>
							<#if covAvailable == "Yes">
							Overall Function Code Coverage Available:
							<a href="aggregateCov.html">
								here
							</a>
							<#else>
							Function Code Coverage: Not Available
							</#if>
						</h4>
						<h4>
							<#if env["MESSAGE_COVERAGE"] == "Y">
							Message Coverage Available:
							<a href="messageCoverage.html">
								here
							</a>
							<#else>
							Message Coverage: Not Available
							</#if>
						</h4>
					</div>
					<footer class="w3-container w3-green">
						<h3>High Level Qualifier: ${env["GERS_TEST_HLQ"]}</h3>
					</footer>
			</div>
	<#-- Show/Hide toggle Divs for each category -->
			<#list categories as cat>
			<div class="w3-card-4 w3-margin-top <@getColour cat/>" id="${cat.name}" style="display: none;">
				<header class="<@getColour cat/>"><h2><a class="w3-button" href="javascript:toggleDiv(&quot;${cat.name}&quot;)"><i class="fa fa-close"></i> ${cat.name}</a></h2></header>
				<#list cat.specGroups as specGroup>
				<div class="w3-card-4 w3-margin w3-sand">
					<div class="w3-row-padding" style="margin:0 16px">
						<header class="${specHeadColor}">
							<h4><a class="w3-button" href="javascript:toggleDiv(&quot;${specGroup.specResults[0].fileName}&quot;)"><i class="fa fa-close"></i> Test Spec ${specGroup.specResults[0].fileName} - ${specGroup.specResults[0].description}</a>
								<span class="w3-right w3-margin-right">
								<a class="w3-hover-blue" style="text-decoration: none" href="${specGroup.specResults[0].htmlPath}">Details</a>
								</span>
							</h4>
						</header>
						<div id="${specGroup.specResults[0].fileName}" style="display: none;">
						<#list specGroup.specResults as specResult>
							<#list specResult.testResults as testResult>
								<div class="w3-card <@getTestColour testResult/>">
									<h4>
									Test: ${testResult.name} <@RTC testResult/> - ${testResult.description} 
									<#if testResult.coveragePath?length gt 0>
									<span class="w3-right w3-margin-right"><a class="w3-hover-blue" style="text-decoration: none" href="${testResult.coveragePath}">Coverage</a></span>
									</#if>
									<#if testResult.vdpFlowPath?length gt 0>
									<span class="w3-right w3-margin-right"><a class="w3-hover-blue" style="text-decoration: none" href="${testResult.vdpFlowPath}">VDPFlow</a></span>
									</#if>
									<span class="w3-right w3-margin-right">
									<a class="w3-margin w3-border w3-pale-blue w3-hover-blue" style="text-decoration: none" href="${testResult.absPath}">Result: ${testResult.result}</a>
									</span>
									</h4>
							    </div>
							</#list>
						</#list>
						</div>
					</div>
				</div>
				</#list>
			</div>
			</#list>
			<#include "help.ftl">
		</div>
		</div>
   	</body>
</html>