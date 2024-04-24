import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Esta classe Java é responsável por fornecer uma interface gráfica simples
 * para converter arquivos CSV em HTML e visualizá-los em um navegador.
 */
public class LancaBrowser {
    /**
     * O método principal que inicia a aplicação.
     * @param args Os argumentos de linha de comando (não utilizados neste programa).
     */
    public static void main(String[] args) {
        // Cria uma instância do JFrame
        JFrame frame = new JFrame("CSV to HTML Converter");

        // Cria botões para escolher um arquivo CSV local ou especificar uma URL para um arquivo CSV
        JButton localFileButton = new JButton("Choose Local CSV File");
        JButton urlButton = new JButton("Specify URL to CSV File");

        // Define as posições e tamanhos dos botões
        localFileButton.setBounds(50, 50, 250, 50);
        urlButton.setBounds(50, 120, 250, 50);

        // Adiciona ouvintes de ação aos botões
        localFileButton.addActionListener(new ActionListener() {
            /**
             * Este método é chamado quando o botão "Escolher Arquivo Local CSV" é clicado.
             * @param e O evento de ação associado ao clique do botão.
             */
            public void actionPerformed(ActionEvent e) {
                // Abre um seletor de arquivo
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    // Obtém o arquivo CSV selecionado
                    File selectedFile = fileChooser.getSelectedFile();
                    String csvFilePath = selectedFile.getAbsolutePath();
                    // Converte o arquivo CSV em HTML
                    CSVparaHTML.convertCSVtoHTML(csvFilePath);
                    // Abre o arquivo HTML no navegador
                    openHTMLFileInBrowser("calendario.html");
                }
            }
        });

        urlButton.addActionListener(new ActionListener() {
            /**
             * Este método é chamado quando o botão "Especificar URL para Arquivo CSV" é clicado.
             * @param e O evento de ação associado ao clique do botão.
             */
            public void actionPerformed(ActionEvent e) {
                // Solicita ao usuário que insira a URL para o arquivo CSV
                String url = JOptionPane.showInputDialog(frame, "Enter URL to CSV File:");
                if (url != null && !url.isEmpty()) {
                    try {
                        // Abre a URL e cria um arquivo temporário para o CSV
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
                        // Converte o arquivo CSV em HTML
                        CSVparaHTML.convertCSVtoHTML(tempFile.getAbsolutePath());
                        // Abre o arquivo HTML no navegador
                        openHTMLFileInBrowser("calendario.html");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Failed to download CSV from the provided URL.");
                    }
                }
            }
        });

        // Adiciona os botões ao JFrame
        frame.add(localFileButton);
        frame.add(urlButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    /**
     * Este método abre um arquivo HTML em um navegador.
     * @param htmlFilePath O caminho do arquivo HTML a ser aberto.
     */
    public static void openHTMLFileInBrowser(String htmlFilePath) {
        File htmlFile = new File(htmlFilePath);
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to open HTML file in the browser.");
        }
    }
}
