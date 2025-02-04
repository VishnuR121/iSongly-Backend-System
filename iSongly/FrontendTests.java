import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import java.util.Scanner;

/**
 * This tester class test Frontend.java to see if menu options work correctly.
 */
public class FrontendTests {
    /**
     * This test checks whether the user can successfully load a file by
     * simulating input for the "L" menu option and verifying the output.
     */
    @Test
    public void roleTest1() {
        TextUITester tester = new TextUITester("L\nsongs.csv\nQ\n");
        Scanner scanner = new Scanner(System.in);

        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        BackendInterface backend = new Backend_Placeholder(tree);
        Frontend frontend = new Frontend(scanner, backend);

        frontend.runCommandLoop();

        String output = tester.checkOutput();

        assertTrue(output.contains("File: songs.csv loaded successfully."), "Loading tester failed");
    }

    /**
     * This test checks whether the user can retrieve songs within a specified year
     * range
     * by simulating input for the "G" menu option.
     */
    @Test
    public void roleTest2() {
        TextUITester tester = new TextUITester("G\n2010\n2020\nQ\n");
        Scanner scanner = new Scanner(System.in);

        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        BackendInterface backend = new Backend_Placeholder(tree);
        Frontend frontend = new Frontend(scanner, backend);

        frontend.runCommandLoop();

        String output = tester.checkOutput();
        assertTrue(output.contains("Songs in specified range"), "Get songs by year range tester failed");
    }

    /**
     * This test checks whether the user can set a filter for loudness and retrieve
     * the top 5 danceable songs
     * by simulating input for the "F" and "D" menu options.
     */
    @Test
    public void roleTest3() {
        TextUITester tester = new TextUITester("F\n-3\nD\nQ\n");
        Scanner scanner = new Scanner(System.in);

        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        BackendInterface backend = new Backend_Placeholder(tree);
        Frontend frontend = new Frontend(scanner, backend);

        frontend.runCommandLoop();

        String output = tester.checkOutput();
        assertTrue(output.contains("Top five most danceable songs"),
                "Loudness filtering and top five msot dancable songs tester failed");
    }

    /**
     * This integration test verifies that the user can successfully load a file 
     * and then immediately attempt to display the top five songs.
     * It checks if the application responds appropriately when songs are not yet filtered.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void integrationTest1() {
        TextUITester tester = new TextUITester("L\nsongs.csv\nD\nQ\n");
        Scanner scanner = new Scanner(System.in);

        IterableSortedCollection<Song> tree = new IterableRedBlackTree();
        BackendInterface backend = new Backend(tree);
        Frontend frontend = new Frontend(scanner, backend);

        frontend.runCommandLoop();

        String output = tester.checkOutput();
        assertTrue(output.contains("Top five most danceable songs"), "Displaying top five songs after loading failed");
    }

    /**
     * This integration test checks if the user can set a filter for songs 
     * based on loudness and then retrieve songs that meet the criteria.
     * It ensures the application functions correctly after applying a filter.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void integrationTest2() {
        TextUITester tester = new TextUITester("L\nsongs.csv\nF\n-5\nG\nQ\n");
        Scanner scanner = new Scanner(System.in);

        IterableSortedCollection<Song> tree = new IterableRedBlackTree();
        BackendInterface backend = new Backend(tree);
        Frontend frontend = new Frontend(scanner, backend);

        frontend.runCommandLoop();

        String output = tester.checkOutput();
        assertTrue(output.contains("Retrieved songs successfully after filter"), "Getting filtered songs failed");
    }

    /**
     * This integration test checks if the application can handle an invalid filter input 
     * gracefully without crashing and still allow the user to continue interacting.
     * It tests how well the frontend and backend handle unexpected inputs.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void integrationTest3() {
        TextUITester tester = new TextUITester("L\nsongs.csv\nF\ninvalid\nG\nQ\n");
        Scanner scanner = new Scanner(System.in);

        IterableSortedCollection<Song> tree = new IterableRedBlackTree();
        BackendInterface backend = new Backend(tree);
        Frontend frontend = new Frontend(scanner, backend);

        frontend.runCommandLoop();

        String output = tester.checkOutput();
        assertTrue(output.contains("Invalid filter input"), "Invalid filter handling failed");
    }

    /**
     * This integration test verifies that the application properly exits when 
     * the user selects the quit option after interacting with other commands.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void integrationTest4() {
        TextUITester tester = new TextUITester("L\nsongs.csv\nG\nF\n-5\nD\nQ\n");
        Scanner scanner = new Scanner(System.in);

        IterableSortedCollection<Song> tree = new IterableRedBlackTree();
        BackendInterface backend = new Backend(tree);
        Frontend frontend = new Frontend(scanner, backend);

        frontend.runCommandLoop();

        String output = tester.checkOutput();
        assertTrue(output.contains("Exiting application."), "Application did not quit properly");
    }
}


