package org.genevaers.runcontrolanalyser.ltcoverage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class LtCoverageYamlWriter {

    private static ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());;
    
    public static void writeYaml(Path output, LTCoverageFile ltcovFile) {
        try {
            yamlMapper.writeValue(output.toFile(), ltcovFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
