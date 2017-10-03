package com.dlmol.versioncompare.display;

import com.dlmol.versioncompare.model.WebApp;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DisplayGrid {
//    private Cell

    private Set<String> masterAppList = null;

    public DisplayGrid(Map<String, List<WebApp>> webappDirsContents) {
        int totalRowCount = getTotalRowCount(webappDirsContents);
    }

    private int getTotalRowCount(Map<String, List<WebApp>> webappDirsContents) {
        if (masterAppList == null) {
            masterAppList = new HashSet<>(30);
        } else {
            return masterAppList.size();
        }
        if (webappDirsContents == null)
            return 0;
        for (List<WebApp> webApps: webappDirsContents.values()) {
            webApps.forEach(it -> masterAppList.add(it.getName()));
        }

        return masterAppList.size();
    }


}
