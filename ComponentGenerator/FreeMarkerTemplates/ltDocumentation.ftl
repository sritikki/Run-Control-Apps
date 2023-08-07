<#ftl strip_whitespace="true">
<#assign prefixRecord = prefix?eval_json> 
<#assign record = currentRecord?eval_json>
<#-- Get the data type declaration -->
<#assign pos = 1>
<#macro newposition t l>
${pos}<#if t == "string"><#assign pos = pos + l><#elseif t == "integer" || t = "enum"><#assign pos = pos + 4>
<#elseif t == "short"><#assign pos = pos + 2>
<#elseif t == "byte"><#assign pos = pos + 1>
<#elseif t == "boolean"><#assign pos = pos + 1>
<#elseif t == "array">F
</#if></#macro>

<#macro length t l>
<#if t == "string"> ${l} <#elseif t == "integer" || t = "enum"> 4 <#elseif t == "short"> 2 
<#elseif t == "byte"> 1 
<#elseif t == "boolean"> 1 
<#elseif t == "array">F
</#if></#macro>

<#macro datatype r t l>
${r} <#if t == "string">CL${l?c}
<#elseif t == "integer" || t = "enum">F
<#elseif t == "short">H
<#elseif t == "byte">X
<#elseif t == "boolean">C
<#elseif t == "array">F
</#if></#macro>
<!---
This file was automatically generated. 

Do not edit.
--->
# LT Record Type: ${record.recordType} 
<table>
<td>Field</td>
<td>Type</td>
<td>Position</td>
<td>Length</td>
<td>Notes</td>
<#list prefixRecord.fields as field>
    <#list field?keys as f>
        <#if !f?contains("prefix")>
        <tr>
            <td>${f}</td>
            <td>${field[f].type}</td>
            <td><@newposition t=field[f].type l=field[f].maxlength!0 /></td>
            <td><@length t=field[f].type l=field[f].maxlength!0/></td>
            <td>${field[f].note!" "}</td>
        </tr>
        </#if>
    </#list> 
</#list>
<#list record.fields as field>
    <#list field?keys as f>
        <#if !f?contains("prefix")>
        <tr>
            <td>${f}</td>
            <td>${field[f].type}</td>
            <td><@newposition t=field[f].type l=field[f].maxlength!0 /></td>
            <td><@length t=field[f].type l=field[f].maxlength!0/></td>
            <td>${field[f].note!" "}</td>
        </tr>
        </#if>
    </#list> 
</#list>
</table>
