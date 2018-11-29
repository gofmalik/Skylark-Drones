import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import javaxt.io.Image;

class ImageLocations {
    private HashMap<String, double[]> imageLocations;

    ImageLocations() {
        imageLocations = new HashMap<>();
    }

    HashMap<String, double[]> getCoordinates() {
        Path path = Paths.get(System.getProperty("user.dir"));
        List<Path> paths = new ArrayList<>();
        try (Stream<Path> files = Files.list(Paths.get(path.getParent()+"/images"))) {
            files.forEachOrdered(f -> paths.add((f)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        paths.forEach(p -> {
            Image img = new Image(p.toString());
            double[] gps = img.getGPSCoordinate();
            if (gps != null) {
                imageLocations.put(p.getFileName().toString(), gps);
            }
        });
        return imageLocations;
    }
}
