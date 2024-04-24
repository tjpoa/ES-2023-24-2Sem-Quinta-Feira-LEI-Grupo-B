import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

        // Define a posição e o tamanho do botão
        converterButton.setBounds(50, 50, 250, 50);

        // Adiciona um ouvinte de ação ao botão
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

        // Adiciona o botão ao JFrame
        frame.add(converterButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);
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

