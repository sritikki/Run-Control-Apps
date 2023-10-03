<#include "../common/ParamGeneration.ftl"/>
<#macro Spaces num>
${""?right_pad(num)}</#macro>
<#macro RightPad val num>
<#if val??>${val?right_pad(num)}<#else><@Spaces num/></#if></#macro>
<#macro LeftPadZero val num>
<#if val??>${val?left_pad(num, "0")}<#else><@Spaces num/></#if></#macro>
<#macro LeftPad val num>
<#if val??>${val?left_pad(num)}<#else><@Spaces num/></#if></#macro>
<#if test.mergeparm??>
*************  MergeParm
*
* POSITION  DESCRIPTION                                                 
*  1        PUT "*" FOR COMMENTS                                        
*  1-8      ENTITY   IDENTIFIER/MNEMONIC NAME                           
*  1-8      ENTITY   IDENTIFIER/MNEMONIC NAME                           
*  9        FILLERAL PARAMETER  KEYWORD                                 
* 10-13     OPTIONAL PARAMETER  KEYWORD                                 
* 14        FILLERARTITIONS (D, W, M, A, ETC)   USED TO PLUG INTO _     
* 15-22     DDNAME TEMPLATE                                             
* 23        FILLERNG PRTN NUM VALID 00 THRU 99, USED TO PLUG INTO ## RN)
* 24-35     CATEGORIES (D, W, M, A, ETC)   USED TO PLUG INTO _     
* 36        FILLERNG PRTN NUM VALID 00 THRU 99, USED TO PLUG INTO ## RN)
* 37-38     STARTING PRTN NUM VALID 00 THRU 99, USED TO PLUG INTO ##    
* 39        FILLER TYPE (" ","T","P","V")                               
* 40-41     STARTING PRTN NUM VALID 00 THRU 99, USED TO PLUG INTO ##    
* 42        FILLERSE OPTION (" ","A","1")                               
* 43        ENTITY TYPE (" ","T","P","V")                               
* 44        FILLERED PREFIX LENGTH                                      
* 45        COLLAPSE OPTION (" ","A","1")                               
* 46        FILLERSITION                                                
* 47-48     EMBEDDED   PREFIX    LENGTH (NON-KEY PREFIX)                
* 49        FILLERNGTH                                                  
* 50-51     KEY        POSITION                                         
* 52        FILLEREAK LENGTH                                            
* 53-54     KEY        LENGTH                                           
* 55        FILLERAMP POSITION                                          
* 56-57     TIMESTAMP  POSITION                                         
* 58        FILLERTE POSITION                                           
* 59-60     EFF  DATE  POSITION                                         
* 61        FILLERE ID POSITION                                         
* 62-63     PROFILE ID POSITION                                         
* 64        FILLERBSTITUTION PATTERN FOR A NODE IN THE DSNAME           
* 65        DB2 INDICATOR IF INPUT DATASET NAME IS A DB2 DATASET        
*           IF 'Y' THEN POSITION 79 - 80 SHOULD BE BLANK                
*           IF NOT 'Y' THEN NON-DB2 IS ASSUMED AND "!!" NUMERIC         
* 66        FILLER                                                      
* 67-68     INDICATES THAT INPUT IS A GDG AND WHAT RELATIVE GDG         
*           GENERATION NEEDS TO BE ALLOCATEDAME IS A DB2 DATASET        
*           IS IGNORED (SHOULD BE BLANK) IF COLUMN 59 CONTAINS A 'Y'    
*           GDG GENERATION MUST BE A VALID SIGN AND NUMBER(" 0", TO -9) 
* 69            FILLER                                                  
* 24-67     DATASET NAME TO DYNAMICALLY ALLOCATE (ON OPTIONAL PARM)     
************************************************************************
*                                         T D PL KP KL TP EP PP D G     
*        OPT                              Y U FE EO EE IO FO RO B D     
*ENTITY* PARM *DDNAME* **CATEGORY** FR/TO P P XN YS YN MS DS OS 2 G# 
<#list test.mergeparm.entities as entity>
<#-- Putting all these on a single line until I can figure out how to manage the whitespace -->
<@RightPad entity.name 9/><#if entity.opt?length gt 0 ><@RightPad entity.opt 5/><#else><@Spaces 5/><@RightPad entity.ddname 9/><@RightPad entity.categories 13/><@LeftPadZero entity.from 2/>/<@LeftPadZero entity.to 2/> <@RightPad entity.type 2/><@RightPad entity.dup 2/><@LeftPadZero entity.prelen 2/> <@LeftPadZero entity.keypos 2/> <@LeftPadZero entity.keylen 2/><@LeftPad entity.timpos 3/><@LeftPad entity.efdpos 3/></#if>
</#list>
<#if test.mergeparm.breaklen?? >
BREAKLEN<@Spaces 15/><@LeftPadZero test.mergeparm.breaklen, 2/>
</#if>
<#if test.mergeparm.lrbuffer?? >
LRBUFFER<@Spaces 15/><@LeftPadZero test.mergeparm.lrbuffer, 3/>
</#if>
<#if test.mergeparm.extension?? >
EXTENSION<@Spaces 14/><@LeftPadZero test.mergeparm.extension, 3/>
</#if>
<#if test.mergeparm.notfound?? >
NOTFND<@Spaces 17/>${test.mergeparm.notfound?trim}</#if></#if>