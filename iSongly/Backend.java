import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class Backend implements BackendInterface {

    private IterableSortedCollection<Song> tree;

    /**
     * Backend class handles the loading, filtering, and sorting of songs.
     * Songs are stored in a tree-like structure for efficient sorting and filtering.
     */
    public Backend(IterableSortedCollection<Song> tree) {
        this.tree = tree;
    }


    /**
     * Loads data from the .csv file referenced by filename.  You can rely
     * on the exact headers found in the provided songs.csv, but you should
     * not rely on them always being presented in this order or on there
     * not being additional columns describing other song qualities.
     * After reading songs from the file, the songs are inserted into
     * the tree passed to the constructor.
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
    @Override
    public void readData(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            String headerLine = reader.readLine();

            Map<String, Integer> headerMap = createHeaderMap(headerLine);

            String line;
            while ((line = reader.readLine()) != null) {

                String[] data = parseLine(line);

                String title = data[headerMap.get("title")];
                String artist = data[headerMap.get("artist")];
                String genres = data[headerMap.get("top genre")];
                int year = Integer.parseInt(data[headerMap.get("year")]);
                int bpm = Integer.parseInt(data[headerMap.get("bpm")]);
                int energy = Integer.parseInt(data[headerMap.get("nrgy")]);
                int danceability = Integer.parseInt(data[headerMap.get("dnce")]);
                int loudness = Integer.parseInt(data[headerMap.get("dB")]);
                int liveness = Integer.parseInt(data[headerMap.get("live")]);

                // Create a new Song object
                Song song = new Song(title, artist, genres, year, bpm, energy,
                        danceability, loudness, liveness);

                // Add the song to the tree
                tree.insert(song);

            }
        } catch (IOException e) {
        // Added a custom message when the IOException is thrown
        throw new IOException("Error reading file: " + filename + ". Please check if the file exists and is accessible.", e);
        }

    }

    // Helper method to create a Map from the header line
    private Map<String, Integer> createHeaderMap(String headerLine) {
        String[] headers = headerLine.split(",");
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            headerMap.put(headers[i], i);
        }
        return headerMap;
    }

    // Helper method to parse a CSV line into an array of fields
    private String[] parseLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            if (currentChar == '"') {
                if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentField.append('"');
                    i++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (currentChar == ',' && !insideQuotes) {
                result.add(currentField.toString());
                currentField.setLength(0);
            } else {
                currentField.append(currentChar);
            }
        }

        result.add(currentField.toString());

        return result.toArray(new String[0]);
    }


    private Integer yearLow = null;
    private Integer yearHigh = null;
    private Integer loudThreshold = null;

    /**
     * Retrieves a list of song titles from the tree passed to the constructor.
     * The songs should be ordered by the songs' Year, and that fall within
     * the specified range of Year values.  This Year range will
     * also be used by future calls to the setFilter and getmost Danceable methods.

     * If a Loudness filter has been set using the setFilter method
     * below, then only songs that pass that filter should be included in the
     * list of titles returned by this method.

     * When null is passed as either the low or high argument to this method,
     * that end of the range is understood to be unbounded.  For example, a null
     * high argument means that there is no maximum Year to include in
     * the returned list.

     * @param low is the minimum Year of songs in the returned list
     * @param high is the maximum Year of songs in the returned list
     * @return List of titles for all songs from low to high, or an empty
     *     list when no such songs have been loaded
     */
    @Override
    public List<String> getRange(Integer low, Integer high) {
        List<String> setFilter = setFilterHelper(loudThreshold); // Filter by loudness
        List<String> getRange = getRangeHelper(low, high); // Filter by year range

        getRange.retainAll(setFilter);  // Combine filters

        yearLow = low;
        yearHigh = high;

        return getRange;
    }

    // Helper method to retrieve songs within the specified year range
    private List<String> getRangeHelper(Integer low, Integer high) {
        Comparator<Song> yearComparator = Comparator.comparingInt(Song::getYear);

        // Set the minimum and maximum bounds for the iterator
        for (Song song : tree) {
            song = new Song(song.getTitle(), song.getArtist(), song.getGenres(), song.getYear(),
                    song.getBPM(), song.getEnergy(), song.getDanceability(), song.getLoudness(),
                    song.getLiveness(), yearComparator);
        }


        if (low == null) {
            tree.setIteratorMin(null);
        } else {
            tree.setIteratorMin(new Song(null, null, null, low,
                    0, 0, 0, 0, 0, yearComparator));
        }

        if (high == null) {
            tree.setIteratorMax(null);
        } else {
            tree.setIteratorMax(new Song(null, null, null, high,
                    0, 0, 0, 0, 0, yearComparator));
        }

        // Retrieve and sort the songs
        List<Song> getRangeSongs = new ArrayList<>();

        for (Song song : tree) {
            getRangeSongs.add(song);
        }

        getRangeSongs.sort(Comparator.comparingInt(Song::getYear));

        // Convert song list to title list
        List<String> getRange = new ArrayList<>();

        for (Song song : getRangeSongs) {
            getRange.add(song.getTitle());
        }

        return getRange;
    }


    /**
     * Retrieves a list of song titles that have a Loudness that is
     * smaller than the specified threshold.  Similar to the getRange
     * method: this list of song titles should be ordered by the songs'
     * Year, and should only include songs that fall within the specified
     * range of Year values that was established by the most recent call
     * to getRange.  If getRange has not previously been called, then no low
     * or high Year bound should be used.  The filter set by this method
     * will be used by future calls to the getRange and getmost Danceable methods.

     * When null is passed as the threshold to this method, then no Loudness
     * threshold should be used.  This effectively clears the filter.

     * @param threshold filters returned song titles to only include songs that
     *     have a Loudness that is smaller than this threshold.
     * @return List of titles for songs that meet this filter requirement, or
     *     an empty list when no such songs have been loaded
     */
    @Override
    public List<String> setFilter(Integer threshold) {
        List<String> getRange = getRangeHelper(yearLow, yearHigh);  // Get songs in year range
        List<String> setFilter = setFilterHelper(threshold);  // Get songs that match loudness filter

        setFilter.retainAll(getRange);  // Combine filters

        loudThreshold = threshold;  // Save loudness threshold

        return setFilter;
    }

    // Helper method to filter songs by loudness
    private List<String> setFilterHelper(Integer threshold) {
        Comparator<Song> loudComparator = Comparator.comparingInt(Song::getLoudness);

        for (Song song : tree) {
            song = new Song(song.getTitle(), song.getArtist(), song.getGenres(), song.getYear(),
                    song.getBPM(), song.getEnergy(), song.getDanceability(), song.getLoudness(),
                    song.getLiveness(), loudComparator);
        }


        if (threshold == null) {
            tree.setIteratorMax(null);  // No filter if threshold is null
        } else {
            tree.setIteratorMax(new Song(null, null, null, 0,
                    0, 0, 0, threshold, 0, loudComparator));
        }

        tree.setIteratorMin(null);  // Set min to null for loudness filtering


        List<Song> setFilterSongs = new ArrayList<>();

        for (Song song : tree) {
            setFilterSongs.add(song);
        }

        setFilterSongs.sort(Comparator.comparingInt(Song::getYear));  // Sort by year

        // Convert song list to title list
        List<String> setFilter = new ArrayList<>();

        for (Song song : setFilterSongs) {
            setFilter.add(song.getTitle());
        }

        return setFilter;
    }

    /**
     * This method returns a list of song titles representing the five
     * most Danceable songs that both fall within any attribute range specified
     * by the most recent call to getRange, and conform to any filter set by
     * the most recent call to setFilter.  The order of the song titles in this
     * returned list is up to you.

     * If fewer than five such songs exist, return all of them.  And return an
     * empty list when there are no such songs.
     *
     * @return List of five most Danceable song titles
     */
    public List<String> fiveMost() {
        Set<String> filteredSongSet = new HashSet<>(setFilter(loudThreshold));  // Apply loudness filter

        List<Song> danceMost = new ArrayList<>();

        List<String> fiveMost = new ArrayList<>();

        for (Song song : tree) {
            if (filteredSongSet.contains(song.getTitle())) {
                danceMost.add(song);
            }
        }

        danceMost.sort(Comparator.comparingInt(Song::getDanceability).reversed());  // Sort by danceability

        int num = 0;
        for (Song song : danceMost) {  // Return top 5 danceable songs
            fiveMost.add(song.getTitle());
            num++;
            if (num >= 5) {
                break;
            }
        }

        return fiveMost;
    }
}