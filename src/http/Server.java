package http;

import com.sun.net.httpserver.HttpServer;
import routes.Routes;
import java.net.InetSocketAddress;

public class Server {
    private HttpServer server;
    private int port;

    public Server(int port) throws Exception {
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        Routes.registerRoutes(server);
        server.setExecutor(null);
    }

    public void start() {
        server.start();
        System.out.printf("Server started and listening on port: %d%n", port);
    }
}