package com.dlmol.versioncompare.env;

import com.dlmol.versioncompare.exception.CompareConfigurationException;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompareConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(CompareConfiguration.class);
    private final static String PATH_DIR_LIST_PROP_KEY_PREFIX = "webapp.dir.path.list";

    @Getter(AccessLevel.PUBLIC)
    List<String> webappDirs = null;

    @Getter(AccessLevel.PUBLIC)
    List<String> webappDirNames = null;

    List<String> availableCompareSets = null;

    public CompareConfiguration(String comparePropKey, Map<String, Object> envProps) throws CompareConfigurationException {
        final String desiredPathsKey = PATH_DIR_LIST_PROP_KEY_PREFIX + "." + comparePropKey;

        for (String key : envProps.keySet()) {
            logger.trace("\tInspecting property: \"" + key + "\"");
            if (desiredPathsKey.equalsIgnoreCase(key))
                webappDirs = splitAndGetListForKey(envProps, desiredPathsKey);
        }
        if (webappDirs == null)
            throw new CompareConfigurationException("Property \"" + desiredPathsKey + "\" NOT found!");
        webappDirNames = webappDirs.stream() //Get name in parenthesis for each dir
                .map(dir -> dir.contains("(") ? dir.substring(dir.indexOf("(") + 1, dir.indexOf(")")) :
                        dir.substring(Math.max(dir.lastIndexOf("/"), dir.lastIndexOf("\\")) + 1, dir.length())) //Default to path after the last '/' or '\'.
                .collect(Collectors.toList());
        webappDirs = webappDirs.stream() //Remove "({user specified name})" from each dir path.
                .map(dir -> dir.contains("(") ? dir.substring(0, dir.indexOf("(")) : dir)
                .collect(Collectors.toList());
        if (webappDirs.size() != webappDirNames.size())
            throw new CompareConfigurationException("'webappDirs' has size '" +
                    webappDirs.size() + "', while 'webappDirNames' has size '" + webappDirNames.size() +
                    "'. These MUST be the same.");
    }

    private List<String> splitAndGetListForKey(Map<String, Object> envProps, String key) {
        final String value = String.valueOf(envProps.get(key));
        logger.debug("For propKey \"" + key + "\", found value: \"" + value + "\"");
        return Arrays.asList(value.split(","));
    }

    public List<String> getAvailableCompareSets(final Map<String, Object> envProps) {
        if (availableCompareSets == null) {
            availableCompareSets = envProps.keySet().stream()
                    .filter(p -> p.contains(PATH_DIR_LIST_PROP_KEY_PREFIX))
                    .map(p -> p.substring(PATH_DIR_LIST_PROP_KEY_PREFIX.length() + 1, p.length()))
                    .sorted()
                    .collect(Collectors.toList());
        }
        logger.debug("getAvailableCompareSets: \"" + String.join("\", \"", availableCompareSets + "\"."));
        return availableCompareSets;
    }
}
