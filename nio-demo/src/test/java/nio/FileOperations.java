package nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileOperations {
    private static final String FILE_NAME = "file.txt";

    @Test
    public void shouldCreateMoveAndDeleteFile() throws IOException {
        final Path filePath = Paths.get(FILE_NAME);

        final Path createdFilePath = Files.write(filePath, "".getBytes(), StandardOpenOption.CREATE);

        assertTrue(Files.exists(createdFilePath));

        final Path movedFilePath = Files.move(createdFilePath, Paths.get("dir1", FILE_NAME),
                StandardCopyOption.ATOMIC_MOVE);

        assertFalse(Files.exists(createdFilePath));
        assertTrue(Files.exists(movedFilePath));

        Files.delete(movedFilePath);
        assertFalse(Files.exists(movedFilePath));
    }
}
