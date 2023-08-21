<#include "../common/JUnitHeader.ftl"/>
<#import "WBXMLFiles.ftl" as wbxml>
<#import "FTPSession.ftl" as ftp>
<#import "OutFiles.ftl" as outfiles>

package org.genevaers.test.${spec.category?replace("/",".")};

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.genevaers.testframework.jesjob.JesJobSession;
import org.genevaers.testframework.jesjob.JesResult;
import org.genevaers.testframework.jesjob.JesResult.JesResultType;
import org.genevaers.testframework.jesjob.JobsRunList;
import org.genevaers.testframework.OutFileFactory;
import org.genevaers.testframework.FMTest;
import org.genevaers.testframework.OutFile;
import org.genevaers.utilities.FTPSession;
import org.genevaers.utilities.FileProcessor;
import org.genevaers.utilities.Substitution;
import org.genevaers.testframework.visualisation.VDPNLRunner;
import org.genevaers.testframework.visualisation.FlowRunner;

public class ${spec.name} extends FMTest {
<#list spec.tests as test>
    public void test${test.name}() throws Exception
    {
        <@ftp.connect />
        
<#assign testDirectory = "${spec.category}/${spec.name}/${test.name}">        
<#assign qualifiedTest = "${env['GERS_TEST_HLQ']}.${test.dataSet}">
<#assign xmlFolder = "${env['LOCALROOT']}/xmlout/${testDirectory}">
        <@wbxml.generate test xmlFolder/>

        <@ftp.clearDatasets spec test  />

        <@ftp.transferXMLs spec test />

        <@ftp.transferConfigAndJCL testDirectory qualifiedTest />

        <@ftp.setupAndConnectJobSession test testDirectory />

        <@ftp.getNumExpectedJobs test />

        <@ftp.setExpectedResult test />

        category = "${spec.category}";
        specName = "${spec.name}";
        testName = "${test.name}";
	outFolder = getOutFolder(category, specName, testName);

        List<OutFile> outFiles = new ArrayList<OutFile>();

        JobsRunList jobs = jSession.submitAndMonitorJob("${test.name}*", jobFile, expectedNumJobs, 4);
        //Run completed 
        if(jobs.checkAllJobsCompleted(expectedNumJobs)) {
                jobs.checkAllJobsPassed();
                <@ftp.checkAndRunSuperC test testDirectory />

<#if test.comparephase??> 
        // No output files to fetch when comparing on the mainframe
<#elseif test.result.type?lower_case == "abend">
        // Expecting an abend so at the moment no output files to fetch
<#else>
        // get output files and compare with base
       <@outfiles.make qualifiedTest test/>
</#if>

                getFiles(outFiles, session);
        }

        jSession.disconnect();    
        endSession(session);                
        processResult(expected, outFiles, jobs);
    }

</#list>    
} //End of TestCase

