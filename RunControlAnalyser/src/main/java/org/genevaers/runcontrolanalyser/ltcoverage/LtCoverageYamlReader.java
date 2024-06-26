package org.genevaers.runcontrolanalyser.ltcoverage;

import java.io.IOException;
import java.nio.file.Path;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.flogger.FluentLogger;

public class LtCoverageYamlReader {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private static ObjectMapper yamlMapper;
    private static LTCoverageFile ltCovFile;

    public static LTCoverageFile readYaml(Path input) {
        yamlMapper = new ObjectMapper(new YAMLFactory());
        yamlMapper.findAndRegisterModules();
        try {
            ltCovFile = yamlMapper.readValue(input.toFile(), LTCoverageFile.class);
        } catch (IOException e) {
            logger.atSevere().log("read coverage failed\n%s", e.getMessage());
        };
        return ltCovFile;
    }

}
