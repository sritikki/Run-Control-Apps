<#include "../common/ParamGeneration.ftl"/>
*HIDE_GENERATION=N
*HIDE_LKLR_VALUES = N
*HIDE_WRITE_SUFFIX = N
*REPLACE_0_POS = N
<#if env["GENERATE_COVERAGE"]?matches("Y")>
[DEFAULT]
COVERAGE=Y
</#if>