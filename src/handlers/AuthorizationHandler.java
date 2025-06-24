package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.UnauthorizedException;

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

        if (!API_KEY.equals(apiKey)) {
            throw new UnauthorizedException("Invalid API Key");
        }

        next.handle(exchange);
    }
}