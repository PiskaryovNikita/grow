package nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PrintFilesTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStream() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStream() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldWalkFileTree() throws IOException {
        Files.walkFileTree(Paths.get("src", "main", "java", "nio"), new PrintFiles());

        assertTrue(outContent.toString().contains("EchoNIOServer"));
        assertTrue(outContent.toString().contains("PrintFiles"));
        assertTrue(outContent.toString().contains("TestClient"));
    }
}
