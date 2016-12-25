package esri.shapefile.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BoundingBox {

    public static BoundingBox fromBytes(final ByteBuffer byteBuffer) {
        final BoundingBox boundingBox = new BoundingBox();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        boundingBox.xMin = byteBuffer.getDouble();
        boundingBox.yMin = byteBuffer.getDouble();
        boundingBox.xMax = byteBuffer.getDouble();
        boundingBox.yMax = byteBuffer.getDouble();

        return boundingBox;
    }

    private BoundingBox() {}

    private double xMin;
    private double yMin;
    private double xMax;
    private double yMax;

    public double getXMin() {
        return xMin;
    }

    public double getYMin() {
        return yMin;
    }

    public double getXMax() {
        return xMax;
    }

    public double getYMax() {
        return yMax;
    }
}
