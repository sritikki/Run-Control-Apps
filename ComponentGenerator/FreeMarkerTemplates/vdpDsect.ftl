<#ftl strip_whitespace="true">
*
* This file was automatically generated. Do not edit.
*
<#assign recType = "VDP${record.recordId?c?left_pad(4, '0')}">
${recType}_${record.dsectName}_RECORD  DSECT

<#list entries.dsectEntries as dsectEntry>
${recType}_${dsectEntry}
</#list>
