package com.dlmol.versioncompare;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VersioncompareApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(VersioncompareApplicationTests.class);

    @Value("#{'${webapp.dir.path.list}'.split(',')}")
    private List<String> webappDirs;

	@Test
	public void contextLoads() {
	}

    @Test
    public void testGetWebAppDirs(){
        assertNotNull(webappDirs);
        assertTrue("Expected webappsDirs size to be 3, found: " + webappDirs.size(),
                webappDirs.size() == 3);
        logger.debug("Using webappDirs:");
        for(String dir : webappDirs) {
            logger.debug("\t" + dir);
            final File webappDir = new File(dir);
            assertTrue("'" + dir + "' is NOT a directory!", webappDir.isDirectory());
            File[] webapps = webappDir.listFiles();
        }

    }

    //TODO: Needs lots more tests!
}
