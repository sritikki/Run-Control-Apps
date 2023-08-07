<#-- 	<#macro RTC testResult>boo<#if testResult.RTC[0]??> (${testResult.RTC[0]})</#if></#macro> -->
<#assign aDateTime = .now>
<#assign aDate = aDateTime?date>
<#assign aTime = aDateTime?time>
GenevaERS Test Results

PM Build: ${env["PMHLQ"]}	
Date: ${aDate?iso_utc} ${aTime?iso_utc}
High Level Qualifier: ${env["TEST_HLQ"]}
	
Category,Test Spec,Test,Result
<#list categories as cat>
	<#list cat.specGroups as specGroup>
   		<#list specGroup.specResults as specResult>
			<#list specResult.testResults as testResult>
${cat.name},"${specGroup.specResults[0].fileName} - ${specGroup.specResults[0].description}","${testResult.name} - ${testResult.description}",${testResult.result}
			</#list>
		</#list>
	</#list>
</#list>
