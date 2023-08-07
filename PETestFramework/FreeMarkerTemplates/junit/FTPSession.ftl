<#macro connect>
        FTPSession session = new FTPSession("${env["TSO_SERVER"]}");
        session.setUserAndPassword("${env["TSO_USERID"]}","");
        // connect to the mainframe
        session.connect();
</#macro>

<#macro clearDatasets spec test>
        // clear out existing datasets
        String destQual = "//" + "${env["TEST_HLQ"]}.${test.dataSet}";
        session.deleteDatasetsUnder(destQual);
</#macro>

<#macro transferXMLs spec test>
        // transfer the XML files to the mainframe PDS
        session.setDCB("track", "100", "100", "VB", "1000");
        session.putPDS(xmlDir, "//" + "${env["TEST_HLQ"]}.${test.dataSet}" + ".MR91.XMLS");
</#macro>

<#macro transferConfigAndJCL testDirectory qualifiedTest>
        // transfer Config to the mainframe
        session.setDCB("track", "10", "10", "FB", "80");
        File cfg = new File("${env["LOCALROOT"]}/Config/${testDirectory}");
        session.putPDS(cfg, "//${qualifiedTest}.PARM");

        // transfer jcl to the mainframe
        session.setDCB("track", "10", "10", "FB", "80");
        <#-- should extract these as a common definitions section... then all template will play the same game? -->
        File jcl = new File("${env["LOCALROOT"]}/jcl/${testDirectory}");
        session.putPDS(jcl, "//${qualifiedTest}.JCL");
</#macro>

<#macro setupAndConnectJobSession test testDirectory>
<#if test.timeout??>
        JesJobSession jSession = new JesJobSession("${env["TSO_SERVER"]}", session.getUser(), session.getPassword(), ${test.timeout});
<#else>
        JesJobSession jSession = new JesJobSession("${env["TSO_SERVER"]}", session.getUser(), session.getPassword(), 10);
</#if>        
        jSession.connect();

         // clear out existing jobs
        jSession.removeFinishedJobs("${env["TSO_USERID"]}", "${test.name}");
<#if test.db2bind??>
        // Only the OLD regression has a ComparePhase so start with the Bind Job
        File jobFile = new File("${env["LOCALROOT"]}/jcl/${testDirectory}/${test.name}N.JCL");
<#else>
        File jobFile = new File("${env["LOCALROOT"]}/jcl/${testDirectory}/${test.name}L.JCL");
</#if>
</#macro>

<#macro getNumExpectedJobs test>
<#if test.result.type?lower_case == "abend">
        int expectedNumJobs = 3;
<#elseif test.formatfiles?size gt 0>        
        // Number of Extract Files ${test.extractfiles?size}
        int expectedNumJobs = 4;
<#else>
        int expectedNumJobs = 3;
</#if>

<#if test.db2bind??>
        //Don't forget the Bind job
        expectedNumJobs ++;
</#if>
<#if test.mr91only?matches("Y")>
        //MR91 Only so override the expected number of jobs
        expectedNumJobs = 1;
</#if>
</#macro>

<#macro setExpectedResult test>
        // check for expected result
<#if test.result.type?lower_case ==  "success">
        JesResult expected = new JesResult(JesResultType.SUCCESS);
<#elseif test.result.type?lower_case ==  "jclerror">
        JesResult expected = new JesResult(JesResultType.JCLERROR);
<#elseif test.result.type?lower_case ==  "abend">
        JesResult expected = new JesResult(JesResultType.ABEND, "00");
<#elseif test.result.type?lower_case?starts_with("badrc")>
<#if test.result.rc?lower_case?ends_with("8")>
        JesResult expected = new JesResult(JesResultType.BADRC, "${test.result.rc}");
<#else>
        JesResult expected = new JesResult(JesResultType.SUCCESS, "${test.result.rc}");
</#if>
<#else>
        Fail here, no such result type ${test.result.type?lower_case}
</#if>
</#macro>

<#macro checkAndRunSuperC test testDirectory>
<#if test.comparephase??>
            // More to to... run the compare phase
            //submit compare job
            File cmpJobFile = new File("${env["LOCALROOT"]}/jcl/${testDirectory}/${test.name}G.jcl");
            <#if test.formatfiles?size gt 0>
            expectedNumJobs = ${test.extractfiles?size};
            <#else>
            expectedNumJobs = 1; //No Format Files so just one job needed to compare the extracts
            </#if>
            jobs.addMore(jSession.submitAndMonitorJob("${test.name}G*", cmpJobFile, expectedNumJobs, 0));
            jobs.checkAllJobsCompleted(expectedNumJobs);
</#if>
</#macro>

