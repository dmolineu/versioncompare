package com.dlmol.versioncompare.display;

import com.dlmol.versioncompare.model.Cell;
import com.dlmol.versioncompare.model.WebApp;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.tags.HtmlEscapeTag;

import java.util.*;
import java.util.stream.Collectors;

public class DisplayGrid {
    private static final Logger logger = LoggerFactory.getLogger(DisplayGrid.class);

    @Getter(AccessLevel.PUBLIC)
    private Cell[][] grid;

    private List<String> masterAppList = null;

    public DisplayGrid(Map<String, List<WebApp>> webappDirsContents,
                       List<String> webappDirNames, List<String> webappDirPaths) {
        masterAppList = getMasterAppList(webappDirsContents);
        logger.debug("Master App List:");
        masterAppList.forEach(app -> logger.debug("\t" + app));
        buildGrid(webappDirsContents, webappDirNames, webappDirPaths);
    }

    private static List<String> getMasterAppList(Map<String, List<WebApp>> webappDirsContents) {
        Set<String> allApps = new HashSet<>();
        webappDirsContents.values().forEach(appsInWebAppDir ->
                appsInWebAppDir.forEach(app -> allApps.add(app.getName())));
        return allApps.stream().distinct().sorted().collect(Collectors.toList());
    }

    private void buildGrid(Map<String, List<WebApp>> webappDirsContents,
                           List<String> webappDirNames, List<String> webappDirPaths) {
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
                        grid[0][column] = new Cell(thisWebAppDir, webappDirPaths.get(column - 1));
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
            //Set comparison attributes for formatting:
            if (row > 0) {
                logger.debug("comparing row: " + row);
                //Compare cells in row
                List<Cell> rowCells = new ArrayList<>(grid[row].length - 1);
                for (int col=1; col<grid[row].length; col++) //Skip row # count column (0)
                    rowCells.add(grid[row][col]);
                if (rowCells.stream().map(c -> c.getAltText()).distinct().count() == 1) //All cells have the same AltText
                    for (int col = 1; col < grid[row].length; col++) {
                        grid[row][col].setAnomoly(false);
                        grid[row][col].setConsistentVersion(true);
                    }
                else
                    for (int col = 1; col < grid[row].length; col++) {
                        grid[row][col].setConsistentVersion(false);
                        final Cell cell = grid[row][col];
                        if (cell == null || cell.getAltText() == null)
                            continue;
                        grid[row][col].setAnomoly(isAnomoly(appDirList.size(), rowCells, cell));
                    }
            }
        }
        printGrid();
    }

    /**
     * @param comparisonListSize
     * @param rowCells
     * @param cell
     * @return True when the cell's version/alt text value appears in more than half of the cells compared.
     */
    private boolean isAnomoly(final int comparisonListSize, final List<Cell> rowCells, final Cell cell) {
        final long countOfRowCellsWithSameAltText = rowCells.stream()
                .filter(c -> cell.getAltText().equals(c.getAltText())) //Get list where
                .count();
        final int thresholdForAnomoly = comparisonListSize / 2;
        final boolean isAnomoly = thresholdForAnomoly >= countOfRowCellsWithSameAltText;
        return isAnomoly;
    }

    public String getGridHtmlTable() {
        printGrid();
        logger.debug("getGridHtmlTable():\n");
        StringBuilder sb = new StringBuilder("\n<table border=1 cellpadding=3\">\n");
        for (int row = 0; row < grid.length; row++) { //iterate row
            sb.append("\t<tr>\n");
            for (int column = 0; column < grid[row].length; column++) { //iterate column
                System.out.print(grid[row][column] == null ? "\"\"" : grid[row][column].getDisplayText() + "\t");
                if (row == 0) {
                    sb.append("\t\t<th><div title=\"").append(getHoverValue(grid[row][column])).append("\">")
                            .append(getDisplayableValue(grid[row][column])).append("</div></th>\n");
                } else {
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
        String str = cell == null || cell.getAltText() == null || "null".equalsIgnoreCase(cell.getAltText()) ?
                "" : cell.getAltText();
        str = StringEscapeUtils.escapeHtml4(str);
        return str;
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
}
