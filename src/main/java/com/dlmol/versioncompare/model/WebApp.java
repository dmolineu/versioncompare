package com.dlmol.versioncompare.model;

public class WebApp {

    private String name;
    private String version;

    public WebApp() {
        super();
    }

    public WebApp(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebApp webApp = (WebApp) o;

        if (!getName().equals(webApp.getName())) return false;
        return getVersion().equals(webApp.getVersion());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getVersion().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "WebApp{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}