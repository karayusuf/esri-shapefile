package esri.shapefile.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The main file header is 100 bytes long. Table 1 shows the fields in the
 * file header with their byte position, value, type, and byte order. In the
 * table, position is with respect to the start of the file.
 *
 * Table 1.
 * Description of the Main File Header
 *
 * Position  Field        Value       Type    Byte Order
 * --------  -----        -----       ----    -----
 * Byte 0    File Code    9994        Integer Big
 * Byte 4    Unused       0           Integer Big
 * Byte 8    Unused       0           Integer Big
 * Byte 12   Unused       0           Integer Big
 * Byte 16   Unused       0           Integer Big
 * Byte 20   Unused       0           Integer Big
 * Byte 24   File Length  File Length Integer Big
 * Byte 28   Version      1000        Integer Little
 * Byte 32   Shape Type   Shape Type  Integer Little
 * Byte 36   Bounding Box Xmin        Double  Little
 * Byte 44   Bounding Box Ymin        Double  Little
 * Byte 52   Bounding Box Xmax        Double  Little
 * Byte 60   Bounding Box Ymax        Double  Little
 * Byte 68*  Bounding Box Zmin        Double  Little
 * Byte 76*  Bounding Box Zmax        Double  Little
 * Byte 84*  Bounding Box Mmin        Double  Little
 * Byte 92*  Bounding Box Mmax        Double  Little
 *
 * * Unused, with value 0.0, if not Measured or Z type
 */
public class MainFileHeader {

    public static MainFileHeader fromBytes(final byte[] bytes) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        final MainFileHeader mainFileHeader = new MainFileHeader();

        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        mainFileHeader.fileCode = byteBuffer.getInt(0);
        mainFileHeader.fileLength = byteBuffer.getInt(24);

        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        mainFileHeader.version = byteBuffer.getInt(28);
        mainFileHeader.shapeType = byteBuffer.getInt(32);

        mainFileHeader.xMin = byteBuffer.getDouble(36);
        mainFileHeader.yMin = byteBuffer.getDouble(44);
        mainFileHeader.xMax = byteBuffer.getDouble(52);
        mainFileHeader.yMax = byteBuffer.getDouble(60);

        mainFileHeader.zMin = byteBuffer.getDouble(68);
        mainFileHeader.zMax = byteBuffer.getDouble(76);
        mainFileHeader.mMin = byteBuffer.getDouble(84);
        mainFileHeader.mMax = byteBuffer.getDouble(92);

        return mainFileHeader;
    }

    private MainFileHeader() {}

    private int fileCode;
    private int fileLength;
    private int version;
    private int shapeType;

    private double xMin;
    private double yMin;
    private double xMax;
    private double yMax;

    private double zMin;
    private double zMax;
    private double mMin;
    private double mMax;

    public int getFileCode() {
        return fileCode;
    }

    public int getFileLength() {
        return fileLength;
    }

    public int getFileLengthBytes() {
        return fileLength * 2;
    }

    public int getVersion() {
        return version;
    }

    public int getShapeType() {
        return shapeType;
    }

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

    public double getZMin() {
        return zMin;
    }

    public double getZMax() {
        return zMax;
    }

    public double getMMin() {
        return mMin;
    }

    public double getMMax() {
        return mMax;
    }
}
