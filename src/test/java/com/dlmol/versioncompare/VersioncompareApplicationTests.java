package com.dlmol.versioncompare;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VersioncompareApplicationTests {

    @Value("#{'${webapp.dir.list}'.split(',')}")
    private List<String> webappDirs;

	@Test
	public void contextLoads() {
	}

    @Test
    public void testGetWebAppDirs(){
        assertNotNull(webappDirs);
        assertTrue("Expected webappsDirs size to be 3, found: " + webappDirs.size(),
                webappDirs.size() == 3);
        System.out.println("Using webappDirs:");
        for(String dir : webappDirs) {
            System.out.println("\t" + dir);
            final File webappDir = new File(dir);
            assertTrue("'" + dir + "' is NOT a directory!", webappDir.isDirectory());
            File[] webapps = webappDir.listFiles();
        }

    }

}
