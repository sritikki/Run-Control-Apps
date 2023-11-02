<#ftl strip_whitespace="true">
<#assign importList = [] />
<#assign javaList = false />
package org.genevaers.genevaio.ltfile;

<#list imports as i>
import ${i};
</#list>

import java.io.FileWriter;
import java.io.IOException;
<#if javaList>
import java.util.ArrayList;
import java.util.List;
</#if>
import org.genevaers.genevaio.fieldnodes.NumericFieldNode;
import org.genevaers.genevaio.fieldnodes.FieldNodeBase;
import org.genevaers.genevaio.fieldnodes.FunctionCodeNode;
import org.genevaers.genevaio.fieldnodes.NoComponentNode;
import org.genevaers.genevaio.fieldnodes.RecordNode;
import org.genevaers.genevaio.fieldnodes.RecordPartNode;
import org.genevaers.genevaio.fieldnodes.StringFieldNode;
import org.genevaers.genevaio.recordreader.RecordFileReaderWriter;
import org.genevaers.repository.components.enums.LtRecordType;
import org.genevaers.genevaio.recordreader.RecordFileReaderWriter.FileRecord;

/**
 * This class was automatically generated by the GenevaERS Component Generator.
 * 
 * DO NOT EDIT
 * 
 */
public class ${record.recordName}
    extends LTFileObject <#if !(record.recordType == "none")>implements LTRecord</#if>
{
<#list entries.fieldEntries as field>
${field}
</#list>

<#list entries.gettersAndSetters as getAndSetEntry>
${getAndSetEntry}
</#list>

    @Override
    public void readRecord(LTRecordReader reader, FileRecord rec)
        throws Exception
    {
<#list entries.readers as readerEntry>
${readerEntry}
</#list>
    }

    @Override
<#if !(record.recordType == "none")>
   	public void addRecordNodes(FieldNodeBase root, boolean compare)
    {
        FunctionCodeNode rn = new FunctionCodeNode();
        rn.setName("LT_" + rowNbr);
        rn.setFunctionCode(functionCode);
        rn = (FunctionCodeNode) root.add(rn, compare);
<#else>
   	public void addRecordNodes(FieldNodeBase rn, boolean compare)
    {
</#if>
<#list entries.fieldNodeEntries as fieldNodeEntry>
${fieldNodeEntry}
</#list>
    }

    @Override
    public void writeCSV(FileWriter fw) throws IOException
    {
<#list entries.csvEntries as csvEntry>
${csvEntry}
</#list>
    }

    @Override
    public void writeCSVHeader(FileWriter fw) throws IOException
    {
<#list entries.csvHeaders as csvHeader>
${csvHeader}
</#list>
    }

    @Override
    public void fillTheWriteBuffer(RecordFileReaderWriter readerWriter) {
        org.genevaers.genevaio.recordreader.RecordFileReaderWriter.FileRecord buffer = readerWriter.getRecordToFill();
<#if record.recordLength?? && record.recordLength != 0>
        recLen = (short) ${record.recordLength?c};
</#if>
<#list entries.fillTheWriteBufferEntries as fillEntry>
${fillEntry}
</#list>
    }
}
