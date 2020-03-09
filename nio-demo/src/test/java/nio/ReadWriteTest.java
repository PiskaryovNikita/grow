package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Non blocking: nio read, write doesn't block.
 * Disadvantage: when read/write invoked we are not sure that all data were read/written.
 * Hence need to manually check if all bytes read/written.
 */
public class ReadWriteTest {
    public static final String FILE_NAME = "file.txt";

    @Test
    public void shouldWriteProcessedData() throws IOException {
        try (final SeekableByteChannel channel = Files.newByteChannel(Paths.get(FILE_NAME),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            final String input = "This is a test";

            channel.write(ByteBuffer.wrap(input.getBytes()));

            final ByteBuffer buffer = ByteBuffer.allocate(10);

            final StringBuilder stringBuilder = new StringBuilder();
            while (channel.read(buffer) > 0) {
                buffer.rewind();
                stringBuilder.append(StandardCharsets.UTF_8.decode(buffer).toString());
                buffer.flip();
            }

            final String processedString = stringBuilder.toString().replace(" ", "_").toUpperCase();

            channel.write(ByteBuffer.wrap(processedString.getBytes()));

            final String actual = new String(Files.readAllBytes(Paths.get(FILE_NAME)));
            assertEquals("THIS_IS_A_TEST", actual);
        }
    }
}
