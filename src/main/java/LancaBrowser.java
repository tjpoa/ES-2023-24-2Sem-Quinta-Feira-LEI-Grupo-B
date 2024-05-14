import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Esta classe fornece uma interface gráfica para converter um arquivo CSV em uma tabela HTML
 * com funcionalidades avançadas usando a biblioteca Tabulator.
 */
public class LancaBrowser {

    /**
     * Método principal que inicia a interface gráfica.
     *
     * @param args Os argumentos da linha de comando (não são utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LancaBrowser::createAndShowGUI);
    }

    /**
     * Cria e exibe a interface gráfica.
     */
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

    /**
     * Abre um seletor de arquivo para o usuário escolher um arquivo CSV e, em seguida, inicia a conversão para HTML.
     *
     * @param parentFrame O frame pai para o seletor de arquivo.
     */
    static void chooseFileAndConvert(JFrame parentFrame) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(parentFrame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            convertCSVToHTML(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Solicita ao usuário uma URL para um arquivo CSV e, em seguida, inicia a conversão para HTML.
     *
     * @param parentFrame O frame pai para o diálogo de entrada da URL.
     */
    static void enterURLAndConvert(JFrame parentFrame) {
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

    /**
     * Faz o download de um arquivo da URL fornecida e o salva temporariamente no sistema.
     *
     * @param url A URL do arquivo a ser baixado.
     * @return O arquivo temporário baixado.
     * @throws IOException Se ocorrer um erro de I/O durante o download.
     */
    static File downloadFileFromURL(URL url) throws IOException {
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

    /**
     * Converte o arquivo CSV em uma tabela HTML e o salva no diretório especificado pelo usuário.
     *
     * @param csvFilePath O caminho para o arquivo CSV a ser convertido.
     */
    static void convertCSVToHTML(String csvFilePath) {
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

    /**
     * Abre o arquivo HTML no navegador padrão do sistema.
     *
     * @param htmlFileName O nome do arquivo HTML a ser aberto.
     */
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
