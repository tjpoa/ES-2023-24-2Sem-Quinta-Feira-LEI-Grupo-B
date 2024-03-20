import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVparaHTML {
    public static void main(String[] args) {
        String arquivoCSV = "HorarioDeExemplo.csv";
        String arquivoHTML = "calendario.html";
        String linha = "";
        String separador = ",";

        // Gerar o HTML do calendário
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("<title>Calendário</title>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("<table border=\"1\">\n");

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            while ((linha = br.readLine()) != null) {
                html.append("<tr>\n");
                String[] dados = linha.split(separador);
                for (String dado : dados) {
                    html.append("<td>").append(dado).append("</td>\n");
                }
                html.append("</tr>\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        html.append("</table>\n");
        html.append("</body>\n");
        html.append("</html>");

        // Escrever o HTML gerado em um arquivo
        try (FileWriter fw = new FileWriter(arquivoHTML)) {
            fw.write(html.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
