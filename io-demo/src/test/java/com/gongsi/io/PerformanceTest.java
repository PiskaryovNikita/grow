package com.gongsi.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PerformanceTest {
    private static final int STRING_LENGTH = 300_000_000;
    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";
    private static final int BUFFER_SIZE = 8192 * 2;
    private String randomString;

    @Before
    public void setup() throws IOException {
        randomString = RandomStringUtils.randomAlphabetic(STRING_LENGTH);
        Files.write(Paths.get(INPUT_FILE), randomString.getBytes(), StandardOpenOption.CREATE,
                StandardOpenOption.WRITE);
    }

    @Test
    public void shouldReadHugeFileWithReader() throws IOException {
        final long before = System.currentTimeMillis();
        final char[] buffer = new char[STRING_LENGTH];
        try (final FileReader fileReader = new FileReader(Paths.get(INPUT_FILE).toFile())) {
            fileReader.read(buffer);
        }
        final long after = System.currentTimeMillis();

        System.out.println(String.format("Reader read time %d ms", after - before));
        assertEquals(randomString, new String(buffer));
    }

    @Test
    public void shouldReadHugeFileWithBufferedReader() throws IOException {
        final long before = System.currentTimeMillis();
        final char[] buffer = new char[STRING_LENGTH];
        try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(Paths.get(INPUT_FILE).toFile()),
                BUFFER_SIZE)) {
            bufferedReader.read(buffer);
        }
        final long after = System.currentTimeMillis();

        System.out.println(String.format("BufferedReader read time %d ms", after - before));
        assertEquals(randomString, new String(buffer));
    }

    @Test
    public void shouldWriteHugeFileWithReader() throws IOException {
        final long before = System.currentTimeMillis();
        try (final FileWriter fileWriter = new FileWriter(Paths.get(OUTPUT_FILE).toFile())) {
            fileWriter.write(randomString);
        }
        final long after = System.currentTimeMillis();
        System.out.println(String.format("FileWriter write time %d ms", after - before));

        final byte[] bytes = Files.readAllBytes(Paths.get(OUTPUT_FILE));
        assertEquals(randomString, new String(bytes));
    }

    @Test
    public void shouldWriteHugeFileWithBufferedReader() throws IOException {
        final long before = System.currentTimeMillis();
        try (final BufferedWriter bufferedWriter =
                     new BufferedWriter(new FileWriter(Paths.get(OUTPUT_FILE).toFile()), BUFFER_SIZE)) {
            bufferedWriter.write(randomString);
        }
        final long after = System.currentTimeMillis();
        System.out.println(String.format("BufferedWriter write time %d ms", after - before));

        final byte[] bytes = Files.readAllBytes(Paths.get(OUTPUT_FILE));
        assertEquals(randomString, new String(bytes));
    }
}
