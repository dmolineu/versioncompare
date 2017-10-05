package com.dlmol.versioncompare.controller;

import com.dlmol.versioncompare.contents.WebAppDirContentParser;
import com.dlmol.versioncompare.display.DisplayGrid;
import com.dlmol.versioncompare.env.EnvUtil;
import com.dlmol.versioncompare.exception.CompareConfigurationException;
import com.dlmol.versioncompare.model.CompareConfiguration;
import com.dlmol.versioncompare.model.WebApp;
import com.dlmol.versioncompare.model.WebAppsDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@org.springframework.web.bind.annotation.RestController
public class RestController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    WebAppDirContentParser webAppParser;

    @Autowired
    EnvUtil env;

//    TODO: Clean up/re-enable.
//    @RequestMapping(value = "/")
//    public String root() {
//        return config();
//    }
//
//    @RequestMapping()
//    public String config() {
//        return "config:" + webappDirs;
//    }

    //TODO: Serve jsp
    //TODO: Color-code output, black all the same, red for outlier
    @RequestMapping(value="/compare/{propKey}", method = RequestMethod.GET)
    public String compare(@PathVariable String propKey) {
        if (propKey == null || propKey.length() == 0) {
            final String msg = "'propKey' parameter is missing!";
            logger.error(msg);
            return msg;
        }
        env.getAllProperties();
        CompareConfiguration config = null;
        try {
            config = new CompareConfiguration(propKey, env.getAllProperties());
        } catch (CompareConfigurationException e) {
            final String msg = "Unable to retrieve properties for \"" + propKey + "\". " + e.getMessage();
            logger.error(msg, e);
            return msg;
        }

        List<WebAppsDirectory> webAppDirsContents = new ArrayList<>(config.getWebappDirs().size());
        for(int i=0; i<config.getWebappDirs().size(); i++) {
            String dir = config.getWebappDirs().get(i);
            final File webappDir = new File(dir);
            List<WebApp> webapps = webAppParser.getWebApps(webappDir);
            webAppDirsContents.add(new WebAppsDirectory(webappDir, config.getWebappDirNames().get(i), webapps));
        }

        Map<String, List<WebApp>> data = new HashMap<>();
        for (WebAppsDirectory webAppsDir : webAppDirsContents)
            data.put(webAppsDir.getDisplayName(), webAppsDir.getWebApps());
        DisplayGrid dg = new DisplayGrid(data, config.getWebappDirNames());
        return  "\n<html>\n" + dg.getGridHtmlTable() + "\n</html>";
    }
}