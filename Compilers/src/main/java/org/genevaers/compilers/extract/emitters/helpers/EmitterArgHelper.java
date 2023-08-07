package org.genevaers.compilers.extract.emitters.helpers;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import org.genevaers.compilers.extract.astnodes.DateFunc;
import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.astnodes.NumAtomAST;
import org.genevaers.compilers.extract.astnodes.RundateAST;
import org.genevaers.compilers.extract.astnodes.StringAtomAST;
import org.genevaers.compilers.extract.astnodes.UnaryInt;
import org.genevaers.compilers.extract.astnodes.ASTFactory.Type;
import org.genevaers.genevaio.ltfile.ArgHelper;
import org.genevaers.genevaio.ltfile.LogicTableArg;

public class EmitterArgHelper {
    
    private static final int LTDateRunDay                = 0xffffffff;
    private static final int LTDateRunMonth              = 0xfffffffe;
    private static final int LTDateRunYear               = 0xfffffffd;
    private static final int LTDateFirstOfQuarter        = 0xfffffffc;
    private static final int LTDateLastOfQuarter         = 0xfffffffb;
    private static final int LTDateFirstOfQ1             = 0xfffffffa;
    private static final int LTDateFirstOfQ2             = 0xfffffff9;
    private static final int LTDateFirstOfQ3             = 0xfffffff8;
    private static final int LTDateFirstOfQ4             = 0xfffffff7;
    private static final int LTDateLastOfQ1              = 0xfffffff6;
    private static final int LTDateLastOfQ2              = 0xfffffff5;
    private static final int LTDateLastOfQ3              = 0xfffffff4;
    private static final int LTDateLastOfQ4              = 0xfffffff3;
    private static final int LTDateRunPeriod             = 0xfffffff2;
    private static final int LTDateTimeStamp             = 0xfffffff1;
    private static final int LTDateFiscalDay             = 0xfffffff0;
    private static final int LTDateFiscalMonth           = 0xffffffef;
    private static final int LTDateFiscalYear            = 0xffffffee;
    private static final int LTDateFiscalPeriod          = 0xffffffed;
    private static final int LTDateFiscalFirstOfQuarter  = 0xffffffec;
    private static final int LTDateFiscalLastOfQuarter   = 0xffffffeb;
    private static final int LTFillField                 = 0xfff0fff0;
    
    private EmitterArgHelper() { }

    public static void setArgValueFrom(LogicTableArg arg, String valStr) {
        ArgHelper.setArgValueFrom(arg, valStr);
    }

    public static void setArgValueFrom(LogicTableArg arg, int val) {
        ArgHelper.setArgValueFrom(arg, val);
    }

    public static void setArgValueFrom(LogicTableArg arg, RundateAST rhs) {
        //This is where we need some magic to map the constant
        switch(rhs.getValue()) {
            case "RUNDAY":
            arg.setValueLength(LTDateRunDay);
            break;
            case "RUNMONTH":
            arg.setValueLength(LTDateRunMonth);
            break;
            case "RUNYEAR":
            arg.setValueLength(LTDateRunYear);
            break;
            default:
            //Error time
            break;
        }
        int v = 0;
        UnaryInt ui = new UnaryInt();
        ui.setValue("0");
        if(rhs.getNumberOfChildren() > 0) {
            ui = (UnaryInt) rhs.getChildIterator().next(); //only one child
            v = Integer.parseInt(ui.getValue());
        }
        byte[] bytes = new byte[256];
        int length = Integer.BYTES;
        for (int i = 0; i < length; i++) {
            bytes[length - i - 1] = (byte) (v & 0xFF);
            v >>= 8;
        }
        String val = new String(bytes);
        arg.setValue(val);
    }

    public static void setArgVal(ExtractBaseAST rhs, LogicTableArg arg) {
        if(rhs.getType() == Type.NUMATOM) {
            setArgValueFrom(arg, ((NumAtomAST)rhs).getValue());
        } else if(rhs.getType() == Type.DATEFUNC) {
            setArgValueFrom(arg, ((DateFunc)rhs).getValue());
        } else if(rhs.getType() == Type.RUNDATE) {
            setArgValueFrom(arg, (RundateAST)rhs);
        } else {
            setArgValueFrom(arg, ((StringAtomAST)rhs).getValue());
        }
    }

    public static String getStringFromNodeVal(ExtractBaseAST rhs) {

        // We should not be doing this
        // Add a GersValue interface to those node that supply a value
        if(rhs.getType() == Type.NUMATOM) {
            return String.valueOf(((NumAtomAST)rhs).getValue());
        } else if(rhs.getType() == Type.DATEFUNC) {
            return ((DateFunc)rhs).getValue();
        } else if(rhs.getType() == Type.RUNDATE) {
            return ((RundateAST)rhs).getValue();
        } else {
            return ((StringAtomAST)rhs).getValue();
        }
    }

    public static String getArgString(LogicTableArg arg) {
        return ArgHelper.getArgString(arg);
    }

}
