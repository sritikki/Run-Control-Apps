<#assign model = modelDefinition?eval_json> 
# Geneva ERS Component Generation

The Component Generator makes the key data components of [Geneva ERS](https://genevaers.org/) Java Frontend and, where feasable, 
automatically generates code to manage them.

# Developer's How To

In order to build the Component Generator you need the following.

A Java 11 or greater JDK for your platform. For instance from [here](https://adoptopenjdk.net/)

An up to date install of [Maven](https://maven.apache.org/download.cgi) - latest at time of writing is Apache Maven 3.8.4.

Clone the repository from github somewhere on your machine.

Use GitBash or something similar and change to the directory where you cloned the repository.

Before building ensure that a Java JDK and Maven are installed.
Enter the commands below and you should see something similar.

    $ java --version
    openjdk 11.0.10 2021-01-19
    OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.10+9)
    Eclipse OpenJ9 VM AdoptOpenJDK (build openj9-0.24.0, JRE 11 Windows 10 amd64-64-Bit Compressed References 20210120_899 (JIT enabled, AOT enabled)
    OpenJ9   - 345e1b09e
    OMR      - 741e94ea8
    JCL      - 0a86953833 based on jdk-11.0.10+9)

    $ mvn -version
    Apache Maven 3.8.4 (9b656c72d54e5bacbed989b64718c159fe39b537)
    Maven home: C:\maven\apache-maven-3.8.4
    Java version: 11.0.10, vendor: AdoptOpenJDK, runtime: C:\adoptOpenJDK\jdk-11.0.10+9
    Default locale: en_AU, platform encoding: Cp1252
    OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"

To build for your platform enter

    mvn install

Maven will then download the various libraries needed.
This may take some time for the first build. Subsequent builds will be faster.

On completion you should see something similar to.

    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time:  31.444 s
    [INFO] Finished at: 2022-02-01T15:35:52+08:00
    [INFO] ------------------------------------------------------------------------

To generate components from the underlying YAML definitions enter

    ./target/compgen.exe

This will output a record of the files generated ending with.

        [INFO] --- ------------------------------------ ---
        [INFO]
        [INFO] Write .\Readme.md via readme.ftl
        [INFO] --- Generation Completed ---

# Description

This code uses the [FreeMarker Template Engine](https://freemarker.apache.org/).

The diagram below shows the general data flow of the Component Generator.

<img src ="./docs/vdpFlow.svg">

The GenevaERS model is made up from a number of segments defined in [the model config](src/main/resources/modelConfig.yaml).

Each of which is defined in its own YAML file. 

The segments and their source files are:

| Segment     | YAML  |
| ----------- | ----- |
<#list model.segments as seg>
|${seg.name}  |[${seg.source}](${seg.source}) |       
</#list>

Each segment has a number of definition input files that are read by the Component Generator and applied to
FreeMarker Templates to generate the desired type of output.  
This documentation is produced in the same way.

# Why bother?

- Generating the various data objects from a single point of definition means that the 
consumers of the data will see products that are in synch. 

- It provides a centralised means of managing and documenting the record structures.

# Segments

The sections below describe each of the model segments.

## ${model.segments[0].name}

${model.segments[0].definition.description}

| VDP Record  | DSECT |
| ----------- | ----- |
<#list model.segments[0].definition.items as item>
        <#if !(item.details.recordID == 0)>
| [${item.details.record_name}](docs/${item.details.record_name}.md) | [${item.details.recordID?c}](docs/mac/GVB${item.details.recordID?c?left_pad(4, "0")}A.mac) |
        </#if>
</#list>

## ${model.segments[1].name}

${model.segments[1].definition.description}

| LT Record  | DSECT |
| ---------- | ----- |
<#list model.segments[1].definition.items as item>
        <#if !(item.details.recordType == "none")>
| [${item.details.record_name}](docs/${item.details.record_name}.md) | [${item.details.recordType}](docs/ltmac/GVBLT${item.details.recordType}A.mac) |
        </#if>
</#list>

## ${model.segments[2].name}

${model.segments[2].definition.description}

The links below are a work in progress and currently just 404.

| Component  | Java  |
| ---------- | ----- |
<#list model.segments[2].definition.items as item>
| [${item.details.component_name}](docs/${item.details.component_name}.md) | [${item.details.component_name}](build/generated/sources/compjobj/${item.details.component_name}.java) |
</#list>

## ${model.segments[3].name}

${model.segments[3].definition.description}

The table below lists the enumerations defined.

The Assembler equates generated are [here](docs/mac/GVBEQS.mac).

| ASM Prefix  | Description |
| ----------  | ----------- |
<#list model.segments[3].definition.items[0].details as key, value>
| ${value.asmPrefix} | ${value.description} |
</#list>

## Relationship Records to Components

The records managed by this code base go to make up the contents of the View Definition Parameters(VDP) file and the logic table files.
The files that are used to tell the extract engine what data to process and how to transform that data.

The VDP and logic table files are generated as a result of the Pre Processor reading its input Workbech XML files.

