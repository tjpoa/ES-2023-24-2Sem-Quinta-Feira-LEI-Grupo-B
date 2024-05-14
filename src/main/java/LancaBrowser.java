import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LancaBrowser {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LancaBrowser::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("CSV to HTML Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(2, 1));

        JButton convertButton = new JButton("Converter");
        convertButton.addActionListener(e -> chooseFileAndConvert(frame));

        JButton urlButton = new JButton("Especificar URL para Arquivo CSV");
        urlButton.addActionListener(e -> enterURLAndConvert(frame));

        frame.add(convertButton);
        frame.add(urlButton);
        frame.setVisible(true);
    }

    private static void chooseFileAndConvert(JFrame parentFrame) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(parentFrame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            convertCSVToHTML(selectedFile.getAbsolutePath());
        }
    }

    private static void enterURLAndConvert(JFrame parentFrame) {
        String url = JOptionPane.showInputDialog(parentFrame, "Enter URL to CSV File:");
        if (url != null && !url.isEmpty()) {
            try {
                URL csvUrl = new URL(url);
                File tempFile = downloadFileFromURL(csvUrl);
                convertCSVToHTML(tempFile.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parentFrame, "Failed to download CSV from the provided URL.");
            }
        }
    }

    private static File downloadFileFromURL(URL url) throws IOException {
        File tempFile = File.createTempFile("temp", ".csv");
        try (InputStream inputStream = url.openStream();
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }

    private static void convertCSVToHTML(String csvFilePath) {
        // Adicione um seletor de diretório para permitir que o usuário escolha onde salvar o arquivo HTML
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar HTML");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            String saveDirectory = selectedDirectory.getAbsolutePath();
            CSVparaHTML.convertCSVtoHTML(csvFilePath, saveDirectory);
        }
    }


    public static void openHTMLInBrowser(String htmlFileName) {
        File htmlFile = new File(htmlFileName);
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Falha ao abrir o arquivo HTML no navegador.");
        }
    }
}
