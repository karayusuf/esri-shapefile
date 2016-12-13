package esri.shapefile.models.shapes;

import java.nio.ByteBuffer;

/**
 * A shape type of 0 indicates a null shape, with no geometric data for the shape. Each
 * feature type (point, line, polygon, etc.) supports nullsit is valid to have points and null
 * points in the same shapefile. Often null shapes are place holders; they are used during
 * shapefile creation and are populated with geometric data soon after they are created.
 *
 * Position  Field       Value  Type     Number  Byte Order
 * --------  -----       -----  ----     ------  ----------
 * Byte 0    Shape Type  0      Integer  1       Little
 *
 */
public class Null implements Shape {

    public static Null fromBytes(final ByteBuffer byteBuffer) {
        return immutableNullType;
    }

    public static Null fromBytes(final byte[] bytes) {
        return immutableNullType;
    }

    private static Null immutableNullType = new Null();

    private Null() {}

    @Override
    public ShapeType getShapeType() {
        return ShapeType.NullShape;
    }


}
