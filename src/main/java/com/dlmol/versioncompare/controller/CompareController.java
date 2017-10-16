package com.dlmol.versioncompare.controller;

import com.dlmol.versioncompare.contents.WebAppDirContentParser;
import com.dlmol.versioncompare.display.DisplayGrid;
import com.dlmol.versioncompare.env.CompareConfiguration;
import com.dlmol.versioncompare.env.EnvUtil;
import com.dlmol.versioncompare.exception.CompareConfigurationException;
import com.dlmol.versioncompare.model.WebApp;
import com.dlmol.versioncompare.model.WebAppsDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
public class CompareController {
    private static final Logger logger = LoggerFactory.getLogger(CompareController.class);

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
        CompareConfiguration config = null;
        try {
            config = new CompareConfiguration(propKey, env.getAllProperties());
        } catch (CompareConfigurationException e) {
            final String msg = "Unable to retrieve properties for \"" + propKey + "\". Known property keys are: \n" +
                    getKeyLinks(config.getAvailableCompareSets(env.getAllProperties())) + e.getMessage();
            logger.error(msg, e);
            return msg;
        }
        final String keySetJoined =
                "\"" + String.join("\", \"", config.getAvailableCompareSets(env.getAllProperties())) + "\"";
        if (propKey == null || propKey.length() == 0) {
            final String msg = "'propKey' parameter is missing! Configured keys are: \n" +
                    getKeyLinks(config.getAvailableCompareSets(env.getAllProperties()));
            logger.error(msg);
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
        return  "<html>\n" +
                "<h1>\"" + propKey + "\" Comparison</h1>\n" +
                dg.getGridHtmlTable() + "\n<div><h3>All Available Compare Keys:</h3>" +
                getKeyLinks(config.getAvailableCompareSets(env.getAllProperties())) + "</div>\n</html>";
    }

    private String getKeyLinks(List<String> availableCompareSets) {
        if (availableCompareSets == null || availableCompareSets.size() == 0)
            return "";
        StringBuffer list = new StringBuffer("<ul>\n");
        for (String set : availableCompareSets){
            list = list.append("\t<li><a href=\"").append(set).append("\">").append(set).append("</a></li>\n");
        }
        list.append("</ul>\n");
        return list.toString();
    }
}