package org.genevaers.runcontrolanalyser.ltcoverage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class LtCoverageYamlWriter {

    private ObjectMapper yamlMapper;
    private ObjectNode coverageRoot;
    private Map<String, LtCoverageEntry> coverageMap;
    
    public void writeTo(Path filename, Map<String, LtCoverageEntry> coverageMap) {
        this.coverageMap = coverageMap;
        addRecordFieldToYamlTree();
        writeYaml(filename);
    }

    private void writeYaml(Path output) {
        try {
            yamlMapper.writeValue(output.toFile(), coverageRoot);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addRecordFieldToYamlTree() {
        yamlMapper = new ObjectMapper(new YAMLFactory());
        coverageRoot = yamlMapper.createObjectNode();
        addDataToRoot();
    }

    private void addDataToRoot() {
        coverageRoot.put("ltcoverage", "lt");
        ArrayNode fieldsArray = coverageRoot.putArray("functionCodes");
        Iterator<LtCoverageEntry> fcci = coverageMap.values().iterator();
        while(fcci.hasNext()) {
            LtCoverageEntry fcc = fcci.next();
            switch(fcc.getType()){
                case CALC:
                case CC:
                case F0:
                case GENERATION:
                case HD:
                case INVALID:
                case NAME:
                case NAMEVALUE:
                case NV:
                case RE:
                case WR:
                    fieldsArray.addPOJO(fcc);
                    break;
                case F1:
                case NAMEF1:
                    addTypeHits(fieldsArray, fcc);
                    break;
                case F2:
                case NAMEF2:
                    addTypeMatrix(fieldsArray, fcc);
                    break;
                default:
                    break;
                
            }
        }
    }

    private void addTypeMatrix(ArrayNode fieldsArray, LtCoverageEntry fcc) {
        ObjectNode hits = fieldsArray.addObject();
        hits.put("name", fcc.getName());        
        hits.put("type", fcc.getType().toString());
        hits.put("hits", fcc.getHits());
        hits.put("expectedItems", fcc.getExpectedItems());
        hits.put("description", fcc.getDescription());
        hits.put("category", fcc.getCategory());
        ArrayNode hitsMatrix = yamlMapper.createArrayNode();
        hits.set("typeHitsMatrix", hitsMatrix);
        Iterator<Entry<String, TypeHitMap>> typeMatrixI = ((LTCoverageEntry2Args)fcc).getTypeMatrixIterator();
        while(typeMatrixI.hasNext()) {
            Entry<String, TypeHitMap> thm = typeMatrixI.next();
            ObjectNode m = hitsMatrix.addObject();
            ArrayNode hitsArray = m.putArray(thm.getKey().toString());
            Iterator<Entry<String, Integer>> typeHitsI = (thm.getValue()).getTypeHitsIterator();
            while(typeHitsI.hasNext()) {
                Entry<String, Integer> the = typeHitsI.next();
                ObjectNode h = hitsArray.addObject();
                h.put(the.getKey().toString(), the.getValue());
            }
        }
    }

    private void addTypeHits(ArrayNode fieldsArray, LtCoverageEntry fcc) {
        ObjectNode hits = fieldsArray.addObject();
        hits.put("name", fcc.getName());        
        hits.put("type", fcc.getType().toString());
        hits.put("hits", fcc.getHits());
        hits.put("expectedItems", fcc.getExpectedItems());
        hits.put("description", fcc.getDescription());
        hits.put("category", fcc.getCategory());
        ArrayNode hitsArray = yamlMapper.createArrayNode();
        hits.set("typeHits", hitsArray);
        Iterator<Entry<String, Integer>> typeHitsI = ((LTCoverageEntry1Arg)fcc).getTypeHitsIterator();
        while(typeHitsI.hasNext()) {
            Entry<String, Integer> the = typeHitsI.next();
            ObjectNode h = hitsArray.addObject();
            h.put(the.getKey().toString(), the.getValue());
        }
        

    }}
