import com.boomi.execution.ContextCreator;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ContextCreatorTest {

    private ContextCreator dataContext;

    @Before
    public void setUp() {
        dataContext = new ContextCreator();
    }

    @Test
    public void testAddFiles() throws IOException {
        // Create a temporary directory and file
        Path tempDir = Files.createTempDirectory("tempDir");
        Path tempFile = Files.createTempFile(tempDir, "testFile", ".txt");
        Files.write(tempFile, "test content".getBytes());

        dataContext.AddFiles(tempDir.toString());

        assertEquals(1, dataContext.getDataCount());

        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(tempDir);
    }

    @Test
    public void testGetDataCountAfterAddingStreams() throws IOException {
        Path tempDir = Files.createTempDirectory("tempDirForTest");
        int numberOfFilesToAdd = 5;
        for (int i = 0; i < numberOfFilesToAdd; i++) {
            Path tempFile = Files.createTempFile(tempDir, "testFile" + i, ".txt");
            Files.write(tempFile, ("test content" + i).getBytes());
        }

        dataContext.AddFiles(tempDir.toString());

        try (Stream<Path> stream = Files.walk(tempDir)) {
            stream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        assertEquals("Number of data streams should match the number of files added", numberOfFilesToAdd, dataContext.getDataCount());
    }


    @Test
    public void testStoreStream() throws Exception {
        try {
            InputStream mockStream = new ByteArrayInputStream("test data".getBytes());
            Properties mockProps = new Properties();
            mockProps.setProperty("testKey", "testValue");

            dataContext.storeStream(mockStream, mockProps);

            // If no exception is thrown, the test passes
            // This method doesn't do anything special. It mainly should not throw an error.
        } catch (Exception e) {
            fail("Expected no exception to be thrown, but got: " + e.getMessage());
        }
    }

    @Test
    public void testGetStream() throws IOException {
        Path tempDir = Files.createTempDirectory("tempDirForGetStreamTest");
        Path tempFile = Files.createTempFile(tempDir, "testFile", ".txt");
        String testContent = "test stream content";
        Files.write(tempFile, testContent.getBytes());
        dataContext.AddFiles(tempDir.toString());

        InputStream retrievedStream = dataContext.getStream(0);
        String retrievedContent = new BufferedReader(new InputStreamReader(retrievedStream))
                .lines().collect(Collectors.joining("\n"));

        assertEquals("The content of the retrieved stream should match the expected content",
                testContent, retrievedContent);

        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(tempDir);
    }

    @Test
    public void testAddDynamicDocumentPropertyValues() throws IOException {
        Path tempDir = Files.createTempDirectory("tempDir");
        Path tempFile = Files.createTempFile(tempDir, "testFile", ".txt");
        Files.write(tempFile, "test content".getBytes());

        dataContext.AddFiles(tempDir.toString());
        dataContext.addDynamicDocumentPropertyValues(0, "key", "value");

        Properties props = dataContext.getProperties(0);
        assertEquals("value", props.getProperty("document.dynamic.userdefined.key"));
    }

    @Test
    public void testGetProperties() throws IOException {
        Path tempDir = Files.createTempDirectory("tempDir");
        Path tempFile = Files.createTempFile(tempDir, "testFile", ".txt");
        Files.write(tempFile, "test content".getBytes());
        dataContext.AddFiles(tempDir.toString());
        String key = "key";
        String value = "value";
        dataContext.addDynamicDocumentPropertyValues(0, key, value);

        Properties props = dataContext.getProperties(0);

        assertEquals("The properties should contain the added key-value pair",
                value,
                props.getProperty("document.dynamic.userdefined." + key));
    }
}
