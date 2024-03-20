import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import javax.swing.*;

public class LancaBrowser {
    public static void main(String[] args) {
        JFrame frame = new JFrame("CSV to HTML Converter");
        JButton localFileButton = new JButton("Choose Local CSV File");
        JButton urlButton = new JButton("Specify URL to CSV File");

        localFileButton.setBounds(50, 50, 250, 50);
        urlButton.setBounds(50, 120, 250, 50);

        localFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String csvFilePath = selectedFile.getAbsolutePath();
                    CSVparaHTML.convertCSVtoHTML(csvFilePath);
                    openHTMLFileInBrowser("calendario.html");
                }
            }
        });

        urlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String url = JOptionPane.showInputDialog(frame, "Enter URL to CSV File:");
                if (url != null && !url.isEmpty()) {
                    try {
                        URL csvUrl = new URL(url);
                        InputStream inputStream = csvUrl.openStream();
                        File tempFile = File.createTempFile("temp", ".csv");
                        FileOutputStream outputStream = new FileOutputStream(tempFile);
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        inputStream.close();
                        outputStream.close();
                        CSVparaHTML.convertCSVtoHTML(tempFile.getAbsolutePath());
                        openHTMLFileInBrowser("calendario.html");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Failed to download CSV from the provided URL.");
                    }
                }
            }
        });

        frame.add(localFileButton);
        frame.add(urlButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private static void openHTMLFileInBrowser(String htmlFilePath) {
        File htmlFile = new File(htmlFilePath);
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to open HTML file in the browser.");
        }
    }
}
