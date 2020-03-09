package nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileOperations {
    private static final String FILE_NAME = "file.txt";
    public static final String TARGET_PATH = "dir1";

    @Test
    public void shouldCreateMoveAndDeleteFile() throws IOException {
        final Path createdFilePath = Files.write(Paths.get(FILE_NAME), "".getBytes(), StandardOpenOption.CREATE);
        assertTrue(Files.exists(createdFilePath));

        final Path movedFilePath = Files.move(createdFilePath, Paths.get(TARGET_PATH));
        assertFalse(Files.exists(createdFilePath));
        assertTrue(Files.exists(movedFilePath));

        Files.delete(movedFilePath);
        assertFalse(Files.exists(movedFilePath));
    }
}
