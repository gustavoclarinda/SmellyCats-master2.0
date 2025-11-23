package support;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.CatBreed;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CatBreedsApiStub {
    private static final String BREEDS_PATH = "/breeds";
    private static final List<CatBreed> KNOWN_BREEDS = List.of(
            new CatBreed("Abyssinian", "Ethiopia", "Natural/Standard", "Short", "Ticked"),
            new CatBreed("Aegean", "Greece", "Natural/Standard", "Semi-long", "Bi- or tri-colored"),
            new CatBreed("American Bobtail", "United States", "Mutation", "Short/Long", "All"),
            new CatBreed("American Curl", "United States", "Mutation", "Short/Long", "All"),
            new CatBreed("American Shorthair", "United States", "Natural/Standard", "Short", "All")
    );

    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpServer httpServer;
    private ExecutorService executorService;

    public void start() throws IOException {
        if (httpServer != null) {
            return;
        }

        httpServer = HttpServer.create(new InetSocketAddress(0), 0);
        httpServer.createContext(BREEDS_PATH, this::handleBreedsRequest);
        executorService = Executors.newSingleThreadExecutor();
        httpServer.setExecutor(executorService);
        httpServer.start();
    }

    public void stop() {
        if (httpServer != null) {
            httpServer.stop(0);
            httpServer = null;
        }

        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
    }

    public boolean isRunning() {
        return httpServer != null;
    }

    public String getBaseUrl() {
        if (httpServer == null) {
            throw new IllegalStateException("Stub server must be started before retrieving the base URL");
        }
        return String.format("http://localhost:%d", httpServer.getAddress().getPort());
    }

    private void handleBreedsRequest(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        int requestedLimit = resolveLimit(exchange.getRequestURI());
        int effectiveLimit = Math.min(requestedLimit, KNOWN_BREEDS.size());
        List<CatBreed> breeds = new ArrayList<>(KNOWN_BREEDS.subList(0, effectiveLimit));

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("current_page", 1);
        payload.put("per_page", effectiveLimit);
        payload.put("total", KNOWN_BREEDS.size());
        payload.put("data", breeds);

        byte[] responseBody = objectMapper.writeValueAsString(payload).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseBody.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBody);
        }
    }

    private int resolveLimit(URI requestUri) {
        String query = requestUri.getQuery();
        if (query == null || query.isBlank()) {
            return KNOWN_BREEDS.size();
        }

        Map<String, String> queryParams = toQueryParams(query);
        String limitValue = queryParams.getOrDefault("limit", String.valueOf(KNOWN_BREEDS.size()));

        try {
            int parsedValue = Integer.parseInt(limitValue);
            return Math.max(1, parsedValue);
        } catch (NumberFormatException ex) {
            return KNOWN_BREEDS.size();
        }
    }

    private Map<String, String> toQueryParams(String query) {
        if (query == null || query.isBlank()) {
            return Collections.emptyMap();
        }

        Map<String, String> params = new LinkedHashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                params.put(keyValue[0].toLowerCase(Locale.ROOT), keyValue[1]);
            }
        }
        return params;
    }
}
