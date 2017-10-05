package com.dlmol.versioncompare.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EnvUtil {
    private static final Logger logger = LoggerFactory.getLogger(EnvUtil.class);

    @Autowired
    private Environment env;

    public Map<String, Object> getAllProperties() {
        Map<String, Object> map = new HashMap();
        for (Iterator it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
            org.springframework.core.env.PropertySource propertySource = (org.springframework.core.env.PropertySource) it.next();
            if (propertySource instanceof MapPropertySource) {
                map.putAll(((MapPropertySource) propertySource).getSource());
            }
        }
        return map;
        /*
        List<String> propKeys = new ArrayList<>(map.entrySet().size());
        //loop a Map
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            logger.debug("Key : " + entry.getKey() + " Value : " + String.valueOf(entry.getValue()));
            propKeys.add(entry.getKey());
        }
        return propKeys;
        */
    }
}