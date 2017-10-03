package com.dlmol.versioncompare.model;

import java.io.File;
import java.util.List;

public class WebAppsDirectory {
    private File webAppDir;
    private String displayName;
    private List<WebApp> webApps;

    public WebAppsDirectory(File webAppDir, String displayName, List<WebApp> webApps) {
        this.webAppDir = webAppDir;
        this.displayName = displayName;
        this.webApps = webApps;
    }

    public File getWebAppDir() {
        return webAppDir;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<WebApp> getWebApps() {
        return webApps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebAppsDirectory that = (WebAppsDirectory) o;

        if (!getWebAppDir().equals(that.getWebAppDir())) return false;
        if (!getDisplayName().equals(that.getDisplayName())) return false;
        return getWebApps().equals(that.getWebApps());
    }

    @Override
    public int hashCode() {
        int result = getWebAppDir().hashCode();
        result = 31 * result + getDisplayName().hashCode();
        result = 31 * result + getWebApps().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "WebAppsDirectory{" +
                "webAppDir=" + webAppDir +
                ", displayName='" + displayName + '\'' +
                ", webApps=" + webApps +
                '}';
    }
}
