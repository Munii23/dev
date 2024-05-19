package at.fhtw.tourplanner.tiles;

public class TileCalculator {
    public record Tile(int x, int y) {}

    public static Tile latlon2Tile(double lat_deg, double lon_deg, int zoom) {
        double lat_rad = Math.toRadians(lat_deg);
        double n = Math.pow(2.0, zoom);
        int x_tile = (int) Math.floor((lon_deg + 180.0) / 360.0 * n);
        int y_tile = (int) Math.floor((1.0 - Math.log(Math.tan(lat_rad) + 1 / Math.cos(lat_rad)) / Math.PI) / 2.0 * n);
        return new Tile(x_tile, y_tile);
    }

    public static void main(String[] args) {
        if (args.length<5) {
            System.out.println("Syntax: TileCalculator bbox[1] bbox[2] bbox[3] bbox[4] zoom");
            System.out.println("Sample: TileCalculator 16.376941 48.239177 16.377488 48.240183 17");
            System.exit(1);
            return;
        }
        double bbox1 = Double.parseDouble(args[0]);
        double bbox2 = Double.parseDouble(args[1]);
        double bbox3 = Double.parseDouble(args[2]);
        double bbox4 = Double.parseDouble(args[3]);
        int zoom = Integer.parseInt(args[4]);

        Tile top_left = latlon2Tile(bbox4, bbox1, zoom);
        System.out.println("Top-left: " + top_left);
        Tile bottom_right = latlon2Tile(bbox2, bbox3, zoom);
        System.out.println("Bottom-right: " + bottom_right);

        for( int x = top_left.x; x <= bottom_right.x; x++)
            for( int y = top_left.y; y <= bottom_right.y; y++)
                System.out.printf("curl https://tile.openstreetmap.org/%d/%d/%d.png -o %d_%d_%d.png\n",
                        zoom, x, y, zoom, x, y);
    }
}

