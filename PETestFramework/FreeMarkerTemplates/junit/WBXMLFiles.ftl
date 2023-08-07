<#macro generate test xmlFolder>
        //Make the XML target directory
        File xmlDir = new File("${xmlFolder}");
        xmlDir.mkdirs();
        List<File> filesIn = new ArrayList<File>();
        List<Substitution> substs = new ArrayList<Substitution>();
    <#list test.xmlfiles as xmlFile>
        //Move the XML file via a substitution
        File xmlfile${xmlFile?counter} = new File("${env["LOCALROOT"]}" + File.separator + "xml" + File.separator + "${xmlFile.name}");
        filesIn.add(xmlfile${xmlFile?counter});
        File outxmlfile${xmlFile?counter} = new File(xmlDir, "XML${xmlFile?counter}");
        substs.add(new Substitution("?>", " encoding=\"IBM-1047\"?>", 1, 1));
        <#list xmlFile.replacements as repl>
            <#if repl.start??>
        substs.add(new Substitution("${repl.start.replace}", "${repl.start.with}", ${repl.start}, ${repl.stop}));
            <#else>
        substs.add(new Substitution("${repl.replace}", "${repl.with}"));
            </#if>
        </#list>
        FileProcessor.sed(xmlfile${xmlFile?counter}, outxmlfile${xmlFile?counter}, substs);
        substs.clear();
   </#list>
</#macro>
