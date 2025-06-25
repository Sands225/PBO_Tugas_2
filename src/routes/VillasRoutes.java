package routes;

import handlers.*;
import models.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.*;

import java.io.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;


public class VillasRoutes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        Map<String, Object> response = new HashMap<>();

        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> queryParams = QueryParamsUtils.parseQueryParams(exchange.getRequestURI());

            switch (method) {
                case "GET":
                    if (path.matches("/villas/?")) {
                        List<Villa> villas = VillasHandler.getAllVillas();
                        SendResponseUtils.sendJsonResponse(exchange, villas, "Villas retrieved successfully.");
                        return;

                    } else if (path.matches("/villas/\\d+/?")) {
                        int id = Integer.parseInt(path.replaceAll("\\D+", ""));
                        Villa villa = VillasHandler.getVillaById(id);

                        SendResponseUtils.sendJsonResponse(exchange, villa, "Villa with ID " + id + " retrieved successfully.");
                        return;

                    } else if (path.matches("/villas/\\d+/rooms/?")) {
                        int villaId = Integer.parseInt(path.split("/")[2]);

                        // check if villa exist
                        VillasHandler.getVillaById(villaId);

                        List<Room> rooms = RoomsHandler.getRoomsByVillaId(villaId);
                        SendResponseUtils.sendJsonResponse(exchange, rooms, "Rooms in Villa with ID " + villaId + " retrieved successfully.");

                    } else if (path.matches("/villas/\\d+/bookings/?")) {
                        int villaId = Integer.parseInt(path.split("/")[2]);

                        // check if villa exist
                        VillasHandler.getVillaById(villaId);

                        List<Map<String, Object>> bookings = BookingsHandler.getBookingsByVillaId(villaId);
                        SendResponseUtils.sendJsonResponse(exchange, bookings, "Bookings in Villa with ID " + villaId + " retrieved successfully.");

                    } else if (path.matches("/villas/\\d+/reviews/?")) {
                        int villaId = Integer.parseInt(path.split("/")[2]);

                        // check if villa exist
                        VillasHandler.getVillaById(villaId);

                        List<Map<String, Object>> reviews = ReviewsHandler.getReviewsByVillaId(villaId);
                        SendResponseUtils.sendJsonResponse(exchange, reviews, "Reviews in Villa with ID " + villaId + " retrieved successfully.");

                    } else if (path.startsWith("/villas") && queryParams.containsKey("ci_date") && queryParams.containsKey("co_date")) {
                        String checkin = queryParams.get("ci_date");
                        String checkout = queryParams.get("co_date");

                        List<Map<String, Object>> availableVillas = VillasHandler.getAvailableVillas(checkin, checkout);

                        SendResponseUtils.sendJsonResponse(exchange, availableVillas, "Villa  retrieved successfully.");
                    } else {
                        SendResponseUtils.sendErrorResponse(exchange, "Path " + path + " not found.", 404);
                        return;
                    }
                    break;

                case "POST":
                    if (path.matches("/villas/?")) {
                        InputStream is = exchange.getRequestBody();
                        Villa villa = mapper.readValue(is, Villa.class);

                        VillasHandler.insertVilla(villa);
                        SendResponseUtils.sendSuccessResponse(exchange, "Villa created successfully", response, 200);
                        return;

                    } else if (path.matches("/villas/\\d+/rooms/?")) {
                        int villaId = Integer.parseInt(path.split("/")[2]);
                        InputStream is = exchange.getRequestBody();
                        Room room = mapper.readValue(is, Room.class);
                        room.setVilla(villaId);

                        // check if villa exist
                        Villa villa = VillasHandler.getVillaById(villaId);

                        boolean success = RoomsHandler.insertRoomType(room);
                        SendResponseUtils.sendSuccessResponse(exchange, "Room successfully added to Villa", response, 200);
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

                            Villa villa = mapper.readValue(is, Villa.class);
                            villa.setId(villaId);

                            boolean success = VillasHandler.updateVilla(villa);
                            SendResponseUtils.sendSuccessResponse(exchange, "Villa updated successfully.", villa, 200);
                        } catch (IOException e) {
                            response.put("error", "Invalid input: " + e.getMessage());
                            SendResponseUtils.sendErrorResponse(exchange, "Invalid input: ", 400);
                        }
                        return;
                    } else if (path.matches("/villas/\\d+/rooms/\\d+/?")) {
                        String[] parts = path.split("/");
                        int villaId = Integer.parseInt(parts[2]);
                        int roomId = Integer.parseInt(parts[4]);
                        InputStream is = exchange.getRequestBody();

                        // check if villa exist
                        Villa existingVilla = VillasHandler.getVillaById(villaId);

                        // check if room exist
                        Room existingRoom = RoomsHandler.getRoomByVillaAndRoomId(villaId, roomId);

                        Room room = mapper.readValue(is, Room.class);
                        room.setId(roomId);
                        room.setVilla(villaId);

                        boolean success = RoomsHandler.updateRoomType(room);
                        SendResponseUtils.sendSuccessResponse(exchange, "Room updated succcessfully", room, 200);
                        return;
                    }
                    break;

                case "DELETE":
                    if (path.matches("/villas/\\d+/?")) {
                        int villaId = Integer.parseInt(path.split("/")[2]);

                        // check if villa exist
                        Villa existingVilla = VillasHandler.getVillaById(villaId);

                        boolean success = VillasHandler.deleteVillaById(villaId);
                        SendResponseUtils.sendSuccessResponse(exchange, "Villa deleted successfully", existingVilla, 200);
                        return;

                    } else if (path.matches("/villas/\\d+/rooms/\\d+/?")) {
                        String[] parts = path.split("/");
                        int villaId = Integer.parseInt(parts[2]);
                        int roomId = Integer.parseInt(parts[4]);

                        // check if villa exist
                        Villa existingVilla = VillasHandler.getVillaById(villaId);

                        // check if room exist
                        Room existingRoom = RoomsHandler.getRoomByVillaAndRoomId(villaId, roomId);

                        boolean success = RoomsHandler.deleteRoomTypeById(roomId, villaId);
                        SendResponseUtils.sendSuccessResponse(exchange, "Room deleted successfully", existingRoom, 200);
                        return;
                    }
                    break;

                default:
                    SendResponseUtils.sendErrorResponse(exchange, "Method not allowed", 405);
            }
        } catch (exceptions.NotFoundException e) {
            System.out.println(e.getMessage());
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 404);
        } catch (exceptions.DatabaseException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 500);
        } catch (NumberFormatException e) {
            SendResponseUtils.sendErrorResponse(exchange, "Invalid number format: " + e.getMessage(), 400);
        } catch (IOException e) {
            SendResponseUtils.sendErrorResponse(exchange, "I/O error: " + e.getMessage(), 500);
        } catch (Exception e) {
            SendResponseUtils.sendErrorResponse(exchange, "Unexpected error: " + e.getMessage(), 500);
        }
    }
}
