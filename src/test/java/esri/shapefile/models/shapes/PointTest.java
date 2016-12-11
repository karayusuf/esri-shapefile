package esri.shapefile.models.shapes;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class PointTest {

    @Test
    public void testBuildingFromBytes() throws Exception {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(1);
        byteBuffer.putDouble(2.2);
        byteBuffer.putDouble(3.3);

        final Point point = Point.fromBytes(byteBuffer.array());
        assertEquals(1,   point.getShapeType());
        assertEquals(2.2, point.getX(), 0.001);
        assertEquals(3.3, point.getY(), 0.001);
    }

    @Test
    public void testBuildingFromCoordinates() throws Exception {
        final Point point = Point.at(1.23, 4.56);
        assertEquals(1,    point.getShapeType());
        assertEquals(1.23, point.getX(), 0.001);
        assertEquals(4.56, point.getY(), 0.001);
    }

}