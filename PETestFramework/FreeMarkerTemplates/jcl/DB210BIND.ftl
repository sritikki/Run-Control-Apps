//${test.name}N JOB (ACCT),'REGRESSION JOB',
//            NOTIFY=&SYSUID.,
//            CLASS=A,
//            MSGLEVEL=(1,1),
//            MSGCLASS=H
//*
//JOBLIB   DD DISP=SHR,DSN=${env["OVERRIDE"]}
//         DD DISP=SHR,DSN=${env["PMLOAD"]}
//         DD DISP=SHR,DSN=${env["DBSDSNL"]}
//         DD DISP=SHR,DSN=${env["DBSDSNE"]}
//         DD DISP=SHR,DSN=${env["DBRUNLD"]}
//         DD DISP=SHR,DSN=CEE.SCEERUN
//         DD DISP=SHR,DSN=CEE.SCEERUN2
//         DD DISP=SHR,DSN=CBC.SCLBDLL
//*
//*
//*********************************************************************
//*
//*   INSTBLD - INSTALL A BUILD OF THE PERFORMANCE ENGINE
//*             INTO AN ENVIRONMENT
//*
//*   NOTE: SETUP FOR ${env["TEST_HLQ"]}
//*
//*   copied from CC 26 Aug 2016
//*********************************************************************
//*********************************************************************
//*   FREE PLANS
//*********************************************************************
//*
<#macro qualifiedTest>
<#-- To avoid the line break at the end I butted up the closing tag -->
${env["TEST_HLQ"]}.${test.dataSet}</#macro>
//FREEPLAN EXEC PGM=IKJEFT01
//*
//STEPLIB  DD DISP=SHR,DSN=${env["DBSDSNL"]}
//*
//SYSTSPRT DD SYSOUT=*
//*
//SYSTSIN  DD *
 DSN SYSTEM(${env["DBSUB"]})
  FREE PLAN(GVBMRDV)
  FREE PLAN(GVBMRCT)
  FREE PLAN(GVBMRSQ)
  END
 CALL 'SYS1.LINKLIB(IEFBR14)'       /* ZERO OUT RETURN CODE */
//*
//*********************************************************************
//* IF THIS STEP ABENDS:
//* 1) FOLLOW RESTART INSTRUCTIONS FROM STEP FREEPLAN
//*
//*********************************************************************
//*
//FREEPLCK EXEC PGM=GVBUT99,
//            COND=(0,EQ,FREEPLAN),
//            PARM='1099'
//*
//SYSUDUMP DD SYSOUT=*
//*
//*********************************************************************
//*   BIND PLANS
//*********************************************************************
//*
//BINDPLAN EXEC PGM=IKJEFT1A
//*
//STEPLIB  DD DISP=SHR,DSN=${env["DBSDSNL"]}
//*
//DBRMLIB  DD DSN=${env["PMHLQ"]}.GVBDBRM,
//            DISP=SHR
//*
//SYSTSPRT DD SYSOUT=*
//*
//SYSTSIN  DD *
 DSN SYSTEM(${env["DBSUB"]})
  BIND PLAN(GVBMRDV) MEM(GVBMRDV) ACT(REP) ISOLATION(CS) -
  LIB('${env["PMHLQ"]}.GVBDBRM') QUALIFIER(SDATRT01) -
  OWNER(SDATRT01)
  BIND PLAN(GVBMRCT) MEM(GVBMRCT) ACT(REP) ISOLATION(CS) -
  LIB('${env["PMHLQ"]}.GVBDBRM') QUALIFIER(SDATRT01) -
  OWNER(SDATRT01)
  BIND PLAN(GVBMRSQ) MEM(GVBMRSQ) ACT(REP) ISOLATION(CS) -
  LIB('${env["PMHLQ"]}.GVBDBRM') QUALIFIER(SDATRT01) -
  OWNER(SDATRT01)
//*
//*********************************************************************
//* IF THIS STEP ABENDS:
//* 1) FOLLOW RESTART INSTRUCTIONS FROM STEP BINDPLAN
//*
//*********************************************************************
//*
//BINDPLCK EXEC PGM=GVBUT99,
//            COND=(8,GT,BINDPLAN),
//            PARM='1099'
//*
//SYSUDUMP DD SYSOUT=*
//*
//GRNTPLAN EXEC PGM=IKJEFT1A,DYNAMNBR=20
//*
//STEPLIB  DD DISP=SHR,DSN=${env["DBSDSNE"]}
//         DD DISP=SHR,DSN=${env["DBSDSNL"]}
//*
//SYSTSPRT DD SYSOUT=*
//SYSPRINT DD SYSOUT=*
//*
//SYSTSIN  DD *
  DSN SYSTEM(${env["DBSUB"]}) RETRY(0) TEST(0)
  RUN PROGRAM(DSNTIAD) PLAN(${env["DBTIADP"]}) -
  LIB('${env["DBRUNLD"]}')
/*
//SYSIN    DD  *
 SET CURRENT SQLID='SDATRT01';
 GRANT EXECUTE ON PLAN GVBMRCT TO PUBLIC;
 GRANT EXECUTE ON PLAN GVBMRSQ TO PUBLIC;
//*
//*********************************************************************
//* IF THIS STEP ABENDS:
//* 1) FOLLOW RESTART INSTRUCTIONS FROM STEP GRNTPLAN
//*
//*********************************************************************
//*
//GRNTPLCK EXEC PGM=GVBUT99,
//            COND=(0,EQ,GRNTPLAN),
//            PARM='1099'
//*
//SYSUDUMP DD SYSOUT=*
//*
//*******************************************************************
//* JSTEPNX1 - SUBMIT NEXT JOB
//*******************************************************************
//*
//JSTEPNX1 EXEC PGM=IEBGENER
//*
//SYSIN    DD DUMMY
//*
//SYSUT1   DD DSN=<@qualifiedTest/>.JCL(${test.name}L),
//            DISP=SHR
//*
//SYSUT2   DD SYSOUT=(*,INTRDR)
//*
//SYSPRINT DD SYSOUT=*
//*
//*********************************************************************
//*   ABEND IF A PRIOR PROCESS HAS FAILED
//*
//*   NOTE: CHECK FOR ERROR MESSAGE ON THE PRIOR STEP
//*         FOR PROBLEM DETERMINATION
//*********************************************************************
//*
//JSTEPNXX EXEC PGM=GVBUT99,
//            PARM='1099',
//            COND=(0,EQ,JSTEPNX1)
