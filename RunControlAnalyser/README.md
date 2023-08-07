# Run Control Analyzer
View and analyze the contents of a Run Control Set, that is the collection of files that tell the GenevaERS Performance Engine what to do. They are made up of the VDP, XLT and JLT.

The analysis shows the contents of the VDP in readable form, and the flow of data through the views. 
Further details can be seen by drilling down into the elements of the resulting flow diagram.

# Installation

The Run Control Analyzer is built as part of the GenevaERS-Java-Frontend [Build](../README.md).

Alternatively, you can download the latest Run Control Analyzer release from GenevaERS-Java-Frontend releases. Look for download RCA v1.x.x.
Extract all the files from the zip into a directory of your choice.

# Usage
The Run Control Analyzer can process Run Control (RC) Sets in two main modes. 

1) Via FTP to retrieve an RC set from the mainframe.
2) Locally to process or reprocess an existing RC set on your PC.

This is shown in the [Process](docs/ProcessFlow.gv.svg) diagram.

The retrieved RC Sets are stored in directory RunControlAnalyser/target/RunControls/< RC Set name >

The results of an analysis are stored in a directory (.gersflows) relative to a user's home directory.

Before you can use the Run Control Analyser (RCA) to FTP RC Sets from a mainframe, some environment variables need to be setup.  
Some of these are the same environment variables needed to run the PE Test Framework. The RCA needs your mainframe URI and mainframe user ID and Password, for example:

    export TSO_USERID=<your mainframe user ID>
    export TSO_SERVER=<uri of the mainframe lpar>
    export TSO_PASSWORD=<your password>

Note : When you exit gitBash your password will be forgotten

If you're running from a Windows command prompt then use then following instead of export:

    set TSO_USERID=<your mainframe user ID> 
    set REGHOST=<uri of the mainframe lpar> 
    set TSO_PASSWORD=<your password>

# How to Run

If you have installed RCA by cloning the source code and building using Maven, change to directory GenevaERS-Java-Frontend/RunControlAnalyser/target

Then enter:

    ../run.sh

If you have downloaded the RCA release zip and extracted the files into a directory, change into that directory.
Then enter:

    run

The RCA has its own command line menu system to guide you through the process.

Via the top level menu you can choose to

    FTP and process an RC Set
    Process a local RC Set

    Re-examine the results of a previous run.
    Either manually and select via File Explorer.
    Or select via a generated list.

The FTP menu needs the environment variables mentioned above. Then

    1) Set a name for the RC set.
    2) Set the mainframe HLQ of the RC Set
    3) Run the process

Processing a local RC Set requires:
    
    1) Set the base directory of the RC Sets
    2) Set the name of the RC Set. (The desired RC Set can be chosen via a generated list.)
    3) Run the Analyser

