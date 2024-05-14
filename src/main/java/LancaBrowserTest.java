import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class LancaBrowserTest {

    @Test
    void testChooseFileAndConvert() {
        // Simulate user selecting a CSV file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("test.csv")); // Sample CSV file
        int returnValue = fileChooser.showOpenDialog(null);
        assertEquals(JFileChooser.APPROVE_OPTION, returnValue);

        // Test if conversion is successful
        assertDoesNotThrow(() -> LancaBrowser.chooseFileAndConvert(null));
    }

    @Test
    void testEnterURLAndConvert() {
        // Simulate user entering a valid URL
        String validURL = "https://example.com/test.csv";

        // Test if conversion is successful
        assertDoesNotThrow(() -> LancaBrowser.enterURLAndConvert(null));
    }

    @Test
    void testDownloadFileFromURL() {
        try {
            URL url = new URL("https://example.com/test.csv"); // Sample URL
            File downloadedFile = LancaBrowser.downloadFileFromURL(url);
            assertTrue(downloadedFile.exists());
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void testConvertCSVToHTML() {
        // Provide a valid CSV file path
        String csvFilePath = "test.csv"; // Assuming the file exists

        // Test if conversion is successful
        assertDoesNotThrow(() -> LancaBrowser.convertCSVToHTML(csvFilePath));
    }

    @Test
    void testOpenHTMLInBrowser() {
        // Provide a valid HTML file path
        String htmlFilePath = "test.html"; // Assuming the file exists

        // Test if opening in browser is successful
        assertDoesNotThrow(() -> LancaBrowser.openHTMLInBrowser(htmlFilePath));
    }

    @Test
    void testMain() {
        // Just ensure it doesn't throw any exceptions
        assertDoesNotThrow(() -> LancaBrowser.main(new String[]{}));
}
}