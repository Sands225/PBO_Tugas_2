package routes;

import com.sun.net.httpserver.HttpServer;
import handlers.AuthorizationHandler;

public class Routes {
    public static void registerRoutes(HttpServer server) {
        server.createContext("/villas", new AuthorizationHandler(new VillasRoutes()));
        server.createContext("/customers", new AuthorizationHandler(new CustomersRoutes()));
        server.createContext("/vouchers", new AuthorizationHandler(new VouchersRoutes()));
    }
}