package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.DatabaseException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedException;

import java.io.IOException;
import java.io.OutputStream;

import static utils.SendResponseUtils.sendErrorResponse;

public class AuthorizationHandler implements HttpHandler {
    private static final String API_KEY = "12345-SECRET-KEY";
    private final HttpHandler next;

    public AuthorizationHandler(HttpHandler next) {
        this.next = next;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String apiKey = exchange.getRequestHeaders().getFirst("X-API-KEY");

            if (!API_KEY.equals(apiKey)) {
                throw new UnauthorizedException("Invalid API Key");
            }

            next.handle(exchange);

        } catch (UnauthorizedException e) {
            sendErrorResponse(exchange, e.getMessage(), 401);
        } catch (NotFoundException e) {
            sendErrorResponse(exchange, e.getMessage(), 404);
        } catch (DatabaseException e) {
            sendErrorResponse(exchange, "Database error: " + e.getMessage(), 500);
        } catch (Exception e) {
            sendErrorResponse(exchange, "Internal server error: " + e.getMessage(),500);
        }
    }
}