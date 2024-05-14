import java.io.FileWriter;
import java.io.IOException;

public class formularioHTML {

    public static void criarFormularioHTML() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("<head>\n")
                .append("<title>Formulário de Alocação de Aula</title>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("<h2>Formulário de Alocação de Aula</h2>\n")
                .append("<form action=\"/submit\" method=\"post\">\n")
                .append(criarCampoTexto("excluirPeriodos", "Períodos a excluir para alocação da aula"))
                .append(criarCampoTexto("alocarPeriodos", "Períodos em que é possível alocar a aula"))
                .append(criarCampoTexto("preferenciaSala", "Preferência do tipo de sala"))
                .append(criarCampoTexto("salasNaoAceitaveis", "Salas não aceitáveis para a aula"))
                .append("<br>\n")
                .append("<input type=\"submit\" value=\"Submit\">\n")
                .append("</form>\n")
                .append("</body>\n")
                .append("</html>");

        escreverArquivoHTML(html.toString());
    }

    public static String criarCampoTexto(String id, String label) {
        return "  <label for=\"" + id + "\">" + label + ":</label><br>\n" +
                "  <input type=\"text\" id=\"" + id + "\" name=\"" + id + "\"><br>\n";
    }

    public static void escreverArquivoHTML(String conteudo) {
        try {
            FileWriter writer = new FileWriter("formulario.html");
            writer.write(conteudo);
            writer.close();
            System.out.println("Formulário HTML criado com sucesso!");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao criar o formulário HTML: " + e.getMessage());
        }
    }
}
