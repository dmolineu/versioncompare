package com.dlmol.versioncompare.contents;

import com.dlmol.versioncompare.model.WebApp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
//import org.apache.commons.io.FileUtils;

public class WebAppDirContentParser {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(WebAppDirContentParser.class);
    public static final String VERSION_TXT_FILE_NAME = "version.txt";

    private static String NO_VERSION_TAG_MSG = "*** No version.txt file found. ***";

    public static List<WebApp> getWebApps (File webappsDir) {
        List<WebApp> webApps = new ArrayList<>();
        if (webappsDir == null || webappsDir.isDirectory() == false) {
            LOGGER.warn("getWebApps 'webappsDir' is null or not a directory!");
            return webApps;
        }

        for (File webApp : webappsDir.listFiles(pathname -> pathname.isDirectory())){
            LOGGER.debug("Examining file: '" + webApp + "', isDir == " + webApp.isDirectory());
            File versionTxtFile = new File(webApp.getPath() + File.separator + VERSION_TXT_FILE_NAME);
            String tagText;
            if (versionTxtFile == null || versionTxtFile.isFile() == false)
                tagText = NO_VERSION_TAG_MSG;
            else
                tagText = "Build version text here"; // FileUtils.readFileToString(versionTxtFile);
            webApps.add(new WebApp(webApp.getName(), tagText));
        }

        LOGGER.info("Returning " + webApps.size() + " web apps under directory: " + webappsDir.getAbsolutePath());
        return webApps;
    }
}