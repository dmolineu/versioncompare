package com.dlmol.versioncompare.controller;

import com.dlmol.versioncompare.contents.WebAppDirContentParser;
import com.dlmol.versioncompare.model.WebApp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.util.List;

@Component
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(RestController.class);

    @Value("#{'${webapp.dir.list}'.split(',')}")
    private List<String> webappDirs;

    @RequestMapping(value = "/")
    public String root() {
        return config();
    }

    @RequestMapping()
    public String config() {
        return "config:" + webappDirs;
    }

    @RequestMapping(value = "/compare")
    public String compare() {
        String out = "<html>";

        for(String dir : webappDirs) {
            out += "<br>" + dir + ":<br>";
            final File webappDir = new File(dir);
            List<WebApp> webapps = WebAppDirContentParser.getWebApps(webappDir);
            for (int i=0; i<webapps.size(); i++)
                out += "<br><nbsp><nbsp><nbsp><nbsp><nbsp>*" + webapps.get(i).getName() + " (" + webapps.get(i).getVersion() + ")<br>";
        }
        out += "</htm>";
        return out;
    }
}