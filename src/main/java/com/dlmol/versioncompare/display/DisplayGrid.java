package com.dlmol.versioncompare.display;

import com.dlmol.versioncompare.model.Cell;
import com.dlmol.versioncompare.model.WebApp;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class DisplayGrid {
    private static final Logger logger = LoggerFactory.getLogger(DisplayGrid.class);

    @Getter(AccessLevel.PUBLIC)
    private Cell[][] grid;
    private List<String> masterAppList = null;

    public DisplayGrid(Map<String, List<WebApp>> webappDirsContents, List<String> webappDirNames) {
        int totalRowCount = getTotalRowCount(webappDirsContents);
        masterAppList = getMasterAppList(webappDirsContents);
        logger.debug("Master App List:");
        masterAppList.forEach(app -> logger.debug("\t" + app));
        buildGrid(webappDirsContents, webappDirNames);
    }

    private void buildGrid(Map<String, List<WebApp>> webappDirsContents, List<String> webappDirNames) {
        grid = new Cell[masterAppList.size() + 1][webappDirsContents.size() + 1];
        grid[0][0] = new Cell("#", null);
        List<String> appDirList = webappDirsContents.keySet().stream().collect(Collectors.toList());
        for (int row = 0; row < masterAppList.size() + 1; row++) { //Iterate rows for apps
            for (int column = 0; column < appDirList.size() + 1; column++) { //Iterate columns for webapp dirs
                final String thisWebAppDir = column == 0 ? "#" : webappDirNames.get(column - 1);
                if (row == 0) { //Headings row
                    if (column == 0)
                        grid[0][column] = new Cell("#", null);
                    else
                        grid[0][column] = new Cell(thisWebAppDir, null);
                } else { //Data rows
                    if (column == 0) // Count column
                        grid[row][0] = new Cell(String.valueOf(row), null);
                    else { //data cell
                        String appForRow = masterAppList.get(row - 1);
                        final List<String> webAppsInCurrentDir =
                                webappDirsContents.get(thisWebAppDir).stream()
                                        .filter(app -> app.getName() != null)
                                        .map(WebApp::getName)
                                        .collect(Collectors.toList());
                        if (webAppsInCurrentDir.contains(appForRow)) {
                            logger.trace(appForRow + " was found on " + thisWebAppDir);
                            List<WebApp> dirWebApps = webappDirsContents.get(thisWebAppDir);
                            String altText = null;
                            for (WebApp app : dirWebApps)
                                if (app.getName().equalsIgnoreCase(appForRow))
                                    altText = app.getVersion();
                            grid[row][column] = new Cell(appForRow, altText);
                        } else {
                            logger.debug(appForRow + " was NOT found on " + thisWebAppDir);
                            grid[row][column] = new Cell(null, null);
                        }
                    }
                }
            }
            if (row > 0) {
                logger.debug("comparing row: " + row);
                //Compare cells in row
                for (int col = 1; col < appDirList.size(); col++) {
                    if (grid[col][row] == null || grid[col][row].getAltText() == null)
                        continue;
                    for (int compareCol = 1; compareCol < appDirList.size(); compareCol++) {
                        Cell thisCell = grid[row][col];
                        final String thisCellVersion = thisCell.getAltText();
                        if (col == compareCol)
                            continue;
                        else {
                            final Cell compareCell = grid[row][compareCol];
                            final String compareCellVersion = compareCell == null ? null : compareCell.getAltText();
                            if (compareCell == null)
                                thisCell.setAnomoly(true);
                            else if (thisCellVersion.compareTo(compareCellVersion) == 0)
                                thisCell.setConsistentVersion(true);
                            else
                                thisCell.setAnomoly(true);
                        }
                    }

                }
            }
        }
        printGrid();
    }

    public String getGridHtmlTable() {
        printGrid();
        logger.debug("getGridHtmlTable():\n"); //TODO: Setup LOGGER.
        StringBuilder sb = new StringBuilder("\n<table border=1 cellpadding=3\">\n");
        for (int row = 0; row < grid.length; row++) { //iterate row
            sb.append("\t<tr>\n");
            for (int column = 0; column < grid[row].length; column++) { //iterate column
                System.out.print(grid[row][column] == null ? "\"\"" : grid[row][column].getDisplayText() + "\t");
                if (row == 0)
                    sb.append("\t\t<th>" + getDisplayableValue(grid[row][column]) + "</th>\n");
                else {
                    if (grid[row][column].isAnomoly())
                        sb.append("\t\t<td><div style=\"color:red;\" title=\"");
                    else if (grid[row][column].isConsistentVersion())
                        sb.append("\t\t<td><div style=\"color:green;\" title=\"");
                    else
                        sb.append("\t\t<td><div title=\"");
                    sb.append(getHoverValue(grid[row][column])).append("\">")
                            .append(getDisplayableValue(grid[row][column])).append("</div></td>\n");
                }
            }
            System.out.print("\n");
            sb.append("\t</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    private String getDisplayableValue(Cell cell) {
        return cell == null || cell.getDisplayText() == null || "null".equalsIgnoreCase(cell.getDisplayText()) ?
                "" : cell.getDisplayText();
    }

    private String getHoverValue(Cell cell) {
        return cell == null || cell.getAltText() == null || "null".equalsIgnoreCase(cell.getAltText()) ?
                "" : cell.getAltText();
    }

    private void printGrid() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                sb.append(grid[row][col] == null ?
                        "\"\"" :
                        grid[row][col].getDisplayText() +
                                "\t");
            }
            sb.append("\n");
        }
        sb.append("\n");
        logger.debug("printGrid():\n" + sb.toString());
    }

    private static List<String> getMasterAppList(Map<String, List<WebApp>> webappDirsContents) {
        Set<String> allApps = new HashSet<>();
        webappDirsContents.values().forEach(appsInWebAppDir ->
                appsInWebAppDir.forEach(app -> allApps.add(app.getName())));
        return allApps.stream().distinct().sorted().collect(Collectors.toList());
    }

    private int getTotalRowCount(Map<String, List<WebApp>> webappDirsContents) {
        if (masterAppList == null) {
            masterAppList = new ArrayList<>(30);
        } else {
            return masterAppList.size();
        }
        if (webappDirsContents == null)
            return 0;
        for (List<WebApp> webApps : webappDirsContents.values()) {
            webApps.forEach(it -> masterAppList.add(it.getName()));
        }

        return masterAppList.size();
    }
}
