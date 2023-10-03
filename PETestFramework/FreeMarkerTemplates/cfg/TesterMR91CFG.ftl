# Test: ${test}
# Input
<#if source?matches("DB")>
INPUT_TYPE=DB2
DB2_ENVIRONMENT_ID=${EnvironmenID}
DB2_SCHEMA=${Schema}
<#elseif source?matches("WBXML")>
INPUT_TYPE=WBXML
<#else>
INPUT_TYPE=VDPXML
</#if>

#Outputs
OUTPUT_RUN_CONTROL_FILES=Y 
TRACE=Y 
<#if GenerateXML??>
OUTPUT_WB_XML_FILES=${GenerateXML}
</#if>
[Default]
<#if source?matches("DB")>
DB2_CONNECTION=Driver={IBM DB2 ODBC SAFR WE DRIVER};Database=DM12 ;Hostname=SP13.svl.ibm.com;Port=5033;Protocol=TCPIP;UID=${User};PWD=${Pass};
</#if>
