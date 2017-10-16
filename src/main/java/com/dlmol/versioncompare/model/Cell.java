package com.dlmol.versioncompare.model;

import lombok.ToString;

@ToString
public class Cell {
    String displayText;
    String altText;
    boolean isConsistentVersion;
    boolean isAnomoly;

    public Cell(String displayText, String altText) {
        this.displayText = displayText;
        this.altText = altText;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public boolean isConsistentVersion() {
        return isConsistentVersion;
    }

    public void setConsistentVersion(boolean consistentVersion) {
        isConsistentVersion = consistentVersion;
        isAnomoly = !consistentVersion;
    }

    public boolean isAnomoly() {
        return isAnomoly;
    }

    public void setAnomoly(boolean anomoly) {
        isAnomoly = anomoly;
        isConsistentVersion = !anomoly;
    }
}
