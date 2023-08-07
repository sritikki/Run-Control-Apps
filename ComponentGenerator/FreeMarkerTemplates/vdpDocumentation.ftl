<#ftl strip_whitespace="true">
<#-- Get the data type declaration -->
<#assign pos = 1>
<#macro newposition t l>
${pos}<#if t == "String"><#assign pos = pos + l><#elseif t == "int" || t?contains("enum")><#assign pos = pos + 4>
<#elseif t == "short"><#assign pos = pos + 2>
<#elseif t == "byte"><#assign pos = pos + 1>
<#elseif t == "boolean"><#assign pos = pos + 1>
<#elseif t == "array">F
</#if></#macro>

<#macro length t l>
<#if t == "String"> ${l} <#elseif t == "int" || t?contains("enum")> 4 <#elseif t == "short"> 2 
<#elseif t == "byte"> 1 
<#elseif t == "boolean"> 1 
<#elseif t == "array">F
</#if></#macro>

<#macro datatype r t l>
${r} <#if t == "String">CL${l?c}
<#elseif t == "int" || t?contains("enum")>F
<#elseif t == "short">H
<#elseif t == "byte">X
<#elseif t == "boolean">C
<#elseif t == "array">F
</#if></#macro>
<!---
This file was automatically generated. 

Do not edit.
--->
# ${record} Record ID: ${recordID?c?left_pad(4, "0")} 
<table>
<td>Field</td>
<td>Type</td>
<td>Position</td>
<td>Length</td>
<td>Notes</td>
<#list prefix as f>
        <#if !f?contains("prefix")>
        <tr>
            <td>${f.name}</td>
            <td>${f.type}</td>
            <td><@newposition t=f.type l=f.maxLength /></td>
            <td><@length t=f.type l=f.maxLength/></td>
            <td>${f.note!" "}</td>
        </tr>
        </#if>
</#list>
<#list fields as f>
        <#if !f?contains("prefix")>
        <tr>
            <td>${f.name}</td>
            <td>${f.type}</td>
            <td><@newposition t=f.type l=f.maxLength /></td>
            <td><@length t=f.type l=f.maxLength/></td>
            <td>${f.note!" "}</td>
        </tr>
        </#if>
</#list>
</table>
