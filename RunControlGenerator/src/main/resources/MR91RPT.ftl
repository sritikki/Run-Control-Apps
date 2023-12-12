<#assign aDateTime = .now>
<#assign aDate = aDateTime?date>
<#assign aTime = aDateTime?time>
~MR91RPT
 
GenevaERS Reporting
-----------------------------
Performance Engine for z/OS - Base Product
Release PM PM4.18.00 D
 
Program ID:      GVBMR91
Program Title:   Run-Control File Generator
Built:           <Build Date>
 
Executed:        ${aDate} : ${aTime}
 
Report DD Name:  MR91RPT
Report Title:    GVBMR91 Control Report
 
 
================
Report Sections:
================
 
    Tag    Section name
    -----  ------------------------------------------------------
    ~PARM  Contents of MR91PARM file
    ~OPTS  Options in effect
    ~WXML  Contents of WBXMLS file
    ~VXML  Contents of VDPXMLS file
    ~DFOL  Contents of DBFLDRS file
    ~RUNV  Contents of RUNVIEWS file
    ~DVWS  Contents of DBVIEWS file
    ~OINI  Contents of DSNAOINI file
    ~RUNV  Contents of RUNVIEWS file
    ~VIEW  Views selected
    ~IFIL  Input files
    ~OFIL  Output files
    ~REFW  Reference Work Files
    ~WRNS  SAFR compiler warnings
    ~ERRS  SAFR compiler errors
    ~EXEC  Execution summary
 
 
==================================
~PARM - Contents of MR91PARM file:
==================================

<#list parmsRead as parm>
${parm}
</#list>
 
 
==========================
~OPTS - Options in effect:
==========================
 
<#list optsInEffect as opt>
${opt}
</#list>
 
================================
~WXML - Contents of WBXMLS file:
================================
 
<none>
 
 
==================================
~RUNV - Contents of RUNVIEWS file:
==================================
 
<none>
 
 
=======================
~VIEW - Views selected:
=======================
 
                                                           Output              >Output         ERA  ERA Buf Size  FRA
View ID  View Name                                         Phase               >Format         On   (in Records)  On
=======  ------------------------------------------------  ---------           >-------------  ---  ------------  ---
<#list views as v>
${v.IDStr}  ${v.name?right_pad(48)}  ${v.phase?right_pad(9)}           >${v.outputFormat?right_pad(13)}  ${v.ERAon?right_pad(3)}  ${v.ERAsize?left_pad(12)}  ${v.FRAon}
</#list>
 
 
====================
~IFIL - Input files:
====================
 
DD Name   Member    Create Date/Time   
========  --------  -------------------
<#list inputReports as ir>
${ir.ddName?right_pad(8)}  ${ir.memberName}  ${ir.generationID} 
</#list>
 
 
=====================
~OFIL - Output files:
=====================
 
DD Name   Member    Record Count
========  ========  ------------
VDP                 ${vdpRecordsWritten?left_pad(12)}
JLT                 ${jltRecordsWritten?left_pad(12)}
XLT                 ${xltRecordsWritten?left_pad(12)}
 
 
==================================
~REFW - Reference-phase Work Files
==================================
 
Ref Work  Runtime                                            Ref       Ref     >Ref                                               Ref      Ref      Key  St  En 
DD Name   View ID  View Name                                 DD Name   PF ID   >PF Name                                           LR ID    LF ID    Len  Dt  Dt
========  -------  ----------------------------------------  --------  ------- >------------------------------------------------  -------  -------  ---  --  --
<#list refviews as r>
${r.workDDName}   ${r.viewID?c}  ${r.viewName?right_pad(40)}  ${r.refDDName?right_pad(8)}  ${r.refPFID?c?left_pad(7)} >${r.refPFName?right_pad(48)}  ${r.refLRID?c?left_pad(7)}  ${r.refLFID?c?left_pad(7)}  ${r.keylen?c?left_pad(3)}  ${r.effStart}  ${r.effEnd}
</#list>
<#if reh??>
${reh.outputFile.outputDDName}   ${reh.ID?c}  ${reh.name?right_pad(40)}
</#if>
<#if rth??>
${rth.outputFile.outputDDName}   ${rth.ID?c}  ${rth.name?right_pad(40)}
</#if>
 
===============================
~WRNS - SAFR compiler warnings:
===============================
 
<none>
 
 
=============================
~ERRS - SAFR compiler errors:
=============================
 
<none>
 
 
==========================
~EXEC - Execution summary:
==========================
 
Number of compiler warnings:               0
Number of compiler errors:                 0
Number of reference-phase views: ${numrefviews?c?left_pad(11)}
Number of extract-phase views:   ${numextviews?c?left_pad(11)}
 
Process completed successfully
