package com.dlmol.versioncompare.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Cell {

    @Getter @Setter
    String displayText;

    @Getter @Setter
    String altText;

    @Getter @Setter
    boolean isConsistentVersion;

    @Getter @Setter
    boolean isAnomoly;

    public Cell(String displayText, String altText) {
        this.displayText = displayText;
        this.altText = altText;
    }
}
