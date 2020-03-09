package com.gongsi.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Buffer is intermediate component where data stored before reading/writing.
 * Because of this less read/write handled directly by OS, only when buffer empty/full read/write request to JVM -> OS
 * is done.
 */
public class BufferedReaderWriterTest {
    private static final String OUTPUT_FILE = "output.txt";
    private static final String INPUT_FILE = "input.txt";

    @Before
    public void setup() throws IOException {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(new File(INPUT_FILE)))) {
            writer.write("This is a test");
            writer.flush();
        }
    }

    @Test
    public void shouldWriteProcessedTextWhenReadingUnprocessedText() throws IOException {
        final StringBuilder totalContent = new StringBuilder();
        try (final BufferedReader reader = new BufferedReader(new FileReader(new File(INPUT_FILE)))) {
            String content;

            while ((content = reader.readLine()) != null) {
                totalContent.append(content);
            }
        }
        assertEquals(14, totalContent.length());

        final String fileContent = totalContent.toString();

        final String processedString = fileContent.replace(" ", "_").toUpperCase();

        try (final Writer writer = new FileWriter(new File(OUTPUT_FILE))) {
            writer.write(processedString);
            writer.flush();
        }

        final String actual = FileUtils.readFileToString(new File(OUTPUT_FILE), StandardCharsets.UTF_8);
        assertEquals("THIS_IS_A_TEST", actual);
    }
}
