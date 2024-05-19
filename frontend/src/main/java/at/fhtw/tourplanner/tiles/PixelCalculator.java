package at.fhtw.tourplanner.tiles;

public class PixelCalculator {
    public record Point(int x, int y) {}

    public static Point latLonToPixel(double lat, double lon, int zoom) {
        double lat_rad = Math.toRadians(lat);
        double n = Math.pow(2.0, zoom);
        int x_pixel = (int) Math.floor((lon + 180.0) / 360.0 * n * 256);
        int y_pixel = (int) Math.floor((1.0 - Math.log(Math.tan(lat_rad) + 1 / Math.cos(lat_rad)) / Math.PI) / 2.0 * n * 256);

        return new Point(x_pixel, y_pixel);
    }

    public static void main(String[] args) {
        if (args.length<5) {
            System.out.println("Syntax: PixelCalculator tileX tileY lat lon zoom");
            System.out.println("Sample: PixelCalculator 71498 45431 16.377229 48.239676 17");
            System.exit(1);
        }
        int tileX = Integer.parseInt(args[0]);
        int tileY = Integer.parseInt(args[1]);
        double lon = Double.parseDouble(args[2]);
        double lat = Double.parseDouble(args[3]);
        int zoom = Integer.parseInt(args[4]);

        Point globalPos = latLonToPixel(lat, lon, zoom);
        Point tileOriginPos = new Point( tileX * 256, tileY * 256 );
        Point relativePos = new Point(globalPos.x - tileOriginPos.x, globalPos.y - tileOriginPos.y );
        System.out.println("Pixel coordinate within tile: " + relativePos);
    }
}
