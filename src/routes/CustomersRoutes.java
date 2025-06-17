package routes;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import handlers.RoomTypesHandler;
import handlers.VillasHandler;
import models.RoomType;
import models.Villa;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.OutputStream;

public class CustomersRoutes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        Map<String, Object> response = new HashMap<>();

        if (method.equals("GET")) {
            if (path.matches("/customers/?")) {
                response.put("message", "List of customers");
            } else if (path.matches("/customers/\\d+/?")) {
                response.put("message", "Customer detail");
            } else if (path.matches("/customers/\\d+/bookings/?")) {
                response.put("message", "Customer bookings");
            } else if (path.matches("/customers/\\d+/reviews/?")) {
                response.put("message", "Customer reviews");
            }
        } else if (method.equals("POST")) {
            } else if (path.matches("/customers/\\d+/bookings/?")) {
            } else if (path.matches("/customers/\\d+/bookings/\\d+/reviews/?")) {
                response.put("message", "Customer review");
            }
        } else if (method.equals("PUT") && path.matches("/customers/\\d+/?")) {
            response.put("message", "Update customer");
        }

        sendResponse(exchange, response);
    }

    private void sendResponse(HttpExchange exchange, Map<String, Object> data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }
}
