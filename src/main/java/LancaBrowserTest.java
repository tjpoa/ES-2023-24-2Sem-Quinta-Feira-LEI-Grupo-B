import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LancaBrowserTest {

    @Test
    void testOpenHTMLFileInBrowser() {
        // Suponha que o arquivo HTML válido seja criado temporariamente
        String htmlFilePath = "C:\\Users\\cova\\Documents\\GitHub\\ES-2023-24-2Sem-Quinta-Feira-LEI-Grupo-B\\SalasDeAulaPorTiposDeSala.html";

        // Verifique se o navegador foi lançado corretamente
        assertDoesNotThrow(() -> LancaBrowser.abrirHTMLNoBrowser(htmlFilePath), "Failed to launch browser");
    }
}