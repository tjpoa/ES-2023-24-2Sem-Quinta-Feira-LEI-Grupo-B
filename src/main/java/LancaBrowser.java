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

        // Cria um botão para converter o arquivo CSV
        JButton converterButton = new JButton("Converter");
        JButton urlButton = new JButton("Especificar URL para Arquivo CSV");

        // Define a posição e o tamanho do botão
        converterButton.setBounds(50, 50, 250, 50);
        urlButton.setBounds(50, 110, 250, 50);

        // Adiciona um ouvinte de ação ao botão "Converter"
        converterButton.addActionListener(new ActionListener() {
            /**
             * Este método é chamado quando o botão "Converter" é clicado.
             * @param e O evento de ação associado ao clique do botão.
             */
            public void actionPerformed(ActionEvent e) {
                // Abre o seletor de arquivos para escolher o arquivo CSV
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    // Obtém o arquivo CSV selecionado
                    File selectedFile = fileChooser.getSelectedFile();
                    String csvFilePath = selectedFile.getAbsolutePath();

                    // Converte o arquivo CSV em HTML
                    CSVparaHTML.convertCSVtoHTML(csvFilePath);

                    // Abre o arquivo HTML no navegador
                    abrirHTMLNoBrowser("salaslayout.html");
                }
            }
        });

        // Adiciona um ouvinte de ação ao botão "Especificar URL para Arquivo CSV"
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
                        abrirHTMLNoBrowser("salaslayout.html");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Failed to download CSV from the provided URL.");
                    }
                }
            }
        });

        // Adiciona os botões ao JFrame
        frame.add(converterButton);
        frame.add(urlButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    /**
     * Este método abre um arquivo HTML em um navegador.
     * @param htmlFileName O nome do arquivo HTML a ser aberto.
     */
    public static void abrirHTMLNoBrowser(String htmlFileName) {
        File htmlFile = new File(htmlFileName);
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Falha ao abrir o arquivo HTML no navegador.");
        }
    }
}
