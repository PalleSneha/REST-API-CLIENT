import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

/**
 * Task 2: REST API Client
 * An interactive, lightweight space tracker that consumes a public REST API
 * to fetch and display the live position of the International Space Station.
 */
public class SpaceTracker {
    public static void main(String[] args) {
        // [1] Free public API endpoint connection URL string declaration
        String apiUri = "https://api.wheretheiss.at/v1/satellites/25544";

        // [2] Initializing Scanner using try-with-resources for auto-closing terminal resource
        try (Scanner scanner = new Scanner(System.in)) {
            // [3] Control variable to maintain the application execution status
            boolean running = true;

            // [4] Application welcome header message printed to terminal output screen
            System.out.println("🛰️ Welcome to the Live ISS Space Tracker! 🛰️");

            // [5] Continuous loop execution block until user decides to exit option
            while (running) {
                // [6] Prompt menu list displayed for functional choice selection
                System.out.println("\nChoose an Action:");
                System.out.println("1. 🌍 Fetch Live Space Station Coordinates");
                System.out.println("2. ❌ Exit");
                System.out.print("Enter your choice (1-2): ");

                // [7] Capturing user decision string input from keyboard console
                String choice = scanner.nextLine();

                // [8] Handling the captured user choices via modern rule switch expression syntax
                switch (choice) {
                    // Case 1: Triggering connection process to fetch real-time data
                    case "1" -> fetchSpaceData(apiUri);
                    
                    // Case 2: Changing state variable value to false to stop application loop cleanly
                    case "2" -> {
                        System.out.println("🚀 Closing tracker. Keep looking at the stars!");
                        running = false;
                    }
                    
                    // Default case: Handling bad user input values safely
                    default -> System.out.println("❌ Invalid choice! Enter 1 or 2.");
                }
            }
        } // Scanner session object gets automatically reclaimed here by JVM layer
    }

    /**
     * Helper method to handle the HTTP request and format the response string.
     */
    private static void fetchSpaceData(String targetUrl) {
        // [9] Informing user about remote web infrastructure connection status
        System.out.println("📡 Connecting to space tracking server...");

        // [10] Instantializing core network HttpClient resource wrapper package manually
        HttpClient client = HttpClient.newHttpClient();
        
        // [11] Enclosing risky internet operations block inside try catch environment safety architecture
        try {
            // [12] Setting up specific target location and building configuration criteria for GET network packet
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(targetUrl))
                    .GET()
                    .build();

            // [13] Blasting request packet synchronously across internet and gathering context stream back as a String
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // [14] Pulling pure readable body data payload segment out from response wrapper instance
            String jsonResponse = response.body();

            // [15] Rendering plain raw unstructured data packet to terminal output console screen
            System.out.println("\n--- 🛰️ LIVE ISS TELEMETRY DATA ---");
            System.out.println(jsonResponse); 
            
            // [16] Verification rule to validate that response string package contains critical tracking details
            if (jsonResponse.contains("latitude") && jsonResponse.contains("longitude")) {
                // [17] Invoking text extraction sub-routine to capture data blocks mapped to latitude keys
                String lat = extractValue(jsonResponse, "latitude");
                
                // [18] Invoking text extraction sub-routine to capture data blocks mapped to longitude keys
                String lon = extractValue(jsonResponse, "longitude");
                
                // [19] Printing beautifully highlighted coordinate calculations safely to display panel layout
                System.out.println("📍 Latitude : " + lat);
                System.out.println("📍 Longitude: " + lon);
            }
            // [20] Closing layout borders safely for neat representation formatting
            System.out.println("----------------------------------");

        } catch (IOException | InterruptedException e) {
            // [21] Exception handler capturing failures related to broken links or system interruptions
            System.out.println("⚠️ Connection failed: " + e.getMessage());
            
            // [22] Re-asserting interruption status flags if system processing thread gets abruptly halted
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Tiny helper method to cleanly isolate a field from the JSON string
     */
    private static String extractValue(String json, String key) {
        // [23] Scanning entire string length to determine exact index pointer position of key pattern
        int keyIndex = json.indexOf("\"" + key + "\"");
        
        // [24] Safe verification step returning non-available error tags if matching keywords aren't discovered
        if (keyIndex == -1) return "N/A";
        
        // [25] Tracking character marker index locations for key separator colons
        int colonIndex = json.indexOf(":", keyIndex);
        
        // [26] Tracking position of ending item separator commas sequentially from colon marker point
        int commaIndex = json.indexOf(",", colonIndex);
        
        // [27] Fallback checker logic resetting ending character anchor point to string bracket if comma is absent
        if (commaIndex == -1) {
            commaIndex = json.indexOf("}", colonIndex);
        }
        
        // [28] Slicing text segment out cleanly between markers and trimming out loose white spaces before delivery
        return json.substring(colonIndex + 1, commaIndex).trim();
    }
}
