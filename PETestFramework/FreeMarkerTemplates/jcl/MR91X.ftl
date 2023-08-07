//${test.name}X JOB (ACCT),'REGRESSION JOB',
//            NOTIFY=&SYSUID.,
//            CLASS=A,
//            MSGLEVEL=(1,1),
//            MSGCLASS=H,
//            REGION=0M
//*
//JOBLIB   DD DISP=SHR,DSN=${env["OVERRIDE"]}
//         DD DISP=SHR,DSN=${env["PMLOAD"]}
<#include "../common/generation.ftl"/>
<#macro qualifiedTest>
<#-- To avoid the line break at the end I butted up the closing tag -->
${env["TEST_HLQ"]}.${test.dataSet}</#macro>
//*                                                                      
//*********************************************************************  
//*                                                                      
//********************************************************************** 
//* PSTEP050 - DELETE THE FILE(S) CREATED IN NEXT STEP                   
//*                                                                      
//* NORMAL RETURN CODE = 0                                               
//*                                                                      
//* IF THIS STEP ABENDS:                                                 
//* 1) CORRECT APPLICATION PROBLEM IF POSSIBLE                           
//* 2) RESTART THE JOB IN THIS STEP                                      
//* 3) IF PROBLEM CANNOT BE CORRECTED, CONTACT APPLICATION SUPPORT       
//*                                                                      
//********************************************************************** 
//*                                                                      
//PSTEP050 EXEC PGM=IDCAMS                                               
//*                                                                      
//SYSPRINT DD SYSOUT=*                                                   
//*                                                                      
//SYSIN    DD *                                                          
 DELETE  <@qualifiedTest/>.XLTRPT PURGE                                        
 IF LASTCC > 0  THEN        /* IF OPERATION FAILED,     */    -          
     SET MAXCC = 0          /* PROCEED AS NORMAL ANYWAY */                                                                                    
 DELETE  <@qualifiedTest/>.XLTCOV PURGE                                        
 IF LASTCC > 0  THEN        /* IF OPERATION FAILED,     */    -          
     SET MAXCC = 0          /* PROCEED AS NORMAL ANYWAY */                                                                                    
//*                                                                      
//*********************************************************************  
//* PSTEP105 - GENERATE LT REPORT                                        
//*                                                                      
//*                                                                      
//*********************************************************************  
//*                                                                      
//PSTEP105 EXEC PGM=GVBLTPRT                                              
//*                                                                      
//*                                                                      
//*        INPUT FILES                                    
//*                                                                      
//LT       DD DSN=<@qualifiedTest/>.MR91.XLT,        
//            DISP=SHR  
/*                                                 
//LTPRPARM DD DSN=<@qualifiedTest/>.PARM(LTPARM),                          
//            DISP=SHR    
/*                                      
//LTPRTRPT DD DSN=<@qualifiedTest/>.XLTRPT,                                    
//            DISP=(NEW,CATLG,DELETE),                                   
//            UNIT=SYSDA,                                                
//            SPACE=(TRK,(10,10),RLSE),                                  
//            DCB=(DSORG=PS,RECFM=VB,LRECL=120)     
//*
//LTCOV    DD DSN=<@qualifiedTest/>.XLTCOV,                                    
//            DISP=(NEW,CATLG,DELETE),                                   
//            UNIT=SYSDA,                                                
//            SPACE=(TRK,(10,10),RLSE),                                  
//            DCB=(DSORG=PS,RECFM=VB,LRECL=200)     
//*
//LTPRTLOG DD SYSOUT=*                     
//*                                                                      
//SYSPRINT DD SYSOUT=*,DCB=(RECFM=VB,LRECL=255,BLKSIZE=32760)            
//SYSOUT   DD SYSOUT=*,DCB=(RECFM=VB,LRECL=255,BLKSIZE=32760)            
//CEEDUMP  DD SYSOUT=*                                                   
//SYSMDUMP DD SYSOUT=*                                                   
//*
//*******************************************************************
//* SUBMIT NEXT JOB
//*******************************************************************
//*
//JSTEPNX1 EXEC PGM=IEBGENER
//*
//SYSIN    DD DUMMY
//*
//SYSUT1   DD DSN=<@qualifiedTest/>.JCL(${test.name}J),
//            DISP=SHR
//*
//SYSUT2   DD SYSOUT=(*,INTRDR)
//*
//SYSPRINT DD SYSOUT=*
//*