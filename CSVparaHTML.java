import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVparaHTML {
    public static void main(String[] args) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Append HTML header
        htmlBuilder.append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        htmlBuilder.append("\t<head>\n");
        htmlBuilder.append("\t\t<meta charset=\"utf-8\" />\n");
        htmlBuilder.append("\t\t<link href=\"https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css\" rel=\"stylesheet\">\n");
        htmlBuilder.append("\t\t<script type=\"text/javascript\" src=\"https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js\"></script>\n");
        htmlBuilder.append("\t</head>\n");
        htmlBuilder.append("\t<body>\n");

        // Append H1 tag
        htmlBuilder.append("\t\t<H1>Tipos de Salas de Aula</H1>\n");

        // Append div for table
        htmlBuilder.append("\t\t<div id=\"example-table\"></div>\n");

        // Append script
        htmlBuilder.append("\t\t<script type=\"text/javascript\">\n");
        htmlBuilder.append("\t\t\tvar tabledata = [\n");

        // Read CSV file and append data to JavaScript array
        try (BufferedReader br = new BufferedReader(new FileReader("HorarioDeExemplo.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 11) {
                    String classroomtypedescription = parts[9].trim();
                    String amountofclassroomsofthistype = parts[4].trim();
                    String classroomsids = parts[10].trim();

                    System.out.println("classroomtypedescription: " + classroomtypedescription);
                    System.out.println("amountofclassroomsofthistype: " + amountofclassroomsofthistype);
                    System.out.println("classroomsids: " + classroomsids);
                    System.out.println();

                    htmlBuilder.append("\t\t\t\t{");
                    htmlBuilder.append("classroomtypedescription:\"" + classroomtypedescription + "\", ");
                    htmlBuilder.append("amountofclassroomsofthistype:\"" + amountofclassroomsofthistype + "\", ");
                    htmlBuilder.append("classroomsids:\"" + classroomsids + "\"");
                    htmlBuilder.append("},\n");
                }
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
        htmlBuilder.append("\t\t\t\tinitialSort: [{ column: \"building\", dir: \"asc\" }],\n");
        htmlBuilder.append("\t\t\t\tcolumns: [\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Descrição do Tipo de Sala\", field: \"classroomtypedescription\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Quantidade\", field: \"amountofclassroomsofthistype\", headerFilter: \"input\" },\n");
        htmlBuilder.append("\t\t\t\t\t{ title: \"Sala\", field: \"classroomsids\", headerFilter: \"input\" }\n");
        htmlBuilder.append("\t\t\t\t]\n");
        htmlBuilder.append("\t\t\t});\n");
        htmlBuilder.append("\t\t</script>\n");

        // Append HTML footer
        htmlBuilder.append("\t</body>\n");
        htmlBuilder.append("</html>");

        // Escrever o HTML gerado em um arquivo
        try (FileWriter fw = new FileWriter("calendario.html")) {
            fw.write(htmlBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
