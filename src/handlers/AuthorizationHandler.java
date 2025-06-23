package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class AuthorizationHandler implements HttpHandler {
    private static final String API_KEY = "12345-SECRET-KEY";
    private final HttpHandler next;

    public AuthorizationHandler(HttpHandler next) {
        this.next = next;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String apiKey = exchange.getRequestHeaders().getFirst("X-API-KEY");

        if (API_KEY.equals(apiKey)) {
            next.handle(exchange);
        } else {
            String response = "Invalid API Key";
            exchange.sendResponseHeaders(401, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}