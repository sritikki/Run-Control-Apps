package org.genevaers.runcontrolanalyser.ltcoverage;

import java.io.IOException;
import java.nio.file.Path;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.flogger.FluentLogger;

public class LtCoverageYamlWriter {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private static ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());;
    
    public static void writeYaml(Path output, LTCoverageFile ltcovFile) {
        try {
            yamlMapper.writeValue(output.toFile(), ltcovFile);
        } catch (IOException e) {
            logger.atSevere().log("write coverage failed\n%s", e.getMessage());
        }
    }

}
