import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;

/**
 * Esta classe fornece métodos para converter arquivos CSV em HTML.
 */
public class CSVparaHTML {

    /**
     * Converte um arquivo CSV em um arquivo HTML formatado.
     * @param csvFilePath O caminho do arquivo CSV a ser convertido.
     */
    public static void convertCSVtoHTML(String csvFilePath) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Append HTML header
        htmlBuilder.append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        htmlBuilder.append("\t<head>\n");
        htmlBuilder.append("\t\t<meta charset=\"utf-8\" />\n");
        htmlBuilder.append("\t\t<link href=\"https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css\" rel=\"stylesheet\">\n");
        htmlBuilder.append("\t\t<script type=\"text/javascript\" src=\"https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js\"></script>\n");
        htmlBuilder.append("\t\t<style>\n");
        htmlBuilder.append("\t\t\t.column-checkbox-label {\n");
        htmlBuilder.append("\t\t\t\tmargin-right: 10px;\n");
        htmlBuilder.append("\t\t\t}\n");
        htmlBuilder.append("\t\t</style>\n");
        htmlBuilder.append("\t</head>\n");
        htmlBuilder.append("\t<body>\n");

        // Append H1 tag 
        htmlBuilder.append("\t\t<H1>Horário das Aulas</H1>\n");

        // Append div for table
        htmlBuilder.append("\t\t<div id=\"example-table\"></div>\n");

        // Append checkbox for column visibility
        htmlBuilder.append("\t\t<div>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"curso\" checked>Curso</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"unidadeCurricular\" checked>Unidade Curricular</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"turno\" checked>Turno</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"turma\" checked>Turma</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"inscritos\" checked>Inscritos no turno</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"diaSemana\" checked>Dia da Semana</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"semanaDoAno\" checked>Semana Do Ano</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"semanaSemestre\" checked>Semana do Semestre</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"horaInicio\" checked>Hora Início</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"horaFim\" checked>Hora Fim</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"dataAula\" checked>Data da Aula</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"SalaPedida\" checked>Características da Sala Pedida</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"SalaAtribuida\" checked>Sala</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"semanaAno\" checked>Semana do Ano</label>\n");
        htmlBuilder.append("\t\t\t<label class=\"column-checkbox-label\"><input type=\"checkbox\" class=\"column-checkbox\" data-column=\"semanaSemestre\" checked>Semana do Semestre</label>\n");
        htmlBuilder.append("\t\t</div>\n");

        // Append script
        htmlBuilder.append("\t\t<script type=\"text/javascript\">\n");
        htmlBuilder.append("\t\t\tvar tabledata = [\n");

        // Read CSV file and append data to JavaScript array
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))){
            String line;
            boolean firstLine = true;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataMinima = null;
            long semanas=50000;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Ignore the first line
                }
                String[] parts = line.split(";");
                if (parts.length == 11) {
                    String curso = parts[0].trim();
                    String unidadeCurricular = parts[1].trim();
                    String turno = parts[2].trim();
                    String turma = parts[3].trim();
                    String inscritos = parts[4].trim();
                    String diaSemana = parts[5].trim();
                    String horaInicio = parts[6].trim();
                    String horaFim = parts[7].trim();
                    String dataAula = parts[8].trim();
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dataAula);
                    dataAula = new SimpleDateFormat("dd-MM-yyyy").format(date);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int semanaDoAno = cal.get(Calendar.WEEK_OF_YEAR);
                    int semanaSemestre;

                    if (semanaDoAno > 35) {
                        semanaSemestre = semanaDoAno - 35;
                    } else {
                        semanaSemestre = semanaDoAno;
                    }
                    String salaPedida = parts[9].trim();
                    String sala = parts[10].trim();

                    LocalDate dataHora = LocalDate.parse(dataAula, formatter);
                    int semanaAno = dataHora.get(WeekFields.ISO.weekOfWeekBasedYear());

                    LocalDate dataAula1 = LocalDate.parse(parts[8].trim(), formatter);
                    
                    // Verifica se é a primeira data ou se é mais antiga do que a mínima atual
                    if (dataMinima == null || dataAula1.isBefore(dataMinima)) {
                        dataMinima = dataAula1; // Atualiza a data mínima
                    }
                        LocalDate inicioSemanaLetiva = dataMinima.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                    LocalDate dataAula2 = LocalDate.parse(parts[8].trim(), formatter);
                     
                    semanas = ChronoUnit.WEEKS.between(inicioSemanaLetiva, dataAula2) +1;
                     String semanaSemestre =(semanas + " semana do semestre.");
                    
                      

                     
                        htmlBuilder.append("\t\t\t\t{");
                        htmlBuilder.append("curso:\"" + curso + "\", ");
                        htmlBuilder.append("unidadeCurricular:\"" + unidadeCurricular + "\", ");
                        htmlBuilder.append("turno:\"" + turno + "\", ");
                        htmlBuilder.append("turma:\"" + turma + "\", ");
                        htmlBuilder.append("inscritos:\"" + inscritos + "\", ");
                        htmlBuilder.append("diaSemana:\"" + diaSemana + "\", ");
                        htmlBuilder.append("horaInicio:\"" + horaInicio + "\", ");
                        htmlBuilder.append("horaFim:\"" + horaFim + "\", ");
                        htmlBuilder.append("dataAula:\"" + dataAula + "\", ");
                        htmlBuilder.append("salaPedida:\"" + salaPedida + "\", ");
                        htmlBuilder.append("salaAtribuida:\"" + sala + "\", ");
                        htmlBuilder.append("semanaAno:\"" + semanaAno + "\", ");
                        htmlBuilder.append("semanaSemestre:\"" + semanaSemestre + "\", ");
                        htmlBuilder.append("},\n");
                    }
                }   
        
    }catch (IOException e) {
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
        htmlBuilder.append("\t\t\t\tinitialSort: [{ column: \"curso\", dir: \"asc\" }],\n");
        htmlBuilder.append("\t\t\t\tcolumns: [\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Curso\", field: \"curso\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Unidade Curricular\", field: \"unidadeCurricular\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Turno\", field: \"turno\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Turma\", field: \"turma\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Inscritos no turno\", field: \"inscritos\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Dia da Semana\", field: \"diaSemana\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Hora Início\", field: \"horaInicio\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Hora Fim\", field: \"horaFim\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Data da Aula\", field: \"dataAula\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Semana do Ano\", field: \"semanaDoAno\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Semana do Semestre\", field: \"semanaSemestre\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Características da Sala Pedida\", field: \"salaPedida\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Sala Atribuída\", field: \"salaAtribuida\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Semana do Ano\", field: \"semanaAno\", headerFilter: \"input\" },\n");  
        htmlBuilder.append("\t\t\t\t\t{ title: \"Semana do Semestre\", field: \"semanaSemestre\", headerFilter: \"input\" }\n");
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

        htmlBuilder.append("\t\t</script>\n");

        // Append HTML footer
        htmlBuilder.append("\t</body>\n");
        htmlBuilder.append("</html>");

        // Write the generated HTML to a file
        try (FileWriter fw = new FileWriter("calendario.html")) {
            fw.write(htmlBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}