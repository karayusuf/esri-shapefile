package esri.shapefile.models.shapes;

import esri.shapefile.models.BoundingBox;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A polygon consists of one or more rings. A ring is a connected sequence of four or more
 * points that form a closed, non-self-intersecting loop. A polygon may contain multiple
 * outer rings. The order of vertices or orientation for a ring indicates which side of the ring
 * is the interior of the polygon. The neighborhood to the right of an observer walking along
 * the ring in vertex order is the neighborhood inside the polygon. Vertices of rings defining
 * holes in polygons are in a counterclockwise direction. Vertices for a single, ringed
 * polygon are, therefore, always in clockwise order. The rings of a polygon are referred to
 * as its parts.
 *
 * Because this specification does not forbid consecutive points with identical coordinates,
 * shapefile readers must handle such cases. On the other hand, the degenerate, zero length
 * or zero area parts that might result are not allowed.
 *
 * The Polygon structure is identical to the PolyLine structure, as follows:
 *
 * Polygon
 * {
 *   Double[4] Box           // Bounding Box
 *   Integer NumParts        // Number of Parts
 *   Integer NumPoints       // Total Number of Points
 *   Integer[NumParts] Parts // Index to First Point in Part
 *   Point[NumPoints] Points // Points for All Parts
 * }
 *
 */
public class Polygon implements Shape {

    /**
     * Position Field       Value     Type    Number    Order
     * -------- -----       -----     ----    ------    -----
     * Byte 0   Shape Type  5         Integer 1         Little
     * Byte 4   Box         Box       Double  4         Little
     * Byte 36  NumParts    NumParts  Integer 1         Little
     * Byte 40  NumPoints   NumPoints Integer 1         Little
     * Byte 44  Parts       Parts     Integer NumParts  Little
     * Byte X   Points      Points    Point   NumPoints Little
     *
     * * Note: X = 44 + 4 * NumParts
     */
    public static Polygon fromBytes(final ByteBuffer byteBuffer) {
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        final Polygon polygon = new Polygon();
        polygon.shapeType = byteBuffer.getInt();
        polygon.boundingBox = BoundingBox.fromBytes(byteBuffer);

        polygon.numParts = byteBuffer.getInt();
        polygon.numPoints = byteBuffer.getInt();

        polygon.parts = new ArrayList<>(polygon.numParts);
        IntStream.range(0, polygon.numParts).forEach((ignored) -> {
            polygon.parts.add(byteBuffer.getInt());
        });

        polygon.points = new ArrayList<>(polygon.numPoints);
        IntStream.range(0, polygon.numPoints).forEach((ignored) -> {
            final double x = byteBuffer.getDouble();
            final double y = byteBuffer.getDouble();
            polygon.points.add(Point.at(x,y));
        });

        return polygon;
    }

    private Polygon() {}
    private int shapeType;

    /**
     * The Bounding Box for the polygon stored in the order Xmin, Ymin, Xmax, Ymax.
     */
    private BoundingBox boundingBox;

    /**
     * The number of rings in the polygon.
     */
    private int numParts;

    /**
     * The total number of points for all rings.
     */
    private int numPoints;

    /**
     * An array of length NumParts. Stores, for each ring, the index of its first
     * point in the points array. Array indexes are with respect to 0.
     */
    private List<Integer> parts;

    /**
     * An array of length NumPoints. The points for each ring in the polygon are
     * stored end to end. The points for Ring 2 follow the points for Ring 1, and so
     * on. The parts array holds the array index of the starting point for each ring.
     * There is no delimiter in the points array between rings.
     */
    private List<Point> points;

    @Override
    public ShapeType getShapeType() {
        return ShapeType.Polygon;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public int getNumParts() {
        return numParts;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public List<Integer> getParts() {
        return parts;
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<List<Point>> getRings() {
        final List<Integer> ringParts = new ArrayList(parts);
        ringParts.add(numPoints);

        final List<List<Point>> rings = new ArrayList<>(numParts);
        ringParts.stream().reduce((startIndex, endIndex) -> {
            final List<Point> ring = points.subList(startIndex, endIndex);
            rings.add(ring);

            return endIndex;
        });

        return rings;
    }
}
