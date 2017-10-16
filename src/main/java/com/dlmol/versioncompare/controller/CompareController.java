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
import org.springframework.ui.Model;
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
    @ResponseBody
    public String compare(@PathVariable String propKey) {
        CompareConfiguration config = null;
        String keySetJoined = null;
        try {
            config = new CompareConfiguration(propKey, env.getAllProperties());
        } catch (CompareConfigurationException e) {
            keySetJoined = "\"" + String.join("\", \"", config.getAvailableCompareSets(env.getAllProperties())) + "\"";
            final String msg = "Unable to retrieve properties for \"" + propKey + "\". " + e.getMessage() +
                    " Known comparison sets are: " + keySetJoined;
            logger.error(msg, e);
            return msg;
        }
        if (propKey == null || propKey.length() == 0) {
            final String msg = "'propKey' parameter is missing! Configured keys are: \"" +
                    keySetJoined;
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
                dg.getGridHtmlTable() + "\n<div>All Available Compare Keys: " + keySetJoined + "<div>\n</html>";
    }

    @RequestMapping("/jsp")
    public String jsp(Model model) {
        model.addAttribute("russian", "Добрый день");
        return "compare";
    }
}
