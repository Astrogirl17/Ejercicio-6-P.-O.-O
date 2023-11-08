import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a method to create a CSV file named "tickets.csv".
 */
public class CsvCreator {

    /**
     * Creates a CSV file named "tickets.csv" with sample data.
     *
     * @throws IOException if an I/O error occurs while writing to the file.
     */
    public static void createCsvFile() throws IOException {
        String csvFileName = "tickets.csv";

        // Sample data for the CSV file
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Ticket ID", "Description", "Status"});
        data.add(new String[]{"1", "Fix bug in login page", "Open"});
        data.add(new String[]{"2", "Implement new feature", "In Progress"});
        data.add(new String[]{"3", "Test application", "Closed"});

        try (FileWriter writer = new FileWriter(csvFileName)) {
            // Write the data to the CSV file
            for (String[] rowData : data) {
                writer.append(String.join(",", rowData));
                writer.append("\n");
            }

            System.out.println("CSV file created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating the CSV file.");
            throw e;
        }
    }

    /**
     * Main method to test the createCsvFile() function.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            createCsvFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}