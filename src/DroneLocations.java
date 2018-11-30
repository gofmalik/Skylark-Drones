import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DroneLocations {

    private List<HashMap<String, Set<double[]>>> droneLocation;
    private String key = "";

    DroneLocations() {
        droneLocation = new ArrayList<>();
    }

    List<HashMap<String, Set<double[]>>> processFile() throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"));
        List<Path> paths;
        // retrieving files in the videos folder
        try (Stream<Path> files = Files.list(Paths.get(path+"/videos"))) {
            paths = files.filter(f -> f.toString().endsWith(".SRT")).collect(Collectors.toList());
        }
        for (Path p : paths) {
            HashMap <String, Set<double[]>> perVideo = new HashMap<>();
            // processing each file
            try (Stream<String> lines = Files.lines(p)) {
                lines.forEachOrdered(line -> processLine(line, perVideo));
            } catch (IOException e) {
                e.printStackTrace();
            }
            droneLocation.add(perVideo);
        }
        return droneLocation;
    }

    /**
     *
     * @param line current line in the current file
     * @param location HashMap to store drone locations of each video
     */
    private void processLine(String line, HashMap<String, Set<double[]>> location) {
        if (line.contains(",")) {
            if (line.contains(":")) {
                String[] timings = line.split(" --> ");
                //Assumption: drone is always moving. Hence, considering the first
                // timecode for the timing seems reasonable.
                key = timings[0].split(",")[0];
            }
            else{
                Set<double[]> locations = location.get(key);
                double[] gps = {Double.parseDouble(line.split(",")[0]), Double.parseDouble(line.split(",")[1])};
                if (location.get(key) == null) {
                    locations = new HashSet<>();
                }
                locations.add(gps);
                location.put(key, locations);
            }
        }
    }
}
