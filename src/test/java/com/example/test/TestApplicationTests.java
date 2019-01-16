package com.example.test;

import jdk.nashorn.api.scripting.URLReader;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class TestApplicationTests {

    @Test
    public void testRead() throws IOException {
//        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Administrator\\IdeaProjects\\test\\src\\main\\resources\\hello.text"));
        URL url = new URL("http://127.0.0.1:8001/text.txt");
        BufferedReader br = new BufferedReader(new URLReader(url));
        List<String[]> fieldLists = analyzeCVS(br);
        for (String[] fields : fieldLists) {
            for (String field : fields) {
                System.out.printf("%s ", field);
            }
            System.out.println();
        }
    }

    private List<String[]> analyzeCVS(Reader read) throws IOException {
        BufferedReader br = new BufferedReader(read);
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
        Arrays.fill(fields, "");
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

