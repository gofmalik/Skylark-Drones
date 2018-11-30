import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadPOIFromFile {

    private HashMap<String, double[]> POI;
    private String key = "";

    ReadPOIFromFile() {
        POI = new HashMap<>();
    }

    HashMap<String, double[]> processCSVFile() throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"));
        List<Path> paths;
        try (Stream<Path> files = Files.list(path)) {
            paths = files.filter(f -> f.endsWith("assets.csv")).collect(Collectors.toList());
        }
        try (Stream<String> lines = Files.lines(paths.get(0))) {
            lines.forEachOrdered(this::processPOI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return POI;
    }

    private void processPOI(String line) {
        String[] POIAndCoordinates = line.split(",");
        if (!POIAndCoordinates[2].toLowerCase().equals("latitude")) {
            double[] coordinates = {Double.parseDouble(POIAndCoordinates[1]), Double.parseDouble(POIAndCoordinates[2])};
            POI.put(POIAndCoordinates[0], coordinates);
        }
    }

}
