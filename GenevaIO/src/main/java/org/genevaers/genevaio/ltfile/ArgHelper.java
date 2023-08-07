package org.genevaers.genevaio.ltfile;

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


import java.nio.ByteBuffer;
import java.util.List;

public class ArgHelper {
    
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
    
    private ArgHelper() { }

    public static void setArgValueFrom(LogicTableArg arg, String valStr) {
        setValBuffer(arg, valStr.getBytes());
        arg.setValueLength(valStr.length());
    }

    public static void setArgValueFrom(LogicTableArg arg, int val) {
        String valStr = Integer.toString(val);
        setValBuffer(arg, valStr.getBytes());
        //arg.setValueLength(valStr.length());
    }


    public static String getArgString(LogicTableArg arg) {
        return arg.getValue();        
    }

    public static void setValBuffer(LogicTableArg arg, byte[] in) {
        String val = new String(in);
        arg.setValue(val);
    }

}
