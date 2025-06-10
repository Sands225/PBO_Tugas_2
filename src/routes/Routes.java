package routes;

import com.sun.net.httpserver.HttpServer;

public class Routes {
    public static void registerRoutes(HttpServer server) {
        server.createContext("/villas", new VillasRoutes());
        server.createContext("/customers", new CustomersRoutes());
        server.createContext("/vouchers", new VouchersRoutes());
    }
}