package at.fhtw.tourplanner.http;

import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.model.Tour_log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestServiceClient {
    private final HttpClient httpClient;
    private final String baseUrl = "http://localhost:10201/api";
    private final ObjectMapper mapper = new ObjectMapper();

    public RestServiceClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public String fetchData(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + endpoint))
                .build();

        HttpResponse<String> response = send(request);

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new Exception("Error occurred: HTTP " + response.statusCode());
        }
    }

    protected HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void sendData(String endpoint, Tour_log tourLog) throws Exception {
        String json = convertTourLogToJson(tourLog);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + endpoint))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = send(request);

        if (response.statusCode() != 200) {
            throw new Exception("Error occurred: HTTP " + response.statusCode());
        }
    }

    private String convertTourLogToJson(Tour_log tourLog) throws Exception {
        return mapper.writeValueAsString(tourLog);
    }

    public void deleteData(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + endpoint))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204) {
            throw new Exception("Failed to delete data: " + response.body());
        }
    }

    public List<Map<String, Object>> getAutocompleteSuggestions(String endpoint, String searchText) throws Exception {
        URI uri = new URI(baseUrl + endpoint + "?query=" + URLEncoder.encode(searchText, StandardCharsets.UTF_8));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = send(request);

        if (response.statusCode() == 200) {
            JsonNode rootNode = mapper.readTree(response.body());
            JsonNode featuresNode = rootNode.path("features");

            List<Map<String, Object>> results = new ArrayList<>();
            if (featuresNode.isArray()) {
                for (JsonNode featureNode : featuresNode) {
                    Map<String, Object> result = new HashMap<>();
                    if (!featureNode.path("properties").path("label").isMissingNode()) {
                        String label = featureNode.path("properties").path("label").asText();
                        result.put("label", label);
                    }
                    if (!featureNode.path("geometry").path("coordinates").isMissingNode()) {
                        List<Double> coordinates = new ArrayList<>();
                        for (JsonNode coordinateNode : featureNode.path("geometry").path("coordinates")) {
                            coordinates.add(coordinateNode.asDouble());
                        }
                        result.put("coordinates", coordinates);
                    }
                    if (!rootNode.path("bbox").isMissingNode()) {
                        List<Double> bbox = new ArrayList<>();
                        for (JsonNode bboxNode : rootNode.path("bbox")) {
                            bbox.add(bboxNode.asDouble());
                        }
                        result.put("bbox", bbox);
                    }
                    results.add(result);
                }
            }
            return results;
        } else {
            throw new Exception("Error occurred: HTTP " + response.statusCode() + " with body " + response.body());
        }
    }

    public String postRoute(List<Double> startCoordinates, List<Double> endCoordinates) throws Exception {
        String startCoordinatesParam = startCoordinates.get(0) + "," + startCoordinates.get(1);
        String endCoordinatesParam = endCoordinates.get(0) + "," + endCoordinates.get(1);

        URI uri = new URI(baseUrl + "/route?startCoordinates=" + startCoordinatesParam + "&endCoordinates=" + endCoordinatesParam);
        System.out.println(uri);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = send(request);

        if (response.statusCode() != 200) {
            throw new Exception("Error occurred: HTTP " + response.statusCode());
        }
        return response.body();
    }

    public void sendData(String endpoint, Tour tour) throws Exception {
        String json = convertTourToJson(tour);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + endpoint))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = send(request);

        if (response.statusCode() != 200) {
            throw new Exception("Error occurred: HTTP " + response.statusCode());
        }
    }

    private String convertTourToJson(Tour tour) throws Exception {
        return mapper.writeValueAsString(tour);
    }

    public List<Tour> fetchTours(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + endpoint))
                .build();

        HttpResponse<String> response = send(request);

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<List<Tour>>() {});
        } else {
            throw new Exception("Error occurred: HTTP " + response.statusCode());
        }
    }
    public void uploadFile(String endpoint, File file) throws Exception {
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        HttpRequest.BodyPublisher bodyPublisher = ofMimeMultipartData(file, boundary);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + endpoint))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(bodyPublisher)
                .build();

        HttpResponse<String> response = send(request);

        if (response.statusCode() != 200) {
            throw new Exception("Error occurred: HTTP " + response.statusCode());
        }
    }

    public String downloadFile(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + endpoint))
                .build();

        HttpResponse<String> response = send(request);

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new Exception("Error occurred: HTTP " + response.statusCode());
        }
    }

    private HttpRequest.BodyPublisher ofMimeMultipartData(File file, String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8);

        // Form field
        byteArrays.add(separator);
        byteArrays.add("Content-Disposition: form-data; name=\"file\"; filename=\"".getBytes(StandardCharsets.UTF_8));
        byteArrays.add(file.getName().getBytes(StandardCharsets.UTF_8));
        byteArrays.add("\"\r\n".getBytes(StandardCharsets.UTF_8));
        byteArrays.add("Content-Type: text/csv\r\n\r\n".getBytes(StandardCharsets.UTF_8));
        byteArrays.add(Files.readAllBytes(file.toPath()));
        byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));

        // End
        byteArrays.add(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }
    public List<Tour> searchTours(String endpoint, String query) throws Exception {
        URI uri = new URI(baseUrl + endpoint + "?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = send(request);

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<List<Tour>>() {});
        } else {
            throw new Exception("Error occurred: HTTP " + response.statusCode() + " with body " + response.body());
        }
    }
}
