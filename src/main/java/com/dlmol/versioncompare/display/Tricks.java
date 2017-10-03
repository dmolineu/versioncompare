package com.dlmol.versioncompare.display;

import java.io.*;
import java.util.Arrays;

public class Tricks {

    public static void main (String[] args) throws IOException {
        final File file = new File("lines.txt");
        final FileReader fileReader = new FileReader(file);
        try (BufferedReader reader = new BufferedReader(fileReader)) {

        }
        String[] array = {"a", "b"};

        String blah = blah();

        String xml = "<xml>\n" +
                "    <a>\n" +
                "        <b>\n" +
                "            <c>hello</c>\n" +
                "        </b>\n" +
                "    </a>\n" +
                "</xml>";
    }

    private static String blah() {
        return "blah";
    }
}