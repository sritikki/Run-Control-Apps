<#ftl strip_whitespace="true">
<#assign importList = [] />
<#assign javaList = false />
package org.genevaers.genevaio.ltfactory;
/*
** This file was automatically generated. 
**
** Do not edit.
*/

import org.genevaers.repository.components.enums.LtRecordType;

/**
 * Factory functions to get the left and right hand side Arithmetic Emitters
 * for an arithmetic expression.
 */
public class LtRecordFactory extends LtRecordFactoryBase {

    public static void init() {
        <#list codes as code>
        ltRecordEmitters.put("${code.functionCode}", getRecordFactory(LtRecordType.${code.ltRecordType!"recType"}));
        </#list>
    }

}
