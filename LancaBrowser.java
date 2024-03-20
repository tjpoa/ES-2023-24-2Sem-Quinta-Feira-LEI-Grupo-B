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

<<<<<<< Updated upstream
    public static void main(String[] args) throws IOException {
        // Gerar o arquivo HTML a partir do CSV
        CSVparaHTML.main(args);

        String urlStr = "https://github.com/vbasto-iscte/dataset-horarios/raw/main/SalasDeAulaPorTiposDeSala.html";
        String file = "SalasDeAulaPorTiposDeSala.html";
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(System.getProperty("user.dir") + File.separator + file);
        byte[] buffer = new byte[1024];
        int count=0;
        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
        System.out.println("URL para o ficheiro html remoto a ser descarregado para o sistema de ficheiros local = " + urlStr);
        System.out.println("Caminho para o ficheiro html local = " + System.getProperty("user.dir"));
        System.out.println("Nome do ficheiro html local = " + file);

        JFrame frame = new JFrame("A Minha Aplicação");
        JButton button = new JButton("Mostrar Salas no Browser Web");
        button.setBounds(20,20,250,50);

        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                try {
                    File htmlFile = new File("calendario.html");
                    Desktop.getDesktop().browse(htmlFile.toURI());
                } catch (IOException e1) {
                    e1.printStackTrace();
=======
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
>>>>>>> Stashed changes
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
