package org.genevaers.genevaio.dataprovider;

import java.util.Map;

import org.genevaers.repository.Repository;
import org.genevaers.repository.components.LogicalRecord;
import org.genevaers.repository.components.LookupPath;
import org.genevaers.repository.components.ViewNode;

/*
 * Merging MR91 functionality into the workbench means we use the
 * CompilerDataProvider in the BuildGenevaASTVisitor class.
 * 
 * For a stand alone MR91 there is no database involved. So we use this
 * to interact directly with the Repository, which will have already been
 * populated with the data required.
 */
public class RepoDataProvider implements CompilerDataProvider{

    @Override
    public Integer findExitID(String string, boolean procedure) {
        return 0;
    }

    @Override
    public Integer findPFAssocID(String lfName, String pfName) {
        return 0;
    }

    @Override
    public Map<String, Integer> getFieldsFromLr(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFieldsFromLr'");
    }

    @Override
    public Map<String, Integer> getLookupTargetFields(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLookupTargetFields'");
    }

    @Override
    public void setEnvironmentID(int environmentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEnvironmentID'");
    }

    @Override
    public int getEnvironmentID() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnvironmentID'");
    }

    @Override
    public void setLogicalRecordID(int lrid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLogicalRecordID'");
    }

    @Override
    public LogicalRecord getLogicalRecord(int id) {
        return Repository.getLogicalRecords().get(id);
    }

    @Override
    public void loadLR(int environmentID, int sourceLR) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadLR'");
    }

    @Override
    public LookupPath getLookup(String name) {
        return Repository.getLookups().get(name);
    }

    @Override
    public ViewNode getView(int id) {
        return Repository.getViews().get(id);
    }
    
}
