import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVparaHTML {

    public static void convertCSVtoHTML(String csvFilePath) {
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
        htmlBuilder.append("\t<body>\n");

        // Append H1 tag
        htmlBuilder.append("\t\t<H1>Cadastro de Salas</H1>\n");

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

        htmlBuilder.append("\t\t</script>\n");

        // Append HTML footer
        htmlBuilder.append("\t</body>\n");
        htmlBuilder.append("</html>");

        // Write the generated HTML to a file
        try (FileWriter fw = new FileWriter("salaslayout.html")) {
            fw.write(htmlBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


