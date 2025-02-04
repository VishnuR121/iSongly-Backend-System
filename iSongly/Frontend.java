import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface {
    private BackendInterface backend;
    private Scanner scanner;
    private Integer maxYear;
    private Integer minYear;

    public Frontend(Scanner scanner, BackendInterface backend) {
        this.backend = backend;
        this.scanner = scanner;
        this.maxYear = null;
        this.minYear = null;
    }

    /**
     * Repeatedly gives the user an opportunity to issue new commands until
     * they select Q to quit. Uses the scanner passed to the constructor to
     * read user input.
     */
    @Override
    public void runCommandLoop() {
        String userInput = "";

        // get user input and run the method corresponding to user's input
        while (!userInput.equalsIgnoreCase("Q")) {
            displayMainMenu();

            if (!scanner.hasNextLine()) {
                break;
            }

            userInput = scanner.nextLine().toUpperCase();

            if (userInput.isEmpty()) {
                System.out.println("Input cannot be empty");
                continue;
            }

            switch (userInput) {
                case "L":
                    loadFile();
                    break;
                case "G":
                    getSongs();
                    break;
                case "F":
                    setFilter();
                    break;
                case "D":
                    displayTopFive();
                    break;
                default:
                    System.out.println("Invalid input to display. Try again.");
            }
        }
    }

    /**
     * Displays the menu of command options to the user. Giving the user the
     * instructions of entering L, G, F, D, or Q (case insensitive) to load a
     * file, get songs, set filter, display the top five, or quit.
     */
    @Override
    public void displayMainMenu() {
        System.out.println("Main Menu");
        System.out.println("Load File [L]");
        System.out.println("Get Songs [G]");
        System.out.println("Set Filter [F]");
        System.out.println("Display Top Five [D]");
        System.out.println("Quit Menu [Q]");
        System.out.print("Enter Input: ");
    }

    // Each of the following commands are designed to handle the user
    // interaction associated with a different Main Menu command.

    /**
     * Provides text-based user interface for prompting the user to select
     * the csv file that they would like to load, provides feedback about
     * whether this is successful vs any errors are encountered.
     * [L]oad Song File
     *
     * When the user enters a valid filename, the file with that name
     * should be loaded.
     * Uses the scanner passed to the constructor to read user input and
     * the backend passed to the constructor to load the file provided
     * by the user. If the backend indicates a problem with finding or
     * reading the file by throwing an IOException, a message is displayed
     * to the user, and they will be asked to enter a new filename.
     */
    @Override
    public void loadFile() {
        String filename = "";

        while (true) {
            System.out.print("Enter the filename to load: ");

            if (!scanner.hasNextLine()) {
                break;
            }

            filename = scanner.nextLine();

            // get the filename to load form the user and read its data
            try {
                backend.readData(filename);
                System.out.println("File: " + filename + " loaded successfully.");
                break;
            } catch (Exception e) {
                System.out.println("Error loading file: " + filename + ". Try again." + e.getMessage());
            }
        }
    }

    /**
     * Provides text-based user interface and error handling for retrieving a
     * list of song titles that are sorted by Year. The user should be
     * given the opportunity to optionally specify a minimum and/or maximum
     * Year to limit the number of songs displayed to that range.
     * [G]et Songs by Year
     *
     * When the user enters only two numbers (pressing enter after each), the
     * first of those numbers should be interpreted as the minimum, and the
     * second as the maximum Year.
     * Uses the scanner passed to the constructor to read user input and
     * the backend passed to the constructor to retrieve the list of sorted
     * songs.
     */
    @Override
    public void getSongs() {
        String minYearStr = null;
        String maxYearStr = null;

        try {
            System.out.print("Enter minimum year: ");
            minYearStr = scanner.nextLine().trim();
            System.out.print("Enter maximum year: ");
            maxYearStr = scanner.nextLine().trim();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a numeric string value for the years." + e.getMessage());
        }

        minYear = Integer.parseInt(minYearStr);
        maxYear = Integer.parseInt(maxYearStr);

        // get max and min year from the user and filter the range of songs by year
        try {
            System.out.println("Songs in specified range (" + minYear + " - " + maxYear + "):");
            for (String song : backend.getRange(minYear, maxYear)) {
                System.out.println(song);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving songs. Try modifying the filters." + e.getMessage());
        }
    }

    /**
     * Provides text-based user interface and error handling for setting a
     * filter threshold. This and future requests to retrieve songs will
     * will only return the titles of songs that are smaller than the
     * user specified Loudness. The user should also be able to clear
     * any previously specified filters.
     * [F]ilter Songs by Loudness
     * 
     * When the user enters only a single number, that number should be used
     * as the new filter threshold.
     * Uses the scanner passed to the constructor to read user input and
     * the backend passed to the constructor to set the filters provided by
     * the user and retrieve songs that maths the filter criteria.
     */
    @Override
    public void setFilter() {
        System.out.print("Enter loudness: ");
        String loudnessThresholdStr = scanner.nextLine().trim();

        // if user does not enter threshold, keep it empty
        if (loudnessThresholdStr.isEmpty()) {
            backend.setFilter(null);
            System.out.println("Filter cleared");
        } else {
            // filter songs by loudness threshold provided by user
            try {
                Integer loudnessThreshold = Integer.parseInt(loudnessThresholdStr);
                backend.setFilter(loudnessThreshold);
                System.out.println("Filter set");
            } catch (Exception e) {
                System.out.println("Invalid input for a filter threshold. Please enter a valid numeric value.");
            }
        }
    }

    /**
     * Displays the titles of up to five of the most Danceable songs within the
     * previously set Year range and smaller than the specified
     * Loudness. If there are no such songs, then this method should
     * indicate that and recommend that the user change their current range or
     * filter settings.
     * [D]isplay five most Danceable
     * 
     * The user should not need to enter any input when running this command.
     * Uses the backend passed to the constructor to retrieve the list of up
     * to five songs.
     */
    @Override
    public void displayTopFive() {
        System.out.println("Top five most danceable songs:");

        // use backend interface method to get the five most danceable songs
        try {
            List<String> songs = backend.fiveMost();

            if (songs.isEmpty()) {
                System.out.println(
                        "No songs match the current filter or range. Try changing range and/or filter settings.");
            } else {
                for (String song : songs) {
                    System.out.println(song);
                }
            }
        } catch (Exception e) {
            System.out.println("Error displaying top five songs, try modifying the filters." + e.getMessage());
        }
    }
}