import javax.swing.*;
import java.io.*;

/**
 * Esta classe fornece funcionalidade para converter um arquivo CSV em uma tabela HTML
 * com recursos avançados usando a biblioteca Tabulator.
 */
public class CSVparaHTML {
    /**
     * Caminho para o arquivo CSV a ser convertido.
     */
    private final String csvFilePath;

    /**
     * Constrói um objeto CSVparaHTML com o caminho do arquivo CSV especificado.
     *
     * @param csvFilePath O caminho para o arquivo CSV.
     */
    public CSVparaHTML(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    /**
     * Gera o conteúdo HTML para a tabela com base nos dados do arquivo CSV.
     *
     * @return O conteúdo HTML para a tabela como uma string.
     */
    private static String generateHTMLHeader() {
        StringBuilder htmlBuilder = new StringBuilder();

        // Append HTML header
        htmlBuilder.append("<html lang=\"en\">\n");
        htmlBuilder.append("\t<head>\n");
        htmlBuilder.append("\t\t<meta charset=\"utf-8\" />\n");
        htmlBuilder.append("\t\t<title>Cadastro de Salas</title>\n");
        htmlBuilder.append("\t\t<link href=\"https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css\" rel=\"stylesheet\">\n");
        htmlBuilder.append("\t\t<script type=\"text/javascript\" src=\"https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js\"></script>\n");
        htmlBuilder.append("\t\t<style>\n");
        htmlBuilder.append("\t\t\t.column-checkbox-label {\n");
        htmlBuilder.append("\t\t\t\tmargin-right: 10px;\n");
        htmlBuilder.append("\t\t\t}\n");
        htmlBuilder.append("\t\t</style>\n");
        htmlBuilder.append("\t</head>\n");

        return htmlBuilder.toString();
    }

    /**
     * Gera o código HTML para a tabela com base nos dados do arquivo CSV.
     *
     * @param csvFilePath O caminho para o arquivo CSV.
     * @return O código HTML para a tabela como uma string.
     */
    private static String generateTable(String csvFilePath) {
        StringBuilder htmlBuilder = new StringBuilder();
        // Append div for table
        htmlBuilder.append("\t\t<div id=\"example-table\"></div>\n");

        // Append checkbox for column visibility
        htmlBuilder.append("\t\t<div>\n");

        String[] headers = null; // Moved outside the loop
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean firstLine = true;
            if ((line = br.readLine()) != null) {
                // Extract headers from the first line
                headers = line.split(";");
                for (String header : headers) {
                    htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"" + header.trim() + "\" checked>" + header.trim() + "</label>\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        htmlBuilder.append("\t\t</div>\n");

        htmlBuilder.append(generateSearchOperatorDropdown());

        // Append button for exporting filtered data
        htmlBuilder.append("\t\t<button onclick=\"exportFilteredData('csv')\">Export Filtered Data as CSV</button>\n");
        htmlBuilder.append("\t\t<button onclick=\"exportFilteredData('json')\">Export Filtered Data as JSON</button>\n");
        htmlBuilder.append("\t\t<button onclick=\"suggestSubstitutionSlots()\">Sugerir Slots para Substituição</button>\n");

        // Append script
        htmlBuilder.append("\t\t<script type=\"text/javascript\">\n");
        htmlBuilder.append("\t\t\tvar tabledata = [\n");

        // Read CSV file and append data to JavaScript array
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    // Skip the first line
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(";");
                htmlBuilder.append("\t\t\t\t{");
                for (int i = 0; i < parts.length; i++) {
                    String field = headers[i].trim();
                    String value = parts[i].trim();
                    htmlBuilder.append("\"" + field + "\":\"" + value + "\"");
                    if (i < parts.length - 1) {
                        htmlBuilder.append(", ");
                    }
                }
                htmlBuilder.append("},\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Append Tabulator initialization
        htmlBuilder.append("\t\t\t];\n");
        htmlBuilder.append("\t\t\tvar table = new Tabulator(\"#example-table\", {\n");
        htmlBuilder.append("\t\t\t\tdata: tabledata,\n");
        htmlBuilder.append("\t\t\t\tlayout: \"fitDatafill\",\n");
        htmlBuilder.append("\t\t\t\tpagination: \"local\",\n");
        htmlBuilder.append("\t\t\t\tpaginationSize: 10,\n");
        htmlBuilder.append("\t\t\t\tpaginationSizeSelector: [5, 10, 20, 40],\n");
        htmlBuilder.append("\t\t\t\tmovableColumns: true,\n");
        htmlBuilder.append("\t\t\t\tpaginationCounter: \"rows\",\n");
        htmlBuilder.append("\t\t\t\tinitialSort: [{ column: \"Edificio\", dir: \"asc\" }],\n");
        htmlBuilder.append("\t\t\t\tcolumns: [\n");

        // Append column definitions dynamically
        if (headers != null) {
            for (String header : headers) {
                htmlBuilder.append("\t\t\t\t\t{ title: \"" + header.trim() + "\", field: \"" + header.trim() + "\", headerFilter: \"input\" },\n");
            }
        }

        // Close Tabulator initialization
        htmlBuilder.append("\t\t\t\t]\n");
        htmlBuilder.append("\t\t\t});\n");
        // Add event listener for checkbox changes
        htmlBuilder.append("\t\t\tvar checkboxes = document.querySelectorAll('.column-checkbox');\n");
        htmlBuilder.append("\t\t\tcheckboxes.forEach(function(checkbox) {\n");
        htmlBuilder.append("\t\t\t\tcheckbox.addEventListener('change', function() {\n");
        htmlBuilder.append("\t\t\t\t\tvar column = this.getAttribute('data-column');\n");
        htmlBuilder.append("\t\t\t\t\tif (this.checked) {\n");
        htmlBuilder.append("\t\t\t\t\t\ttable.showColumn(column);\n");
        htmlBuilder.append("\t\t\t\t\t} else {\n");
        htmlBuilder.append("\t\t\t\t\t\ttable.hideColumn(column);\n");
        htmlBuilder.append("\t\t\t\t\t}\n");
        htmlBuilder.append("\t\t\t\t});\n");
        htmlBuilder.append("\t\t\t});\n");
        return htmlBuilder.toString();
    }

    /**
     * Gera funções JavaScript para exportar dados filtrados e manipular checkboxes.
     *
     * @return Funções JavaScript como uma string.
     */
    private static String generateJs() {
        StringBuilder htmlBuilder = new StringBuilder();

        // JavaScript function to export filtered data
        htmlBuilder.append("\t\t\tfunction exportFilteredData(format) {\n"); // Added 'format' parameter
        htmlBuilder.append("\t\t\t\tvar checkboxes = document.querySelectorAll('.column-checkbox');\n");
        htmlBuilder.append("\t\t\t\tvar filteredData = [];\n"); // Defined filteredData variable
        htmlBuilder.append("\t\t\t\ttable.getRows(true).forEach(function(row) {\n");
        htmlBuilder.append("\t\t\t\t\tvar rowData = row.getData();\n");
        htmlBuilder.append("\t\t\t\t\tvar filteredRow = Array.from(checkboxes)\n");
        htmlBuilder.append("\t\t\t\t\t\t.filter(function(checkbox) {\n");
        htmlBuilder.append("\t\t\t\t\t\t\treturn checkbox.checked;\n");
        htmlBuilder.append("\t\t\t\t\t\t})\n");
        htmlBuilder.append("\t\t\t\t\t\t.map(function(checkbox) {\n");
        htmlBuilder.append("\t\t\t\t\t\t\treturn checkbox.getAttribute('data-column');\n");
        htmlBuilder.append("\t\t\t\t\t\t})\n");
        htmlBuilder.append("\t\t\t\t\t\t.map(function(column) {\n");
        htmlBuilder.append("\t\t\t\t\t\t\treturn rowData[column];\n");
        htmlBuilder.append("\t\t\t\t\t\t});\n");
        htmlBuilder.append("\t\t\t\t\tfilteredData.push(filteredRow);\n"); // Push filteredRow to filteredData
        htmlBuilder.append("\t\t\t\t});\n");
        htmlBuilder.append("\t\t\t\tif (format === 'csv') {\n");
        htmlBuilder.append("\t\t\t\t\tvar csvContent = \"data:text/csv;charset=utf-8,\";\n");
        htmlBuilder.append("\t\t\t\t\tcsvContent += Array.from(checkboxes)\n");
        htmlBuilder.append("\t\t\t\t\t\t.filter(function(checkbox) {\n");
        htmlBuilder.append("\t\t\t\t\t\t\treturn checkbox.checked;\n");
        htmlBuilder.append("\t\t\t\t\t\t})\n");
        htmlBuilder.append("\t\t\t\t\t\t.map(function(checkbox) {\n");
        htmlBuilder.append("\t\t\t\t\t\t\treturn checkbox.getAttribute('data-column');\n");
        htmlBuilder.append("\t\t\t\t\t\t})\n");
        htmlBuilder.append("\t\t\t\t\t\t.join(';') + '\\n';\n");
        htmlBuilder.append("\t\t\t\t\tfilteredData.forEach(function(row) {\n");
        htmlBuilder.append("\t\t\t\t\t\tcsvContent += row.join(';') + '\\n';\n");
        htmlBuilder.append("\t\t\t\t\t});\n");
        htmlBuilder.append("\t\t\t\t\tvar encodedUri = encodeURI(csvContent);\n");
        htmlBuilder.append("\t\t\t\t\tvar link = document.createElement('a');\n");
        htmlBuilder.append("\t\t\t\t\tlink.setAttribute('href', encodedUri);\n");
        htmlBuilder.append("\t\t\t\t\tlink.setAttribute('download', 'filtered_schedule.csv');\n");
        htmlBuilder.append("\t\t\t\t\tdocument.body.appendChild(link);\n");
        htmlBuilder.append("\t\t\t\t\tlink.click();\n");
        htmlBuilder.append("\t\t\t\t} else if (format === 'json') {\n");
        htmlBuilder.append("\t\t\t\t\tvar jsonContent = \"data:text/json;charset=utf-8,\" + JSON.stringify(filteredData, null, 2);\n");
        htmlBuilder.append("\t\t\t\t\tvar encodedUri = encodeURI(jsonContent);\n");
        htmlBuilder.append("\t\t\t\t\tvar link = document.createElement('a');\n");
        htmlBuilder.append("\t\t\t\t\tlink.setAttribute('href', encodedUri);\n");
        htmlBuilder.append("\t\t\t\t\tlink.setAttribute('download', 'filtered_schedule.json');\n");
        htmlBuilder.append("\t\t\t\t\tdocument.body.appendChild(link);\n");
        htmlBuilder.append("\t\t\t\t\tlink.click();\n");
        htmlBuilder.append("\t\t\t\t}\n");
        htmlBuilder.append("\t\t\t}\n");


        htmlBuilder.append("\t\t\tfunction suggestSubstitutionSlots() {\n");
        htmlBuilder.append("\t\t\t\t// Capturar as preferências do usuário\n");
        htmlBuilder.append("\t\t\t\tvar uc = prompt('Informe a Unidade Curricular (UC):');\n");
        htmlBuilder.append("\t\t\t\tvar turno = prompt('Informe o Turno:');\n");
        htmlBuilder.append("\t\t\t\tvar data = prompt('Informe a Data (YYYY-MM-DD):');\n");
        htmlBuilder.append("\t\t\t\tvar preferredPeriods = prompt('Informe os períodos preferenciais (por exemplo: manhã, tarde, noite):');\n");
        htmlBuilder.append("\t\t\t\tvar excludedPeriods = prompt('Informe os períodos excluídos (por exemplo: manhã, tarde, noite):');\n");
        htmlBuilder.append("\t\t\t\tvar preferredRooms = prompt('Informe as salas preferenciais (separadas por vírgula, por exemplo: A101, B203, etc.):');\n");
        htmlBuilder.append("\t\t\t\tvar excludedRooms = prompt('Informe as salas excluídas (separadas por vírgula, por exemplo: OS08, BYOD, etc.):');\n");
        htmlBuilder.append("\t\t\t\talert('Sugestões de Slots:\\nSlot 1: Dia: Segunda-feira, Horário: 08:00 - 10:00, Sala: A101\\nSlot 2: Dia: Segunda-feira, Horário: 10:00 - 12:00, Sala: B203');\n");
        htmlBuilder.append("\t\t\t}\n");

        htmlBuilder.append("\t\t</script>\n");
        return htmlBuilder.toString();
    }

    /**
     * Gera um dropdown para selecionar operadores de pesquisa.
     *
     * @return O conteúdo HTML para o dropdown como uma string.
     */
    private static String generateSearchOperatorDropdown() {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("\t\t<div>\n");
        htmlBuilder.append("\t\t\t<label for=\"operatorSelect\">Selecione o operador de pesquisa:</label>\n");
        htmlBuilder.append("\t\t\t<select id=\"operatorSelect\" onchange=\"handleOperatorChange()\">\n"); // Adiciona o evento onchange
        htmlBuilder.append("\t\t\t\t<option value=\"and\">E (AND)</option>\n");
        htmlBuilder.append("\t\t\t\t<option value=\"or\">OU (OR)</option>\n");
        htmlBuilder.append("\t\t\t</select>\n");
        htmlBuilder.append("\t\t</div>\n");

        return htmlBuilder.toString();
    }

    /**
     * Converte o arquivo CSV em uma tabela HTML e o salva no diretório especificado.
     *
     * @param csvFilePath   O caminho para o arquivo CSV.
     * @param saveDirectory O diretório onde o arquivo HTML será salvo.
     */
    public static void convertCSVtoHTML(String csvFilePath, String saveDirectory) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append(generateHTMLHeader());
        htmlBuilder.append(generateTable(csvFilePath));
        htmlBuilder.append(generateJs());

        // Append HTML footer
        htmlBuilder.append("\t</body>\n");
        htmlBuilder.append("</html>");

        // Defina o caminho completo para o arquivo HTML, incluindo o diretório de salvamento
        String htmlFilePath = saveDirectory + File.separator + "salaslayout.html";

        // Escreva o conteúdo HTML gerado no arquivo HTML especificado
        try (FileWriter fw = new FileWriter(htmlFilePath)) {
            fw.write(htmlBuilder.toString());
            JOptionPane.showMessageDialog(null, "HTML gerado com sucesso!\nSalvo em: " + htmlFilePath);

            // Abra o arquivo HTML no navegador
            LancaBrowser.openHTMLInBrowser(htmlFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Falha ao salvar o arquivo HTML.");
        }
    }

}
