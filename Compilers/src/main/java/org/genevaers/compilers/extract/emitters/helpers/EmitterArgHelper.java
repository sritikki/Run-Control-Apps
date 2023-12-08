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
import org.genevaers.compilers.extract.astnodes.FiscaldateAST;
import org.genevaers.compilers.extract.astnodes.NumAtomAST;
import org.genevaers.compilers.extract.astnodes.RundateAST;
import org.genevaers.compilers.extract.astnodes.StringAtomAST;
import org.genevaers.compilers.extract.astnodes.UnaryInt;
import org.genevaers.compilers.extract.astnodes.ASTFactory.Type;
import org.genevaers.genevaio.ltfile.ArgHelper;
import org.genevaers.genevaio.ltfile.Cookie;
import org.genevaers.genevaio.ltfile.LogicTableArg;

public class EmitterArgHelper {
    
    private EmitterArgHelper() { }

    public static void setArgValueFrom(LogicTableArg arg, String valStr) {
        ArgHelper.setArgValueFrom(arg, valStr);
    }

    public static void setArgValueFrom(LogicTableArg arg, RundateAST rhs) {
        //This is where we need some magic to map the constant
        Cookie cookie;
        switch(rhs.getValue()) {
            case "RUNDAY":
            cookie = new Cookie(Cookie.LTDateRunDay,  null);
            break;
            case "RUNMONTH":
            cookie = new Cookie(Cookie.LTDateRunMonth,  null);
            break;
            case "RUNYEAR":
            cookie = new Cookie(Cookie.LTDateRunYear,  null);
            break;
            default:
            //Error time
            cookie = null;
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
        cookie.setIntegerData(val);
        arg.setValue(cookie);
    }

    public static void setArgVal(ExtractBaseAST node, LogicTableArg arg) {
        if(node.getType() == Type.NUMATOM) {
            arg.setValue(new Cookie(((NumAtomAST)node).getValueString()));
        } else if(node.getType() == Type.DATEFUNC) {
            arg.setValue(new Cookie(((DateFunc)node).getValue()));
        } else if(node.getType() == Type.FISCALDATE) {
            FiscaldateAST fn = ((FiscaldateAST)node);
            arg.setValue(new Cookie(fn.getCookieCode(), fn.getValue()));
            arg.setFieldContentId(fn.getDateCode());
        } else if(node.getType() == Type.RUNDATE) {
            RundateAST rd = ((RundateAST)node);
            arg.setValue(new Cookie(rd.getCookieCode(), rd.getValue()));
            arg.setFieldContentId(rd.getDateCode());
        } else {
            arg.setValue(new Cookie(((StringAtomAST)node).getValue()));
        }
    }

    public static String getStringFromNodeVal(ExtractBaseAST rhs) {

        // We should not be doing this
        // Add a GersValue interface to those node that supply a value
        if(rhs.getType() == Type.NUMATOM) {
            return ((NumAtomAST)rhs).getValueString();
        } else if(rhs.getType() == Type.DATEFUNC) {
            return ((DateFunc)rhs).getValue();
        } else if(rhs.getType() == Type.RUNDATE) {
            return ((RundateAST)rhs).getValue();
        } else {
            return ((StringAtomAST)rhs).getValue();
        }
    }

}
