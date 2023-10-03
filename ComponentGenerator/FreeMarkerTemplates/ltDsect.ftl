<#ftl strip_whitespace="true">
*
* This file was automatically generated. Do not edit.
*
<#assign LTrec = "LT${record.recordType}">
${LTrec}_REC  DSECT

<#list entries.dsectEntries as dsectEntry>
<#if dsectEntry?starts_with("*")>
${dsectEntry}
<#else>
${LTrec}_${dsectEntry}
</#if>
</#list>
