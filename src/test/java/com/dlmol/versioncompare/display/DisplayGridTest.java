package com.dlmol.versioncompare.display;

import com.dlmol.versioncompare.model.Cell;
import com.dlmol.versioncompare.model.WebApp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DisplayGridTest {
    public static final String TEST_DIR = "test";
    public static final String MODL_DIR = "modl";
    public static final String PROD_DIR = "prod";
    Map<String, List<WebApp>> contents;
    List<String> dirNames;

    @Before
    public void setUp() throws Exception {
        dirNames = new ArrayList<>(3);
        dirNames.add(TEST_DIR);
        dirNames.add(MODL_DIR);
        dirNames.add(PROD_DIR);

        List<WebApp> testDirApps = new ArrayList<>();
        testDirApps.add(new WebApp("a", "a: build 1"));
        testDirApps.add(new WebApp("b", "a: build 2"));

        List<WebApp> modlDirApps = new ArrayList<>();
        modlDirApps.add(new WebApp("a", "a: build 1"));
        modlDirApps.add(new WebApp("b", "a: build 2"));

        List<WebApp> prodDirApps = new ArrayList<>();
        prodDirApps.add(new WebApp("a", "a: build 1"));
        prodDirApps.add(new WebApp("b", "a: build 1"));

        contents = new HashMap<>(3);

        contents.put(TEST_DIR, testDirApps);
        contents.put(MODL_DIR, modlDirApps);
        contents.put(PROD_DIR, prodDirApps);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGrid() throws Exception {
        assertEquals(3, dirNames.size());
        assertEquals(3, contents.size());
        DisplayGrid displayGrid = new DisplayGrid(contents, dirNames);
        Cell[][] grid = displayGrid.getGrid();
        int row = 0;
        int col = 0;

        //row 1
        assertEquals("#", grid[row][col].getDisplayText());
        assertEquals(null, grid[row][col].getAltText());
        assertEquals(false, grid[row][col].isAnomoly());
        assertEquals(false, grid[row][col].isConsistentVersion());

        assertEquals(TEST_DIR, grid[row][++col].getDisplayText());
        assertEquals(null, grid[row][col].getAltText());
        assertEquals(false, grid[row][col].isAnomoly());
        assertEquals(false, grid[row][col].isConsistentVersion());

        assertEquals(MODL_DIR, grid[row][++col].getDisplayText());
        assertEquals(null, grid[row][col].getAltText());
        assertEquals(false, grid[row][col].isAnomoly());
        assertEquals(false, grid[row][col].isConsistentVersion());

        assertEquals(PROD_DIR, grid[row][++col].getDisplayText());
        assertEquals(null, grid[row][col].getAltText());
        assertEquals(false, grid[row][col].isAnomoly());
        assertEquals(false, grid[row][col].isConsistentVersion());

        //row 2
        col = 0;
        assertEquals("1", grid[++row][col].getDisplayText());
        assertEquals(null, grid[row][col].getAltText());
        assertEquals(false, grid[row][col].isAnomoly());
        assertEquals(false, grid[row][col].isConsistentVersion());

        assertEquals("a", grid[row][++col].getDisplayText());
        assertEquals("a: build 1", grid[row][col].getAltText());
        assertEquals(false, grid[row][col].isAnomoly());
        assertEquals(true, grid[row][col].isConsistentVersion());

        assertEquals("a", grid[row][++col].getDisplayText());
        assertEquals("a: build 1", grid[row][col].getAltText());
        assertEquals(false, grid[row][col].isAnomoly());
        assertEquals(true, grid[row][col].isConsistentVersion());

        assertEquals("a", grid[row][++col].getDisplayText());
        assertEquals("a: build 1", grid[row][col].getAltText());
        assertEquals(false, grid[row][col].isAnomoly());
        assertEquals(true, grid[row][col].isConsistentVersion());

    }

}