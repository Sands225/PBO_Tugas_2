package routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.OutputStream;

public class VouchersRoutes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        Map<String, Object> response = new HashMap<>();

        if (method.equals("GET")) {
            if (path.matches("/vouchers/?")) {
                response.put("message", "List of vouchers");
            } else if (path.matches("/vouchers/\\d+/?")) {
                response.put("message", "Voucher detail");
            }
        } else if (method.equals("POST") && path.matches("/vouchers/?")) {
            response.put("message", "Create voucher");
        } else if (method.equals("PUT") && path.matches("/vouchers/\\d+/?")) {
            response.put("message", "Update voucher");
        } else if (method.equals("DELETE") && path.matches("/vouchers/\\d+/?")) {
            response.put("message", "Delete voucher");
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