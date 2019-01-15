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
    public void readCsv() throws IOException {
        Reader in = new FileReader("/home/lee/IdeaProjects/test/src/main/resources/test.csv");
        Iterable<CSVRecord> records =  CSVFormat.DEFAULT.withHeader("ID","Name") .withTrim().withDelimiter(' ').parse(in);
        for (CSVRecord record : records) {
            String itemNum = record.get("ID");
            String desc = record.get("Name");
            System.out.printf("%s %s", itemNum, desc);
        }
    }

    @Test
    public void writeCSV() throws IOException {

        CSVFormat format = CSVFormat.DEFAULT.withHeader("ID","Name").withDelimiter(' ');

        try(Writer out = new FileWriter("/home/lee/IdeaProjects/test/src/main/resources/test.csv");
            CSVPrinter printer = new CSVPrinter(out, format)) {
            List<String> records = new ArrayList<>();
            records.add("001");
            records.add("lee");
            printer.printRecord(records);
            records = new ArrayList<>();
            records.add("002");
            records.add("lei");
            printer.printRecord(records);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void contextLoads() throws IOException {
        Reader in = new FileReader("/home/lee/IdeaProjects/test/src/main/resources/test.csv");
        try {
            String s = IOUtils.toString(in);
            System.out.println(s);
        } finally {
            IOUtils.closeQuietly(in);
        }


    }

    @Test
    public void testRead() {
        int[] is = {1, 2, 3, 4};
        int i = 0;
        while (i < is.length) {
            if (i == 1) {
                if (is[i] == 2) {
                    break;
                }
            }
            System.out.println(is[i++]);
        }
    }

    @Test
    public void testReadFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("/home/lee/IdeaProjects/test/src/main/resources/hello.text"))) {
            String line;
            boolean start = false;
            List<String[]> filedList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                //analyze title and every field length
                if (line.startsWith("Item Number")) {
                    String lenLine = br.readLine();
                    if (lenLine == null) {
                        break;
                    } else if (!lenLine.startsWith("-")) {
                        //TODO
                        throw new RuntimeException("analyze faile");
                    }

                    start = true;
                    String[] s = lenLine.split(" ");
                    filedList.add(s);

                     s = line.split("\\s+");
                    filedList.add(s);
                    continue;
                } else if (start && line.startsWith("  ")) {
                    continue;
                } else if (start && line.equals("")) {
                    start = false;
                }

                if (start) {
                    String[] fields = analyzeLine(line, filedList.get(0));
                    for (String field : fields) {
                        System.out.printf("%s ", field);
                    }
                    System.out.println();
                }
            }
        }
    }

    private String[] analyzeLine(String line, String[] lenLine) {
        char[] chars = line.toCharArray();
        int i = 0;
        String[] fields = new String[lenLine.length];
        for (int j = 0; i < line.length(); j++) {
            int len = lenLine[j].length();

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

