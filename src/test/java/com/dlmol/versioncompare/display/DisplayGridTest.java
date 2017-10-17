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
        testDirApps.add(new WebApp("b", "b: build 2"));
        testDirApps.add(new WebApp("c", "c: build 1"));

        List<WebApp> modlDirApps = new ArrayList<>();
        modlDirApps.add(new WebApp("a", "a: build 1"));
        modlDirApps.add(new WebApp("b", "b: build 2"));
        modlDirApps.add(new WebApp("c", "c: build 2"));

        List<WebApp> prodDirApps = new ArrayList<>();
        prodDirApps.add(new WebApp("a", "a: build 1"));
        prodDirApps.add(new WebApp("b", "b: build 1"));
        prodDirApps.add(new WebApp("c", "c: build 3"));

        contents = new HashMap<>(3);

        contents.put(TEST_DIR, testDirApps);
        contents.put(MODL_DIR, modlDirApps);
        contents.put(PROD_DIR, prodDirApps);
    }

    @Test
    public void testSetup() throws Exception {
        assertEquals("a: build 1", contents.get(TEST_DIR).get(0).getVersion());
        assertEquals("a: build 1", contents.get(MODL_DIR).get(0).getVersion());
        assertEquals("a: build 1", contents.get(PROD_DIR).get(0).getVersion());

        assertEquals("b: build 2", contents.get(TEST_DIR).get(1).getVersion());
        assertEquals("b: build 2", contents.get(MODL_DIR).get(1).getVersion());
        assertEquals("b: build 1", contents.get(PROD_DIR).get(1).getVersion());
    }

    @Test
    public void testGrid() throws Exception {
        assertEquals(3, dirNames.size());
        assertEquals(3, contents.size());
        DisplayGrid displayGrid = new DisplayGrid(contents, dirNames);
        Cell[][] grid = displayGrid.getGrid();
        int row = 0;
        int col = 0;

        //row 0 - Heading
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "#", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                null, grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                false, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());

        col++;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                TEST_DIR, grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                null, grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                false, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());

        col++;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                MODL_DIR, grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                null, grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                false, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());

        col++;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                PROD_DIR, grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                null, grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                false, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());

        //row 1 - 'a: build 1'
        row++;
        col = 0;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "1", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                null, grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                false, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());

        col++;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "a", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                "a: build 1", grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                false, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                true, grid[row][col].isConsistentVersion());

        col++;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "a", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                "a: build 1", grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                false, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                true, grid[row][col].isConsistentVersion());

        col++;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "a", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                "a: build 1", grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                false, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                true, grid[row][col].isConsistentVersion());

        //row 2 - 'b: build 1/2'
        row++;
        col = 0;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "2", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                null, grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                false, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());

        col++;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "b", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                "b: build 2", grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                false, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());

        col++;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "b", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                "b: build 2", grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                false, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());

        col++;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "b", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                "b: build 1", grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                true, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());

        //row 3 - 'c: build 1,2,3'
        row++;
        col = 0;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "3", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                null, grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                false, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());

        col++;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "c", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                "c: build 1", grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                true, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());

        col++;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "c", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                "c: build 2", grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                true, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());

        col++;
        printLog(row, col, grid[row][col].toString());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getDisplayText() == " + grid[row][col].getDisplayText(),
                "c", grid[row][col].getDisplayText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].getAltText() == " + grid[row][col].getAltText(),
                "c: build 3", grid[row][col].getAltText());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isAnomoly() == " + grid[row][col].isAnomoly(),
                true, grid[row][col].isAnomoly());
        assertEquals("row: " + row + ", col: " + col + ", grid[row][col].isConsistentVersion() == " + grid[row][col].isConsistentVersion(),
                false, grid[row][col].isConsistentVersion());
    }

    private void printLog(int row, int col, String cellToString) {
        System.out.println("Testing row: " + row + ", col: " + col + ": " + cellToString);
    }
}