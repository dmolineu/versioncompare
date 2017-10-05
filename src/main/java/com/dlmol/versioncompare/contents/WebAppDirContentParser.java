package com.dlmol.versioncompare.contents;

import com.dlmol.versioncompare.model.WebApp;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
public class WebAppDirContentParser {

    private static final Logger logger = LoggerFactory.getLogger(WebAppDirContentParser.class);

    private static final String VERSION_TXT_READ_FAILURE_MSG = "Unable to read version.txt!";
    private static final String NO_VERSION_TAG_MSG = "*** No version.txt file found. ***";

    @Value("${build.tag.file.name}")
    private String buildTagFileName;

    @Value("#{'${webapp.ignore.list}'.split(',')}")
    private List<String> webappIgnoreList;

    public List<WebApp> getWebApps (File webappsDir) {
        List<WebApp> webApps = new ArrayList<>();
        if (webappsDir == null || webappsDir.isDirectory() == false) {
            logger.warn("getWebApps 'webappsDir' is null or not a directory!");
            return webApps;
        }

        for (File webApp : webappsDir.listFiles(pathname ->
                pathname.isDirectory() && webappIgnoreList.contains(pathname.getName().toLowerCase()) == false)){
            logger.debug("Examining file: '" + webApp + "', isDir == " + webApp.isDirectory());
            File versionTxtFile = new File(webApp.getPath() + File.separator + buildTagFileName);
            webApps.add(new WebApp(webApp.getName(), getBuildTagText(versionTxtFile)));
        }

        logger.info("Returning " + webApps.size() + " web apps under directory: " + webappsDir.getAbsolutePath());
        return webApps;
    }

    private String getBuildTagText(File versionTxtFile) {
        String tagText;
        if (versionTxtFile == null || versionTxtFile.isFile() == false)
            tagText = NO_VERSION_TAG_MSG;
        else
              try {
                  tagText = FileUtils.readFileToString(versionTxtFile, Charset.defaultCharset());
              } catch (IOException e) {
                  logger.error("Unable to read text from verstion.txt file!");
                  tagText = VERSION_TXT_READ_FAILURE_MSG;
              }
        return tagText;
    }
}