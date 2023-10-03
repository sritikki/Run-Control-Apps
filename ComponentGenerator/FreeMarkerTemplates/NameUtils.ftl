<#macro snakeToCamelCase snake>
    <#assign cc="">
    <#if snake?contains("_")>
        <#list snake?lower_case?split("_") as part>
            <#if part?is_first>
                <#assign cc = cc + part>
            <#else>
                <#assign cc = cc + part?cap_first>
            </#if>
        </#list>
    <#else>
        <#assign cc = snake?uncap_first>
    </#if>
${cc}</#macro>

<#macro snakeToCapCase snake>
    <#assign cc="">
    <#if snake?contains("_")>
        <#list snake?lower_case?split("_") as part>
            <#assign cc = cc + part?cap_first>
        </#list>
    <#else>
        <#assign cc = snake?cap_first>
    </#if>
${cc}</#macro>