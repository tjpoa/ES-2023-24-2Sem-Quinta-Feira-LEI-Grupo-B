import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CSVparaHTMLTest {

    // Teste para garantir que o método convertCSVtoHTML() gera o HTML esperado
    @Test
    public void testConvertCSVtoHTML() {
        // Criar uma instância da classe CSVparaHTML
        //CSVparaHTML csvConverter = new CSVparaHTML();

        // Criar um arquivo CSV de teste
        String csvFilePath = "test.csv";
        CSVparaHTML csvConverter = new CSVparaHTML(csvFilePath);
        createTestCSV(csvFilePath);

        // Chamar o método de conversão
        csvConverter.convertCSVtoHTML(csvFilePath);

        // Verificar se o arquivo HTML foi gerado corretamente
        // Aqui você pode implementar verificações adicionais, como a presença de tags específicas ou dados esperados no HTML gerado
        // Neste exemplo, estamos apenas verificando se o arquivo foi criado corretamente
        File htmlFile = new File("calendario.html");
        assertTrue(htmlFile.exists());

        // Limpar os arquivos de teste após o teste
        htmlFile.delete();
        new File(csvFilePath).delete();
    }

    // Método auxiliar para criar um arquivo CSV de teste
    private void createTestCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Escrever alguns dados de exemplo no arquivo CSV
            writer.write("Curso;Unidade Curricular;Turno;Turma;Inscritos;Dia da Semana;Hora Início;Hora Fim;Data da Aula;Características da Sala Pedida;Sala Atribuída\n");
            writer.write("Curso A;UC1;Manhã;Turma 1;20;Segunda-feira;08:00;10:00;2022-01-01;Sala 1;Sala 101\n");
            writer.write("Curso B;UC2;Tarde;Turma 2;15;Terça-feira;14:00;16:00;2022-01-02;Sala 2;Sala 102\n");
            // Adicione mais linhas de dados de teste conforme necessário
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
