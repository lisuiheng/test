package com.example.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.convert.Delimiter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TestApplicationTests {

    @Test
    public void testRead() throws IOException {
        List<String[]> fieldLists = analyzeCVS("C:\\Users\\Administrator\\IdeaProjects\\test\\src\\main\\resources\\hello.text");
        for (String[] fields : fieldLists) {
            for (String field : fields) {
                System.out.printf("%s ", field);
            }
            System.out.println();
        }
    }

    private List<String[]> analyzeCVS(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        boolean start = false;
        List<String[]> filedList = new ArrayList<>();
        String[] lenFields = new String[0];
        while ((line = br.readLine()) != null) {
            //analyze title and every field length
            if (line.startsWith("Item Number")) {
                String lenLine = br.readLine();
                if (lenLine == null) {
                    break;
                } else if (!lenLine.startsWith("-")) {
                    throw new RuntimeException("analyze file format invalid");
                }

                lenFields = lenLine.split(" ");
                start = true;
                continue;
            } else if (start && line.startsWith("  ")) {
                continue;
            } else if (start && line.equals("")) {
                start = false;
            }

            if (start) {
                if (lenFields.length == 0) {
                    throw new RuntimeException("lenFields not init");
                }
                String[] fields = analyzeLine(line, lenFields);
                filedList.add(fields);
            }
        }
        return filedList;
    }

    private String[] analyzeLine(String line, String[] lenFields) {
        char[] chars = line.toCharArray();
        int i = 0;
        String[] fields = new String[lenFields.length];
        for (int j = 0; i < line.length(); j++) {
            int len = lenFields[j].length();

            int e = i + len + 1;
            if (e > line.length()) {
                e = line.length();
            }
            for (int k = i; k < e; k++) {
                if (chars[k] > '~') {
                    e--;
                }
            }
            String substring = line.substring(i, e);
            fields[j] = substring.trim();
            i = e;
        }
        return fields;
    }


}

