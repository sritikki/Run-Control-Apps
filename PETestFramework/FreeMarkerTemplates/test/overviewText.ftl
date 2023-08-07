<#-- 	<#macro RTC testResult>boo<#if testResult.RTC[0]??> (${testResult.RTC[0]})</#if></#macro> -->
GenevaERS Test Results

PM Build: ${env["PMHLQ"]}
	Date: ${.now}
	High Level Qualifier: ${env["TEST_HLQ"]}
	
Categories
----------
<#list categories as cat>
${cat.name} Passed: ${cat.numPassed} of ${cat.totalNumTests}
</#list>

Details
-------
<#list categories as cat>
Category ${cat.name}
	<#list cat.specGroups as specGroup>
  Test Spec ${specGroup.specResults[0].fileName} - ${specGroup.specResults[0].description}
		<#list specGroup.specResults as specResult>
			<#list specResult.testResults as testResult>
    Test: ${testResult.name?right_pad(68)} ${testResult.description?right_pad(64)} Result: ${testResult.result}
			</#list>
		</#list>
	</#list>
</#list>
