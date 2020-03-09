package com.gongsi.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Wrapper of Input/Output Stream, that delegates to Input/Output Stream and decodes bytes to chars.
 */
public class ReaderWriterTest {
    private static final String OUTPUT_FILE = "output.txt";
    private static final String INPUT_FILE = "input.txt";

    @Before
    public void setup() throws IOException {
        try (final Writer writer = new FileWriter(new File(INPUT_FILE))) {
            writer.write("This is a test");
            writer.flush();
        }
    }

    @Test
    public void shouldWriteProcessedTextWhenReadingUnprocessedText() throws IOException {
        char[] buffer = new char[0];
        try (final Reader reader = new FileReader(new File(INPUT_FILE))) {
            int character;

            while (true) {
                character = reader.read();
                if (character == -1) {
                    break;
                }
                buffer = Arrays.copyOf(buffer, buffer.length + 1);
                buffer[buffer.length - 1] = (char) character;
            }
        }
        assertEquals(14, buffer.length);

        final String fileContent = new String(buffer);

        final String processedString = fileContent.replace(" ", "_").toUpperCase();

        try (final Writer writer = new FileWriter(new File(OUTPUT_FILE))) {
            writer.write(processedString);
            writer.flush();
        }

        final String actual = FileUtils.readFileToString(new File(OUTPUT_FILE), StandardCharsets.UTF_8);
        assertEquals("THIS_IS_A_TEST", actual);
    }
}
