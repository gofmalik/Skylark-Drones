import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CreateKMLFile {

    String generateKMLString(HashMap<String, Set<double[]>> droneLocations) {
        String kmlFileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                "<Document>\n";
        for(Map.Entry<String, Set<double[]>> drone : droneLocations.entrySet()) {
            Set<double[]> locations = drone.getValue();
            for(double[] coordinates : locations) {
                kmlFileContent += "<Placemark>\n" +
                        "    <Point>\n" +
                        "        <coordinates>" + coordinates[0] + "," + coordinates[1] + ",0</coordinates>\n" +
                        "    </Point>\n" +
                        "</Placemark>\n";
            }
        }
        kmlFileContent += "</Document>\n" +
                "</kml>";
        return kmlFileContent;
    }

}
