<#macro make qual test>
        OutFile outFile = null;
<#if test.result.type?matches("badrc")>
//Bad RC case
<#if test.mr91only?matches("Y")>
//MR910nly
        outFile = OutFileFactory.getOutFileFactory().getOutFile("//${qual}.${test.mr91out.ddname}", 
                                            "${test.mr91out.startkey}", 
                                            "${test.mr91out.stopkey}");
        outFile.setSaveAsName("${test.mr91out.filename}");
        outFile.setTrim(false);
        outFiles.add(outFile);
</#if>
//Not MR910nly
 <#elseif test.formatfiles?size gt 0>
        //Get the Format Files. Number defined: ${test.formatfiles?size}
    <#list test.formatfiles as frmt>
        <#if frmt.ddname??>
            <#if frmt.start?? && !frmt.stop??>        
        outFile = OutFileFactory.getOutFileFactory().getOutFile("//${qual}.OUTF.MR88.${frmt.ddname}", 
                                            ${frmt.start}, 
                                            999 );
            <#elseif frmt.start?? && frmt.stop??>
        outFile = OutFileFactory.getOutFileFactory().getOutFile("//${qual}.OUTF.MR88.${frmt.ddname}", 
                                            ${frmt.start}, 
                                            ${frmt.stop} );
            <#else>
        outFile = OutFileFactory.getOutFileFactory().getOutFile("//${qual}.OUTF.MR88.${frmt.ddname}");
            </#if>
        outFile.setSaveAsName("OUTF.MR88.${frmt.ddname}");
        outFile.setTrim(true);
        outFiles.add(outFile);
        </#if>
    </#list>
<#else>
        //Get the Extract Files. Number defined: ${test.extractfiles?size}
    <#list test.extractfiles as extr>
        <#if extr.ddname??>
            <#if extr.start??>
        outFile = OutFileFactory.getOutFileFactory().getOutFile("//${qual}.${extr.ddname}", 
                                            "${extr.start}", 
                                            "${extr.stop}" );
            <#else>
                <#if extr.comparable??>
        outFile = OutFileFactory.getOutFileFactory().getOutFile("//${qual}.OUTE.MR95.${extr.ddname}", ${extr.comparable});
                <#else>
        outFile = OutFileFactory.getOutFileFactory().getOutFile("//${qual}.OUTE.MR95.${extr.ddname}");
                </#if>
            </#if>
        </#if>
        outFile.setSaveAsName("OUTE.MR95.${extr.ddname}");
        outFile.setTrim(true);
        outFiles.add(outFile);
    </#list>
</#if>
<#-- 
<xsl:if test="MergeRpt">
        OutFile moutFile = OutFileFactory.getOutFileFactory().getOutFile("OUTE.MR95.MERGRPT", 
                                            "<xsl:value-of select="MergeRpt/Start" />", 
                                            "<xsl:value-of select="MergeRpt/Stop" />" );
        moutFile.setTrim(true);
        outFiles.add(moutFile);
</xsl:if>


<xsl:if test="Result/Abend">        outFiles.clear();       // when abend don't compare outfiles
</xsl:if>
</xsl:template>
-->
</#macro>