package esri.shapefile.models.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A point consists of a pair of double-precision coordinates in the order X,Y.
 *
 * Position Field      Value Type    Number Byte Order
 * -------- -----      ----- ----    ------ ----------
 * Byte 0   Shape Type 1     Integer 1      Little
 * Byte 4   X          X     Double  1      Little
 * Byte 12  Y          Y     Double  1      Little
 */
public class Point implements Shape {

    public static Point fromBytes(final byte[] bytes) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        final Point point = new Point();
        point.shapeType = byteBuffer.getInt(0);
        point.x = byteBuffer.getDouble(4);
        point.y = byteBuffer.getDouble(12);

        return point;
    }

    public static Point at(final double x, final double y) {
        final Point point = new Point();
        point.shapeType = 1;
        point.x = x;
        point.y = y;

        return point;
    }

    private Point() {}
    private int shapeType;
    private double x;
    private double y;

    public int getShapeType() {
        return shapeType;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof Point) {
            final Point otherPoint = (Point) other;
            return otherPoint.getShapeType() == this.getShapeType() &&
                   otherPoint.getX() == this.getX() &&
                   otherPoint.getY() == this.getY();

        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 31 * String.format("%d%d%d", shapeType, x, y).hashCode();
    }
}
