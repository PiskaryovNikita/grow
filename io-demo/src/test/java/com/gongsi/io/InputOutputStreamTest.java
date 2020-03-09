package com.gongsi.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Core interfaces InputStream, OutputStream: It has JVM native implementations.
 * Implementations is used by other classes such as: FileReader(FileInputStream), FileWriter(FileOutputStream)
 */
public class InputOutputStreamTest {
    private static final String OUTPUT_FILE = "output.txt";
    private static final String INPUT_FILE = "input.txt";

    @Before
    public void setup() throws IOException {
        try (final FileOutputStream fileOutputStream = new FileOutputStream(new File(INPUT_FILE))) {
            fileOutputStream.write("This is a test".getBytes());
            fileOutputStream.flush();
        }
    }

    @Test
    public void shouldWriteProcessedTextWhenReadingUnprocessedText() throws IOException {
        byte[] buffer = new byte[0];
        try (final InputStream inputStream = new FileInputStream(new File(INPUT_FILE))) {
            int oneByte;

            while (true) {
                oneByte = inputStream.read();
                if (oneByte == -1) {
                    break;
                }
                buffer = Arrays.copyOf(buffer, buffer.length + 1);
                buffer[buffer.length - 1] = (byte) oneByte;
            }
        }
        assertEquals(14, buffer.length);

        final String fileContent = new String(buffer);

        final String processedString = fileContent.replace(" ", "_").toUpperCase();

        try (final FileOutputStream fileOutputStream = new FileOutputStream(new File(OUTPUT_FILE))) {
            fileOutputStream.write(processedString.getBytes());
            fileOutputStream.flush();
        }

        final String actual = FileUtils.readFileToString(new File(OUTPUT_FILE), StandardCharsets.UTF_8);
        assertEquals("THIS_IS_A_TEST", actual);
    }
}
