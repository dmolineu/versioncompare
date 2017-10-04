package com.dlmol.versioncompare.display;

import com.dlmol.versioncompare.model.Cell;
import com.dlmol.versioncompare.model.WebApp;

import java.util.*;
import java.util.stream.Collectors;

public class DisplayGrid {

    private Cell[][] grid;

    private List<String> masterAppList = null;

    public DisplayGrid(Map<String, List<WebApp>> webappDirsContents) {
        int totalRowCount = getTotalRowCount(webappDirsContents);
        masterAppList = getMasterAppList(webappDirsContents);
        System.out.println("Master App List:");
        masterAppList.forEach(app -> System.out.println("\t" + app));
        grid = new Cell[masterAppList.size() + 1][webappDirsContents.size() + 1];
        grid[0][0] = new Cell("#", null);
        List<String> appDirList = webappDirsContents.keySet().stream().collect(Collectors.toList());
        for (int row = 0; row < masterAppList.size() + 1; row++) { //Iterate rows for apps
            for (int column = 0; column < appDirList.size() + 1; column++) { //Iterate columns for webapp dirs
                final String thisWebAppDir = column == 0 ? "" : appDirList.get(column - 1);
                if (row == 0) { //Headings row
                    if (column == 0)
                        grid[0][column] = new Cell(null, null);
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
                            System.out.println(appForRow + " was found on " + thisWebAppDir);
                            List<WebApp> dirWebApps = webappDirsContents.get(thisWebAppDir);
                            String altText = null;
                            for (WebApp app : dirWebApps)
                                if (app.getName().equalsIgnoreCase(appForRow))
                                    altText = app.getVersion();
                            grid[row][column] = new Cell(appForRow, altText);
                        } else {
                            System.out.println(appForRow + " was NOT found on " + thisWebAppDir);
                            grid[row][column] = new Cell(null, null);
                        }
                    }
                }
            }
        }
        printGrid();
    }

    public String getGridHtmlTable() {
        printGrid();
        System.out.println("getGridHtmlTable():\n"); //TODO: Setup LOGGER.
        StringBuilder sb = new StringBuilder("<table border=1 cellpadding=3 style=\"width:80%\">\n");
        for (int row = 0; row < grid.length; row++) { //iterate row
            sb.append("<tr>\n");
            for (int column = 0; column < grid[row].length; column++) { //iterate column
                System.out.print(grid[row][column] == null ? "\"\"" : grid[row][column].getDisplayText() + "\t");
                if (row == 0)
                    sb.append("<th>" + getDisplayableValue(grid[row][column]) + "</th>\n");
                else
                    sb.append("<td>" + (getDisplayableValue(grid[row][column])) + "</td>\n");
            }
            System.out.print("\n");
            sb.append("</tr>\n");
        }
        sb.append("</table>");
        return sb.toString();
    }

    private String getDisplayableValue(Cell cell) {
        return cell == null || cell.getDisplayText() == null || "null".equalsIgnoreCase(cell.getDisplayText()) ?
                "" : cell.getDisplayText();
    }

    private void printGrid() {
        System.out.println("printGrid():");
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                System.out.print(grid[row][col] == null ?
                        "\"\"" :
                        grid[row][col].getDisplayText() +
                                "\t");
            }
            System.out.println();
        }
        System.out.println();
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

    //TODO: Replace w/ Lombok @Getter
    public Cell[][] getGrid() {
        return grid;
    }
}
