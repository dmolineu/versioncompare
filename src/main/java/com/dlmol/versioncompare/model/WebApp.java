package com.dlmol.versioncompare.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class WebApp implements Comparable{

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String version;

    @Override
    public int compareTo(Object o) {
        if (o == null || getClass() != o.getClass())
            return 0;
        WebApp that = (WebApp) o;
        if (this.getName().equalsIgnoreCase(that.getName())) {
            if (this.getVersion().equalsIgnoreCase(that.getVersion()))
                return 0;
            else
                return this.getVersion().compareTo(that.getVersion());
        } else
            return this.getName().compareTo(that.getName());
    }
}
