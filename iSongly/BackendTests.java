import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.List;

public class BackendTests {

    /**
     * This test method verifies if songs can be successfully loaded from a CSV file
     * and properly inserted into the tree. It checks if the tree contains the last song.
     */
    @Test
    public void roleTest1() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        try {
            backend.readData("songs.csv");
        } catch (Exception e) {
            Assertions.fail("Exception occurred while reading data from the file: " + e.getMessage());
        }

        Assertions.assertEquals(4, tree.size());
        Assertions.assertTrue(tree.contains(new Song("Kills You Slowly", null, null,
                0, 0, 0, 0, 0 ,0)));
    }


    /**
     * This test method checks if the getRange and setFilter methods work together
     * by filtering songs within a year range and applying a loudness filter.
     * It ensures that the resulting list is filtered correctly by both year and loudness.
     */
    @Test
    public void roleTest2() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        try {
            backend.readData("songs.csv");
        } catch (IOException e) {
            Assertions.fail("Exception occurred while reading data from the file: " + e.getMessage());
        }

        // Test getRange method
        List<String> getRange1 =  backend.getRange(2016, null);
        Assertions.assertEquals("[Cake By The Ocean, A L I E N S, Kills You Slowly]", getRange1.toString());

        // Test setFilter method (after using getRange method)
        List<String> setFilter = backend.setFilter(-6);
        Assertions.assertEquals("[Kills You Slowly]", setFilter.toString());

        // Test getRange method again (after using setFilter method)
        List<String> getRange2 = backend.getRange(2015, 2018);
        Assertions.assertEquals("[]", getRange2.toString());
    }


    /**
     * This test method checks if the fiveMost method correctly identifies the top 5 most danceable songs.
     * It ensures that the returned list contains at most 5 songs, sorted by danceability.
     */
    @Test
    public void roleTest3() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        try {
            backend.readData("songs.csv");
        } catch (IOException e) {
            Assertions.fail("Exception occurred while reading data from the file: " + e.getMessage());
        }

        // Call getRange method
        backend.getRange(null, 2018);

        // Test fiveMost method
        List<String> fiveMost =  backend.fiveMost();
        Assertions.assertEquals("[BO$$, Cake By The Ocean, A L I E N S]", fiveMost.toString());
    }
}