package org.genevaers.compilers.extract.astnodes;

import org.genevaers.compilers.base.EmittableASTNode;
import org.genevaers.repository.components.LookupPath;

import com.google.common.flogger.FluentLogger;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008.
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


public class LookupPathRefAST extends ExtractBaseAST {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private LookupPathHandler lookupHandler = new LookupPathHandler();

    public LookupPathRefAST() {
        type = ASTFactory.Type.LOOKUPREF;
    }

    public void setLookup(LookupPath lookup) {
        lookupHandler.setLookup(lookup);
    }

    @Override
    public void resolveGotos(Integer compT, Integer compF, Integer joinT, Integer joinF) {
        lookupHandler.resolveGotos(joinT, joinF);
    }

    public void emitJoin(boolean skt) {
        lookupHandler.emitJoin(skt);
    }

    public String getName() {
        return lookupHandler.getLookup().getName();
    }

    public String getUniqueKey() {
        return lookupHandler.getUniqueKey();
    }

    public int getNewJoinId() {
        return lookupHandler.getNewJoinId();
    }

    public void setSymbols(SymbolList symbols) {
        lookupHandler.setSymbols(symbols);
    }

    public void setEffDateValue(EffDateValue effDateValue) {
        lookupHandler.setEffDateValue(effDateValue);
    }

    public void makeUnique() {
        lookupHandler.makeUnique();
    }

}
