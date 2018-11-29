import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ImagesAtKDist {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the drone distance from the image: ");
        double droneDist = scanner.nextDouble();
        System.out.println("Enter the POI distance from the image: ");
        double POIDist = scanner.nextDouble();

        // Objects to fetch drone locations during all videos throught their SRTs
        DroneLocations droneLocations = new DroneLocations();
        ImageLocations imageLocations = new ImageLocations();

        // Method call to fetch the drone locations
        List<HashMap<String, Set<double[]>>> droneLoc = droneLocations.processFile();

        // Method call to fetch image locations
        HashMap<String, double[]> imageLoc = imageLocations.getCoordinates();
        Set<String> images = new HashSet<>();
        int i = 1; // to make different files for diff videos.
        PrintWriter droneFile;
        PrintWriter kmlFile;

        // creating kml file object to retrieve the content that should go in the kml file.
        CreateKMLFile createKMLFile = new CreateKMLFile();

        for (HashMap<String, Set<double[]>> k : droneLoc) {
            // creating kml file
            kmlFile = new PrintWriter("kml" + i + ".kml");
            kmlFile.write(createKMLFile.generateKMLString(k));
            kmlFile.close();

            droneFile = new PrintWriter("DroneImagesVideo" + i++ +".csv");
            // For every second, each drone location
            for (Map.Entry<String, Set<double[]>> drone : k.entrySet()) {
                for (Map.Entry<String, double[]> image : imageLoc.entrySet()) {
                    double[] imageCoordinates = image.getValue();
                    Set<double[]> drones = drone.getValue();
                    // trying to match the user entered distance
                    for (double[] loc : drones) {
                        if (findDistance(loc[0], loc[1], imageCoordinates[0], imageCoordinates[1]) <= droneDist) {
                            images.add(image.getKey());
                            break;
                        }
                    }
                }
                String imageString = "";
                for (String s : images) {
                    imageString += s + ",";
                }
                droneFile.write(drone.getKey() + "," + imageString.substring(0, imageString.length() - 1) + "\n");
            }
            droneFile.close();
            images.clear();
        }


        ReadPOIFromFile POILocations = new ReadPOIFromFile();
        HashMap<String, double[]> POIImages = POILocations.processCSVFile();
        PrintWriter POIFile = new PrintWriter("POIImages.csv");
        for (Map.Entry<String, double[]> POI : POIImages.entrySet()) {
            for (Map.Entry<String, double[]> image : imageLoc.entrySet()) {
                double[] imageCoordinates = image.getValue();
                double[] POILoc = POI.getValue();
                if (findDistance(POILoc[0], POILoc[1], imageCoordinates[0], imageCoordinates[1]) <= POIDist) {
                    images.add(image.getKey());
                }
            }
            String imageString = "";
            for (String s : images) {
                imageString += s + ",";
            }
            POIFile.write(POI.getKey() + "," + imageString.substring(0, imageString.length() - 1) + "\n");
        }
        POIFile.close();
    }

    /**
     *
     * @param long1 longitude 1
     * @param lat1 latitude 1
     * @param long2 longitude 2
     * @param lat2 latitude 2
     * @return distance between two points mentioned through the latitudes and longitudes of 2 given points
     */
    private static double findDistance(double long1, double lat1, double long2, double lat2) {
        // Using haversine formula
        final int R = 6371; // Radius of the earth

        double latitudeDistance = Math.toRadians(lat2 - lat1);
        double longitudeDistance = Math.toRadians(long2 - long1);
        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000; // convert to meters
    }
}
