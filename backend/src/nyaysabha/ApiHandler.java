package nyaysabha;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ApiHandler implements HttpHandler {

    private static final TrialService SERVICE = new TrialService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        cors(ex);
        if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(204, -1);
            return;
        }
        try {
            String path = ex.getRequestURI().getPath(); 
            String method = ex.getRequestMethod();
            if (path.equals("/api/health") && method.equals("GET")) {
                send(ex, 200, "{\"status\":\"ok\",\"application\":\"Nyay Sabha\",\"judgeMode\":" + Json.quote(GroqJudgeService.isConfigured() ? "GROQ_AI" : "DEMO_NO_API_KEY") + "}");
            } else if (path.equals("/api/categories") && method.equals("GET")) {
                send(ex, 200, Json.array(CaseCategory.names()));
            } else if (path.equals("/api/cases") && method.equals("GET")) {
                send(ex, 200, SERVICE.cases(query(ex, "category")));
            } else if (path.equals("/api/trials/start") && method.equals("POST")) {
                send(ex, 201, SERVICE.start(form(ex)));
            } else if (path.equals("/api/trials/argument") && method.equals("POST")) {
                send(ex, 201, SERVICE.addArgument(form(ex)));
            } else if (path.equals("/api/trials/objection") && method.equals("POST")) {
                send(ex, 201, SERVICE.addObjection(form(ex)));
            } else if (path.equals("/api/trials/evaluate-round") && method.equals("POST")) {
                send(ex, 200, SERVICE.evaluateRound(form(ex)));
            } else if (path.equals("/api/trials/rest") && method.equals("POST")) {
                send(ex, 200, SERVICE.rest(form(ex)));
            } else if (path.equals("/api/trials/finish") && method.equals("POST")) {
                send(ex, 200, SERVICE.finish(form(ex)));
            } else {
                send(ex, 404, "{\"error\":\"Endpoint not found\"}");
            }
        } catch (IllegalArgumentException e) {
            send(ex, 400, "{\"error\":" + Json.quote(e.getMessage()) + "}");
        } catch (Exception e) {
            e.printStackTrace();
            send(ex, 502, "{\"error\":" + Json.quote(e.getMessage() == null ? "AI service error" : e.getMessage()) + "}");
        }
    }

    private static void cors(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "http://localhost:5173");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
    }

    private static void send(HttpExchange ex, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = ex.getResponseBody()) {
            out.write(bytes);
        }
    }

    private static Map<String, String> form(HttpExchange ex) throws IOException {
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> map = new HashMap<>();
        for (String pair : body.split("&")) {
            if (pair.isBlank()) {
                continue;
            }
            String[] p = pair.split("=", 2);
            map.put(decode(p[0]), p.length > 1 ? decode(p[1]) : "");
        }
        return map;
    }

    private static String query(HttpExchange ex, String key) {
        String q = ex.getRequestURI().getRawQuery();
        if (q == null) {
            return "";
        }
        for (String pair : q.split("&")) {
            String[] p = pair.split("=", 2);
            if (decode(p[0]).equals(key)) {
                return p.length > 1 ? decode(p[1]) : "";
            }
        }
        return "";
    }

    private static String decode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }
}
