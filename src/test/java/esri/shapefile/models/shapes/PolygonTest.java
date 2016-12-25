package esri.shapefile.models.shapes;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PolygonTest {

    @Test
    public void testBuildingFromBytes() throws Exception {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(132);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(5);      // Shape Type (Polygon)
        byteBuffer.putDouble(1.1); // Min X
        byteBuffer.putDouble(9.8); // Min Y
        byteBuffer.putDouble(1.2); // Max X
        byteBuffer.putDouble(9.9); // Max Y

        byteBuffer.putInt(1); // Number of Parts
        byteBuffer.putInt(5); // Number of Points

        // Parts
        byteBuffer.putInt(0);

        // Points
        byteBuffer.putDouble(0);
        byteBuffer.putDouble(0);

        byteBuffer.putDouble(2);
        byteBuffer.putDouble(2);

        byteBuffer.putDouble(4);
        byteBuffer.putDouble(0);

        byteBuffer.putDouble(2);
        byteBuffer.putDouble(-2);

        byteBuffer.putDouble(0);
        byteBuffer.putDouble(0);

        byteBuffer.rewind();
        final Polygon polygon = Polygon.fromBytes(byteBuffer);
        assertEquals(ShapeType.Polygon, polygon.getShapeType());

        assertEquals(1.1, polygon.getBoundingBox().getXMin(), 0.001);
        assertEquals(9.8, polygon.getBoundingBox().getYMin(), 0.001);
        assertEquals(1.2, polygon.getBoundingBox().getXMax(), 0.001);
        assertEquals(9.9, polygon.getBoundingBox().getYMax(), 0.001);

        assertEquals(1, polygon.getNumParts());
        assertEquals(5, polygon.getNumPoints());

        assertEquals(Arrays.asList(0), polygon.getParts());
        assertEquals(Arrays.asList(
            Point.at(0, 0),
            Point.at(2, 2),
            Point.at(4, 0),
            Point.at(2,-2),
            Point.at(0, 0)
        ), polygon.getPoints());
    }
}