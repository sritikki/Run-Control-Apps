# Test: ${test.name}
# Input
<#if test.source?matches("DB")>
INPUT_TYPE=DB2
<#--IN_DB.ENVIRONMENT_ID=${test.dBDetails.EnvironmenID}
IN_DB.USER_ID=ADMIN
IN_DB.DB2_SUBSYSTEM=${test.DBDetails.Subsystem}
IN_DB.SCHEMA=${test.DBDetails.Database} -->
<#elseif test.source?matches("WBXML")>
INPUT_TYPE=WBXML
<#else>
INPUT_TYPE=VDPXML
</#if>

#Outputs
OUTPUT_RUN_CONTROL_FILES=Y 
TRACE=Y       

#All environment vars...
HLQ=${env["GERS_TEST_HLQ"]}