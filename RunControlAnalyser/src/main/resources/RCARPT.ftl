<#assign aDateTime = .now>
<#assign aDate = aDateTime?date>
<#assign aTime = aDateTime?time>
~RCARPT
 
GenevaERS - The Single-Pass Optimization Engine
(https://genevaers.org)
Licensed under the Apache License, Version 2.0
Performance Engine for z/OS - Base Product
Release PM PM4.18.00 D
 
Program ID:      GVBRCG
Program Title:   Run-Control Analyser
Built:           ${rcaversion}
 
Executed:        ${aDate} : ${aTime}
 
Report DD Name:  RCARPT
Report Title:    GVBRCA Control Report
 
 
================
Report Sections:
================
 
    Tag    Section name
    -----  ------------------------------------------------------
    ~PARM  Contents of RCAPARM file
    ~OPTS  Options in effect
    ~IFIL  Input files
    ~OFIL  Output files
    ~EXEC  Execution summary
 
 
==================================
~PARM - Contents of RCAPARM file:
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
 

====================
~IFIL - Input files:
====================
 
DD Name   Member    Create Date/Time   
========  --------  -------------------
<#-- <#list inputReports as ir>
${ir.ddName?right_pad(8)}  ${ir.memberName}  ${ir.generationID!0} 
</#list> -->
 
 
=====================
~OFIL - Output files:
=====================
 
DD Name   Member    Record Count
========  ========  ------------
VDP                 ${vdpRecordsWritten!0?left_pad(12)}
JLT                 ${jltRecordsWritten!0?left_pad(12)}
XLT                 ${xltRecordsWritten!0?left_pad(12)}
 
==========================
~EXEC - Execution summary:
==========================
 
VDP difference count = ${numVDPDiffs}
XLT difference count = ${numXLTDiffs}
JLT difference count = ${numJLTDiffs}

Process completed successfully
