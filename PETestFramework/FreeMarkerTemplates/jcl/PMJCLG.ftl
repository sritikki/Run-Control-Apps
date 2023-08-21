<#macro qualifiedTest>
<#-- To avoid the line break at the end I butted up the closing tag -->
${env["GERS_TEST_HLQ"]}.${test.dataSet}</#macro>
<#assign extFiles = test.extractfiles?size>
<#assign fmtFiles = test.formatfiles?size>
<#if fmtFiles gt 0>
    <#if extFiles gt 0>
    <#list test.extractfiles as ext>
      <#if ext.workfile??>
        <#assign wfNum = ext.workfile?number>
      <#else>
        <#assign wfNum = ext?counter>
      </#if>
//${test.name}G${ext?counter} JOB (ACCT),'REGRESSION JOB',
//            NOTIFY=&SYSUID.,
//            CLASS=A,
//            MSGLEVEL=(1,1),
//            MSGCLASS=H
//*
        <#--<#assign formatQuery = "test.formatfiles.workfile=num"/>-->
        <#assign formatFiltered = test.formatfiles?filter(x -> x.workfile?number=wfNum)/>
//******************************************************************
//* Total Number of extract files ${extFiles}
//* Total Number of format files ${fmtFiles}
//*
//* Comparisons for extract workfile ${wfNum}
//* 
//* That is Format Views:
        <#list formatFiltered as wf>
//*        ${wf.ddname}
        </#list>
        <#list formatFiltered as wf>
        <#if !wf.comparable?? || wf.comparable == "Y">
        <#if wf.dsn??>
          <#assign wfDSN = wf.dsn>
        <#else>
          <#assign wfDSN>
<@qualifiedTest/>.OUTF.MR88.${wf.ddname}</#assign>
        </#if>
        <@compareDSNs wf.ddname wf.basedsn wfDSN wf.compparm/>
        </#if>
        </#list>
    </#list>
    </#if>
<#else>
//${test.name}G$ JOB (ACCT),'REGRESSION JOB',
//            NOTIFY=&SYSUID.,
//            CLASS=A,
//            MSGLEVEL=(1,1),
//            MSGCLASS=H
//*
    <#list test.extractfiles as ext>
        <@compareDSNs ext.ddname ext.basedsn ext.dsn ext.compparm/>
    </#list>
</#if>
<#macro compareDSNs DDName BaseDSN DSN CompParm>
//*
//*********************************************************************
//* ${DDName} - COMPARE ${DDName} FILES
//*********************************************************************
//*
//${DDName} EXEC PGM=ISRSUPC,
//             PARM=(DELTAL,LINECMP)
//*
//OLDDD    DD DSN=${BaseDSN},
//            DISP=SHR
//*
//NEWDD    DD DSN=${DSN},
//            DISP=SHR
//*
//OUTDD    DD SYSOUT=*<#if CompParm?length gt 0>
//* 
//SYSIN    DD *
${CompParm?trim}
<#else>

</#if>
//*
</#macro>
