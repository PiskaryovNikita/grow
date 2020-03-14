package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Non blocking: nio read, write doesn't block.
 * Disadvantage: when read/write invoked we are not sure that all data were read/written.
 * Hence need to manually check if all bytes read/written.
 */
public class ReadWriteTest {
    private static final String INPUT_FILE_NAME = "input.txt";
    private static final String OUTPUT_FILE_NAME = "output.txt";
    private static final int BUFFER_CAPACITY = 10;

    @Before
    public void setup() throws IOException {
        final Path path = Paths.get(INPUT_FILE_NAME);
        try (final SeekableByteChannel channel = Files.newByteChannel(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {
            final String input = "This is a test";
            final ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());

            // as long as pos, limit have right values - we don't need to do anything
            while (channel.write(buffer) > 0) {
                // copy [pos, limit) to the beginning
                buffer.compact();
                // limit = pos, pos = 0
                buffer.flip();
            }
        }
    }

    @After
    public void cleanup() throws IOException {
        Files.delete(Paths.get(INPUT_FILE_NAME));
        Files.delete(Paths.get(OUTPUT_FILE_NAME));
    }

    @Test
    public void shouldReadFromOneFileAndWriteProcessedDataToAnotherFile() throws IOException {
        final StringBuilder stringBuilder;
        try (final SeekableByteChannel channel = Files.newByteChannel(Paths.get(INPUT_FILE_NAME),
                StandardOpenOption.READ)) {

            final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_CAPACITY);

            stringBuilder = new StringBuilder();
            while (channel.read(buffer) > 0) {
                buffer.flip();
                // decode decodes [pos, limit) decode will change pos, hence followed by flip()
                stringBuilder.append(StandardCharsets.UTF_8.decode(buffer));
                buffer.flip();
            }
        }

        final String processedString = stringBuilder.toString().replace(" ", "_").toUpperCase();

        try (final SeekableByteChannel channel = Files.newByteChannel(Paths.get(OUTPUT_FILE_NAME),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            channel.write(ByteBuffer.wrap(processedString.getBytes()));

            final String actual = new String(Files.readAllBytes(Paths.get(OUTPUT_FILE_NAME)));
            assertEquals("THIS_IS_A_TEST", actual);
        }
    }
}
