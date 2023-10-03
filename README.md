# GenevaERS-Java-Frontend
The two main applications within the Java Frontend of GenevaERS are the [Run Control Generator](RunControlGenerator/README.md) and the [Run Control Analyzer](RunControlAnalyser/README.md).

The [Performance Engine Test Framework](PETestFramework/README.md) is also in this repo it is used to develop and test views. As well as to provide a regression framework to test the performane engine itself.

There are a number of libraries that provide the underlying functions that read and write the components and records of the performance engine. The diagram [projects](docs/projects.svg) captures the overall structure.

At the heart of the code is the [Component Generator](ComponentGenerator/Readme.md) which is used to avoid a large amount of repetitive coding. And to ensure compatability of record formats between the various applications both on the mainframe assembler and Java programs.

# Clone and Build

The Java programs are mainly intended to be run from z/OS Unix.  
They do build and run from Windows too and the instructions are essentially the same.  
But here we concentrate on z/OS.

## z/OS 

Log into your z/OS Unix account.

I suggest via git bash and entering (with your TSO username).

```
ssh username@sp13.svl.ibm.com
```


You can also run from OMVS but the command line editing features are much better via ssh.

The following environment variables are needed.
Best to add them to your ~/.profile.  
Edit using vi via ssh or oedit via OMVS. 

Add the following if they are not already there.
Or as a temporary measure just run them from the command line.

```
export PATH=$PATH:/hsstools/git-2.14.4/bin/:/u/icunnin/apache-maven-3.8.4/bin:/Java/J11.0_64/bin:/bin
export LIBPATH=/usr/lib/java_runtime64:/lib:/usr/lib
export JAVA_HOME=/Java/J11.0_64
export MAVEN_OPTS=-Dfile.encoding=IBM-1047
```

The above shows that Maven is being run from my account. We probably should move it to a more general location.

## Prerequisites

Before building ensure that a Java JDK and Maven are available.
Enter the commands below and you should see something similar.

```
$ java -version
java version "11.0.12" 2021-07-20
IBM Semeru Runtime Certified Edition for z/OS 11.0.0.0 (build 11.0.12+7)
IBM J9 VM 11.0.0.0 (build z/OS-Release-11.0.0.0-b04, JRE 11 z/OS s390x-64-Bit Compressed References 20211026_96 (JIT enabled, AOT enabled)
OpenJ9   - 9726ee78977
OMR      - 769db6cade4
IBM      - 2f2c48b
JCL      - 5c83cf3c907 based on jdk-11.0.12+7)
```
And 

```
$ mvn -version
Apache Maven 3.8.4 (9b656c72d54e5bacbed989b64718c159fe39b537)
Maven home: /u/icunnin/apache-maven-3.8.4
Java version: 11.0.12, vendor: IBM Corporation, runtime: /Java/J11.0_64
Default locale: en_US, platform encoding: IBM-1047
OS name: "z/os", version: "02.04.00", arch: "s390x", family: "z/os"
```

### Build

Clone the GenevaERS-Java-Frontend repository from github to a local directory.
And cd to your cloned repo. 

For instance...

```
mkdir git
cd git
git clone git@github.ibm.com:SAFR/GenevaERS-Java-Frontend.git
cd GenevaERS-Java-Frontend
```

Before starting the build we need some jar files installed into the Maven repository.
Run the maven install script.

```
sh mvnInstallJars.sh
```

You should see four BUILD SUCCESS messages.

The above should be a one off and once done will not be required again unless you have corrupted or deleted your Maven repository.

To build the apps for your platform enter

### Windows

    mvn install 


### z/OS

    mvn install -DskipTests -Drat.skip=true

Maven will then download the various libraries needed for the build.
This may take some time for the first build. Subsequent builds will be faster.

To rebuild following code changes or a git pull clean the build first.

    mvn clean
    mvn install -DskipTests -Drat.skip=true

On completion you should see somthing similar to.

    [INFO] ------------------------------------------------------------------------
    [INFO] Reactor Summary:
    [INFO]
    [INFO] GenevaERS Apps 1.0.1 ...................... SUCCESS [  0.248 s]
    [INFO] GenevaERS Component Generator 1.0.1 ....... SUCCESS [  7.367 s]
    [INFO] GenevaERS Utilities 1.0.1 ................. SUCCESS [  0.647 s]
    [INFO] GenevaERS Component Repository 1.0.1 ...... SUCCESS [  8.532 s]
    [INFO] GenevaERS GenevaIO 1.0.1 .................. SUCCESS [ 27.997 s]
    [INFO] GenevaERS Compilers 1.0.1 ................. SUCCESS [  7.788 s]
    [INFO] GenevaERS Test Framework 1.0.1 ............ SUCCESS [ 18.537 s]
    [INFO] GenevaERS Run Control Generator 1.0.1 ..... SUCCESS [ 10.306 s]
    [INFO] GenevaERS Run Control Analyser 1.0.1 ............... SUCCESS [ 16.571 s]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time:  01:38 min
    [INFO] Finished at: 2022-07-04T09:35:17+08:00
    [INFO] ------------------------------------------------------------------------

To run the Test Framework

```
cd PETestFramework/
```

Its Readme etc is here [Performance Engine Test Framework](PETestFramework/README.md)