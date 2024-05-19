package at.fhtw.backend.util;

import at.fhtw.backend.model.Tour;
import org.apache.commons.csv.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvHelper {

    // CSV Header names
    private static final String[] CSV_HEADER = { "TourId", "Name", "Description", "FromLocation", "ToLocation", "TransportType", "Distance", "EstimatedTime", "RouteInformation", "FromCoordinates", "ToCoordinates", "Popularity", "ChildFriendliness" };

    /**
     * Converts a list of tours to a CSV formatted input stream.
     * @param tours the list of tours
     * @return ByteArrayInputStream containing the CSV data
     */
    public static ByteArrayInputStream toursToCsv(List<Tour> tours) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL).withHeader(CSV_HEADER);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            for (Tour tour : tours) {
                List<String> data = Arrays.asList(
                        String.valueOf(tour.getTourId()),
                        tour.getName(),
                        tour.getDescription(),
                        tour.getFromLocation(),
                        tour.getToLocation(),
                        tour.getTransportType(),
                        String.valueOf(tour.getDistance()),
                        String.valueOf(tour.getEstimatedTime()),
                        tour.getRouteInformation(),
                        tour.getFromCoordinates().stream().map(String::valueOf).collect(Collectors.joining(",")),
                        tour.getToCoordinates().stream().map(String::valueOf).collect(Collectors.joining(",")),
                        String.valueOf(tour.getPopularity()),
                        String.valueOf(tour.getChildFriendliness())
                );
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to CSV file: " + e.getMessage());
        }
    }

    /**
     * Converts a CSV formatted file to a list of tours.
     * @param file the CSV file
     * @return List of Tour objects
     */
    public static List<Tour> csvToTours(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Tour> tours = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Tour tour = new Tour(
                        Long.parseLong(csvRecord.get("TourId")),
                        csvRecord.get("Name"),
                        csvRecord.get("Description"),
                        csvRecord.get("FromLocation"),
                        csvRecord.get("ToLocation"),
                        csvRecord.get("TransportType"),
                        Double.parseDouble(csvRecord.get("Distance")),
                        Double.parseDouble(csvRecord.get("EstimatedTime")),
                        csvRecord.get("RouteInformation"),
                        Arrays.stream(csvRecord.get("FromCoordinates").split(",")).map(Double::parseDouble).collect(Collectors.toList()),
                        Arrays.stream(csvRecord.get("ToCoordinates").split(",")).map(Double::parseDouble).collect(Collectors.toList()),
                        Integer.parseInt(csvRecord.get("Popularity")),
                        Integer.parseInt(csvRecord.get("ChildFriendliness"))
                );
                tours.add(tour);
            }
            return tours;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }

    /**
     * Checks if the given file has a CSV format.
     * @param file the file to check
     * @return true if the file is in CSV format, false otherwise
     */
    public static boolean hasCSVFormat(MultipartFile file) {
        String TYPE = "text/csv";
        return TYPE.equals(file.getContentType()) || file.getContentType().equals("application/vnd.ms-excel");
    }
}
