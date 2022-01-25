package game.rest_api;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestHandler_ImplTest {

    @Test
    void getCardInfo() {
        HttpRequestHandler_Impl handler = new HttpRequestHandler_Impl();
        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(()->assertTrue(handler.getCardInfo(mapper.readTree("[{\"Id\":\"67f9048f-99b8-4ae4-b866-d8008d00c53d\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}, {\"Id\":\"aa9999a0-734c-49c6-8f4a-651864b14e62\", \"Name\":\"RegularSpell\", \"Damage\": 50.0}, {\"Id\":\"d6e9c720-9b5a-40c7-a6b2-bc34752e3463\", \"Name\":\"Knight\", \"Damage\": 20.0}, {\"Id\":\"02a9c76e-b17d-427f-9240-2dd49b0d3bfd\", \"Name\":\"RegularSpell\", \"Damage\": 45.0}, {\"Id\":\"2508bf5c-20d7-43b4-8c77-bc677decadef\", \"Name\":\"FireElf\", \"Damage\": 25.0}]"))));
    }
    @Test
    void authorizeRequest()
    {
        HttpRequest_Impl req = new HttpRequest_Impl();
        req.setToken("Authorization: Basic leeiam-mtcgToken");
        req.authorizeRequest();
        assertEquals("leeiam", req.getAuthorizedUser());
    }
}