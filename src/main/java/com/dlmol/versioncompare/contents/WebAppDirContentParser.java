package com.dlmol.versioncompare.contents;

import com.dlmol.versioncompare.model.WebApp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import org.apache.commons.io.FileUtils;

@Component
public class WebAppDirContentParser {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(WebAppDirContentParser.class);

    private static final String VERSION_TXT_FILE_NAME = "version.txt";
    private static final String VERSION_TXT_READ_FAILURE_MSG = "Unable to read version.txt!";
    private static final String NO_VERSION_TAG_MSG = "*** No version.txt file found. ***";

    @Value("#{'${webapp.ignore.list}'.split(',')}")
    private List<String> webappIgnoreList;

    public List<WebApp> getWebApps (File webappsDir) {
        List<WebApp> webApps = new ArrayList<>();
        if (webappsDir == null || webappsDir.isDirectory() == false) {
            LOGGER.warn("getWebApps 'webappsDir' is null or not a directory!");
            return webApps;
        }

        for (File webApp : webappsDir.listFiles(pathname ->
                pathname.isDirectory() && webappIgnoreList.contains(pathname.getName().toLowerCase()) == false)){
            LOGGER.debug("Examining file: '" + webApp + "', isDir == " + webApp.isDirectory());
            File versionTxtFile = new File(webApp.getPath() + File.separator + VERSION_TXT_FILE_NAME);
            String tagText;
            if (versionTxtFile == null || versionTxtFile.isFile() == false)
                tagText = NO_VERSION_TAG_MSG;
            else
                //TODO: FileUtils
//                try {
//                    tagText = FileUtils.readFileToString(versionTxtFile);
                    tagText = "Switch to FileUtils to read file.";
//                } catch (IOException e) {
//                    LOGGER.error("Unable to read text from verstion.txt file!", e);
//                    tagText = VERSION_TXT_READ_FAILURE_MSG;
//                }
            webApps.add(new WebApp(webApp.getName(), tagText));
        }

        LOGGER.info("Returning " + webApps.size() + " web apps under directory: " + webappsDir.getAbsolutePath());
        return webApps;
    }
}