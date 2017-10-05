package com.dlmol.versioncompare.model;

import com.dlmol.versioncompare.controller.RestController;
import com.dlmol.versioncompare.exception.CompareConfigurationException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CompareConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(CompareConfiguration.class);

    private final static String PATH_DIR_LIST_PROP_KEY_PREFIX = "webapp.dir.path.list";
    private final static String PATH_NAME_LIST_PROP_KEY_PREFIX = "webapp.dir.name.list";

    public List<String> getWebappDirs() {
        return webappDirs;
    }

    public List<String> getWebappDirNames() {
        return webappDirNames;
    }

    @Getter
    List<String> webappDirs = null;

    @Getter
    List<String> webappDirNames = null;

    //TODO: Put friendly names in parens to eliminate need for second prop.
    public CompareConfiguration(String comparePropKey, Map<String, Object> envProps) throws CompareConfigurationException {
        final String desiredPathsKey = PATH_DIR_LIST_PROP_KEY_PREFIX + "." + comparePropKey;
        final String desiredNamesKey = PATH_NAME_LIST_PROP_KEY_PREFIX + "." + comparePropKey;

        for (String key : envProps.keySet()) {
            logger.trace("\tInspecting property: \"" + key + "\"");
            if (desiredPathsKey.equalsIgnoreCase(key))
                webappDirs = splitAndGetListForKey(envProps, desiredPathsKey);
            else if (desiredNamesKey.equalsIgnoreCase(key))
                webappDirNames = splitAndGetListForKey(envProps, desiredNamesKey);
        }
        if (webappDirs == null)
            throw new CompareConfigurationException("Property \"" + desiredPathsKey + "\" NOT found!");
        if (webappDirNames == null)
            throw new CompareConfigurationException("Property \"" + desiredNamesKey + "\" NOT found!");
        if (webappDirs.size() != webappDirNames.size())
            throw new CompareConfigurationException("Property \"" + desiredPathsKey + "\" has size '" +
                webappDirs.size() + "', while property \"" + desiredNamesKey + "\" has size '" + webappDirNames.size() +
                "'. These MUST be the same.");
    }

    private List<String> splitAndGetListForKey(Map<String, Object> envProps, String key) {
        final String value = String.valueOf(envProps.get(key));
        logger.debug("For propKey \"" + key + "\", found value: \"" + value + "\"");
        return Arrays.asList(value.split(","));
    }
}
