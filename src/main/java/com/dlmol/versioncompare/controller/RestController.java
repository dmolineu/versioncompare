package com.dlmol.versioncompare.controller;

import com.dlmol.versioncompare.contents.WebAppDirContentParser;
import com.dlmol.versioncompare.display.DisplayGrid;
import com.dlmol.versioncompare.model.WebApp;
import com.dlmol.versioncompare.model.WebAppsDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.util.*;

@Component
@org.springframework.web.bind.annotation.RestController
public class RestController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    WebAppDirContentParser webAppParser;

    @Value("#{'${webapp.dir.list}'.split(',')}")
    private List<String> webappDirs;

    @Value("#{'${webapp.dir.name.list}'.split(',')}")
    private List<String> webappDirNames;

    @RequestMapping(value = "/")
    public String root() {
        return config();
    }

    @RequestMapping()
    public String config() {
        return "config:" + webappDirs;
    }

    //TODO: Serve jsp
    //TODO: Color-code output, black all the same, red for outlier
    @RequestMapping() //value = "/compare")
    public String compare() {
        if (webappDirs.size() != webappDirNames.size())
            return "Config Error! webappDirs and webappDirNames have different lengths!";

        List<WebAppsDirectory> webAppDirsContents = new ArrayList<>(webappDirs.size());
        for(int i=0; i<webappDirs.size(); i++) {
            String dir = webappDirs.get(i);
            final File webappDir = new File(dir);
            List<WebApp> webapps = webAppParser.getWebApps(webappDir);
            webAppDirsContents.add(new WebAppsDirectory(webappDir, webappDirNames.get(i), webapps));
        }

        Map<String, List<WebApp>> data = new HashMap<>();
        for (WebAppsDirectory webAppsDir : webAppDirsContents)
            data.put(webAppsDir.getDisplayName(), webAppsDir.getWebApps());
        DisplayGrid dg = new DisplayGrid(data, webappDirNames);
        return  "\n<html>\n" + dg.getGridHtmlTable() + "\n</htm>";
    }
}