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
                        sendResponse(exchange, response, 404);
                        return;
                    }
                    return;

                } else if (path.matches("/villas/\\d+/rooms/?")) {
                    int villaId = Integer.parseInt(path.split("/")[2]);

                    // check if villa exist
                    Villa villa = VillasHandler.getVillaById(villaId);
                    if (villa == null) {
                        response.put("error", "Villa with id " + villaId + " not found");
                        sendResponse(exchange, response, 404);
                        return;
                    }

                    List<Room> rooms = RoomsHandler.getRoomsByVillaId(villaId);
                    response.put("rooms", rooms);

                } else if (path.matches("/villas/\\d+/bookings/?")) {
                    int villaId = Integer.parseInt(path.split("/")[2]);

                    // check if villa exist
                    Villa villa = VillasHandler.getVillaById(villaId);
                    if (villa == null) {
                        response.put("error", "Villa with id " + villaId + " not found");
                        sendResponse(exchange, response, 404);
                        return;
                    }

                    List<Map<String, Object>> bookings = BookingsHandler.getBookingsByVillaId(villaId);
                    response.put("bookings", bookings);

                } else if (path.matches("/villas/\\d+/reviews/?")) {
                    int villaId = Integer.parseInt(path.split("/")[2]);

                    // check if villa exist
                    Villa villa = VillasHandler.getVillaById(villaId);
                    if (villa == null) {
                        response.put("error", "Villa with id " + villaId + " not found");
                        sendResponse(exchange, response, 404);
                        return;
                    }

                    List<Map<String, Object>> reviews = ReviewsHandler.getReviewsByVillaId(villaId);
                    response.put("reviews", reviews);

                } else if (path.startsWith("/villas") && queryParams.containsKey("ci_date") && queryParams.containsKey("co_date")) {
                    String checkin = queryParams.get("ci_date");
                    String checkout = queryParams.get("co_date");

                    List<Map<String, Object>> availableVillas = VillasHandler.getAvailableVillas(checkin, checkout);
                    if (availableVillas == null) {
                        response.put("error", "No available villas found at " + checkin + " and " + checkout);
                        sendResponse(exchange, response, 404);
                        return;
                    }

                    response.put("available_villas", availableVillas);
                } else {
                    response.put("error", "Path " + path + " not found.");
                    sendResponse(exchange, response, 404);
                    return;
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
                    Room room = mapper.readValue(is, Room.class);
                    room.setVilla(villaId);

                    // check if villa exist
                    Villa villa = VillasHandler.getVillaById(villaId);
                    if (villa == null) {
                        response.put("error", "Villa with id " + villaId + " not found");
                        sendResponse(exchange, response, 404);
                        return;
                    }

                    boolean success = RoomsHandler.insertRoomType(room);
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

                    try {
                        // check if villa exist
                        Villa existingVilla = VillasHandler.getVillaById(villaId);
                        if (existingVilla == null) {
                            response.put("error", "Villa with id " + villaId + " not found");
                            sendResponse(exchange, response, 404);
                            return;
                        }

                        Villa villa = mapper.readValue(is, Villa.class);
                        villa.setId(villaId);

                        boolean success = VillasHandler.updateVilla(villa);
                        if (success) {
                            response.put("message", "Villa updated successfully");
                            sendResponse(exchange, response, 200);
                        } else {
                            response.put("error", "Failed to update villa");
                            sendResponse(exchange, response, 500);
                        }
                    } catch (IOException e) {
                        response.put("error", "Invalid input: " + e.getMessage());
                        sendResponse(exchange, response, 400);
                    }
                    return;
                } else if (path.matches("/villas/\\d+/rooms/\\d+/?")) {
                    String[] parts = path.split("/");
                    int villaId = Integer.parseInt(parts[2]);
                    int roomId = Integer.parseInt(parts[4]);
                    InputStream is = exchange.getRequestBody();

                    // check if villa exist
                    Villa existingVilla = VillasHandler.getVillaById(villaId);
                    if (existingVilla == null) {
                        response.put("error", "Villa with id " + villaId + " not found");
                        sendResponse(exchange, response, 404);
                        return;
                    }

                    // check if room exist
                    Room existingRoom = RoomsHandler.getRoomByVillaAndRoomId(villaId, roomId);
                    if (existingRoom == null) {
                        response.put("error", "Room with id " + roomId + " not found");
                        sendResponse(exchange, response, 404);
                        return;
                    }

                    Room room = mapper.readValue(is, Room.class);
                    room.setId(roomId);
                    room.setVilla(villaId);

                    boolean success = RoomsHandler.updateRoomType(room);
                    if (success) {
                        response.put("message", "Room updated successfully");
                        sendResponse(exchange, response, 200);
                    } else {
                        response.put("error", "Failed to update room");
                        sendResponse(exchange, response, 500);
                    }
                    return;
                }
                break;

            case "DELETE":
                if (path.matches("/villas/\\d+/?")) {
                    int villaId = Integer.parseInt(path.split("/")[2]);

                    // check if villa exist
                    Villa existingVilla = VillasHandler.getVillaById(villaId);
                    if (existingVilla == null) {
                        response.put("error", "Villa with id " + villaId + " not found");
                        sendResponse(exchange, response, 404);
                        return;
                    }

                    boolean success = VillasHandler.deleteVillaById(villaId);
                    if (success) {
                        response.put("message", "Villa deleted successfully");
                        sendResponse(exchange, response, 200);
                    } else {
                        response.put("error", "Failed to delete villa");
                        sendResponse(exchange, response, 500);
                    }
                    return;

                } else if (path.matches("/villas/\\d+/rooms/\\d+/?")) {
                    String[] parts = path.split("/");
                    int villaId = Integer.parseInt(parts[2]);
                    int roomId = Integer.parseInt(parts[4]);

                    // check if villa exist
                    Villa existingVilla = VillasHandler.getVillaById(villaId);
                    if (existingVilla == null) {
                        response.put("error", "Villa with id " + villaId + " not found");
                        sendResponse(exchange, response, 404);
                        return;
                    }

                    // check if room exist
                    Room existingRoom = RoomsHandler.getRoomByVillaAndRoomId(villaId, roomId);
                    if (existingRoom == null) {
                        response.put("error", "Room with id " + roomId + " not found");
                        sendResponse(exchange, response, 404);
                        return;
                    }

                    boolean success = RoomsHandler.deleteRoomTypeById(roomId, villaId);
                    if (success) {
                        response.put("message", "Villa deleted successfully");
                        sendResponse(exchange, response, 200);
                    } else {
                        response.put("error", "Failed to delete villa");
                        sendResponse(exchange, response, 500);
                    }
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

    public static void sendResponse(HttpExchange exchange, Map<String, Object> responseMap, int responseCode) throws IOException {
        String responseJson = new ObjectMapper().writeValueAsString(responseMap);
        byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(responseCode, responseBytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
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
