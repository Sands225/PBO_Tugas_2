package routes;

import handlers.*;
import models.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VillasRoutes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> response = new HashMap<>();

        Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI());

        switch (method) {
            case "GET":
                if (path.matches("/villas/?")) {
                    List<Villa> villas = VillasHandler.getAllVillas();
                    sendJsonResponse(exchange, villas);
                    return;

                } else if (path.matches("/villas/\\d+/?")) {
                    int id = Integer.parseInt(path.replaceAll("\\D+", ""));
                    Villa villa = VillasHandler.getVillaById(id);
                    if (villa != null) {
                        sendJsonResponse(exchange, villa);
                    } else {
                        response.put("error", "Villa not found");
                        exchange.sendResponseHeaders(404, 0);
                        exchange.getResponseBody().close();
                    }
                    return;

                } else if (path.matches("/villas/\\d+/rooms/?")) {
                    int villaId = Integer.parseInt(path.split("/")[2]);
                    List<RoomType> rooms = RoomTypesHandler.getRoomsByVillaId(villaId);
                    response.put("rooms", rooms);

                } else if (path.matches("/villas/\\d+/bookings/?")) {
                    int villaId = Integer.parseInt(path.split("/")[2]);
                    List<Map<String, Object>> bookings = BookingsHandler.getBookingsByVillaId(villaId);
                    response.put("bookings", bookings);

                } else if (path.matches("/villas/\\d+/reviews/?")) {
                    int villaId = Integer.parseInt(path.split("/")[2]);
                    List<Map<String, Object>> reviews = ReviewsHandler.getReviewsByVillaId(villaId);
                    response.put("reviews", reviews);

                } else if (path.startsWith("/villas") && queryParams.containsKey("ci_date") && queryParams.containsKey("co_date")) {
                    String checkin = queryParams.get("ci_date");
                    String checkout = queryParams.get("co_date");

                    List<Map<String, Object>> availableVillas = VillasHandler.getAvailableVillas(checkin, checkout);
                    response.put("available_villas", availableVillas);
                }
                break;

            case "POST":
                if (path.matches("/villas/?")) {
                    InputStream is = exchange.getRequestBody();
                    Villa villa = mapper.readValue(is, Villa.class);

                    boolean success = VillasHandler.insertVilla(villa);
                    if (success) {
                        response.put("message", "Villa created successfully");
                    } else {
                        response.put("error", "Failed to create villa");
                    }
                    sendResponse(exchange, response);
                    return;

                } else if (path.matches("/villas/\\d+/rooms/?")) {
                    int villaId = Integer.parseInt(path.split("/")[2]);
                    InputStream is = exchange.getRequestBody();
                    RoomType room = mapper.readValue(is, RoomType.class);
                    room.setVilla(villaId);

                    boolean success = RoomTypesHandler.insertRoomType(room);
                    if (success) {
                        response.put("message", "Room type added to villa successfully");
                    } else {
                        response.put("error", "Failed to add room type to villa");
                    }
                    sendResponse(exchange, response);
                    return;
                }
                break;

            case "PUT":
                if (path.matches("/villas/\\d+/?")) {
                    int villaId = Integer.parseInt(path.split("/")[2]);
                    InputStream is = exchange.getRequestBody();
                    Villa villa = mapper.readValue(is, Villa.class);
                    villa.setId(villaId);  // Make sure Villa model has setId()

                    boolean success = VillasHandler.updateVilla(villa);
                    if (success) {
                        response.put("message", "Villa updated successfully");
                    } else {
                        response.put("error", "Failed to update villa");
                    }
                    sendResponse(exchange, response);
                    return;

                } else if (path.matches("/villas/\\d+/rooms/\\d+/?")) {
                    String[] parts = path.split("/");
                    int villaId = Integer.parseInt(parts[2]);
                    int roomId = Integer.parseInt(parts[4]);

                    InputStream is = exchange.getRequestBody();
                    RoomType room = mapper.readValue(is, RoomType.class);
                    room.setId(roomId);
                    room.setVilla(villaId);

                    boolean success = RoomTypesHandler.updateRoomType(room);
                    if (success) {
                        response.put("message", "Room type updated successfully");
                    } else {
                        response.put("error", "Failed to update room type");
                    }
                    sendResponse(exchange, response);
                    return;
                }
                break;

            case "DELETE":
                if (path.matches("/villas/\\d+/?")) {
                    int villaId = Integer.parseInt(path.split("/")[2]);
                    boolean success = VillasHandler.deleteVillaById(villaId);

                    if (success) {
                        response.put("message", "Villa deleted successfully");
                    } else {
                        response.put("error", "Failed to delete villa");
                    }
                    sendResponse(exchange, response);
                    return;

                } else if (path.matches("/villas/\\d+/rooms/\\d+/?")) {
                    String[] parts = path.split("/");
                    int villaId = Integer.parseInt(parts[2]);
                    int roomId = Integer.parseInt(parts[4]);

                    boolean success = RoomTypesHandler.deleteRoomTypeById(roomId, villaId);

                    if (success) {
                        response.put("message", "Room deleted successfully");
                    } else {
                        response.put("error", "Failed to delete room");
                    }
                    sendResponse(exchange, response);
                    return;
                }
                break;
        }

        sendResponse(exchange, response);
    }

    private Map<String, String> parseQueryParams(URI uri) {
        Map<String, String> queryParams = new HashMap<>();
        String query = uri.getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2) {
                    queryParams.put(URLDecoder.decode(pair[0], StandardCharsets.UTF_8),
                            URLDecoder.decode(pair[1], StandardCharsets.UTF_8));
                }
            }
        }
        return queryParams;
    }

    public static void sendResponse(HttpExchange exchange, Map<String, Object> responseMap) throws IOException {
        String responseJson = new ObjectMapper().writeValueAsString(responseMap);
        byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseBytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }

    private void sendJsonResponse(HttpExchange exchange, Object data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes());
        }
    }
}
