* Test: ${test.name}
DUMP_LT_AND_GENERATED_CODE=Y
<#if test.rundate??>                                                                         
RUN_DATE=${test.rundate}
</#if>
<#if test.fiscalDateDefault??>                                                                         
FISCAL_DATE_DEFAULT=${test.fiscalDateDefault}
</#if>                                 
<#if test.fiscaldateoverride??>                                                                         
FISCAL_DATE_OVERRIDE=${test.fiscaldateoverride}
</#if>                                 
<#if test.trace??>                                                                         
TRACE=${test.trace}
</#if>
<#if test.EXECUTE_IN_MAIN_TASK??>                                                                         
EXECUTE_IN_MAIN_TASK=${test.EXECUTE_IN_MAIN_TASK}
</#if>