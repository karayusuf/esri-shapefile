package esri.shapefile;

import esri.shapefile.models.MainFileHeader;
import esri.shapefile.models.RecordHeader;
import esri.shapefile.models.shapes.Null;
import esri.shapefile.models.shapes.Point;
import esri.shapefile.models.shapes.Polygon;
import esri.shapefile.models.shapes.Shape;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.BiConsumer;

public class ShapefileReader {

    private final int MAIN_FILE_HEADER_SIZE = 100;
    private final int RECORD_HEADER_SIZE = 8;

    public MainFileHeader getMainFileHeader(final String pathToShapefile) throws IOException {
        final File file = new File(pathToShapefile);
        final FileInputStream fileInputStream = new FileInputStream(file);

        final byte[] mainFileHeaderBytes = readBytes(fileInputStream, MAIN_FILE_HEADER_SIZE);
        return MainFileHeader.fromBytes(mainFileHeaderBytes);
    }

    public void forEachRecord(final String pathToShapefile,
                              final BiConsumer<RecordHeader, Shape> recordConsumer) throws IOException {
        final File file = new File(pathToShapefile);
        final FileInputStream fileInputStream = new FileInputStream(file);

        final byte[] mainFileHeaderBytes = readBytes(fileInputStream, MAIN_FILE_HEADER_SIZE);
        final MainFileHeader mainFileHeader = MainFileHeader.fromBytes(mainFileHeaderBytes);

        int offset = mainFileHeaderBytes.length;
        while (offset < mainFileHeader.getFileLengthBytes()) {
            final byte[] recordHeaderBytes = readBytes(fileInputStream, RECORD_HEADER_SIZE);
            final RecordHeader recordHeader = RecordHeader.fromBytes(recordHeaderBytes);
            offset += recordHeaderBytes.length;

            final byte[] shapeBytes = readBytes(fileInputStream, recordHeader.getContentLengthBytes());
            final Shape shape = buildShapeFromBytes(shapeBytes);
            offset += shapeBytes.length;

            recordConsumer.accept(recordHeader, shape);
        }
    }

    private Shape buildShapeFromBytes(final byte[] shapeBytes) {
        final ByteBuffer shapeByteBuffer = ByteBuffer.wrap(shapeBytes);
        shapeByteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        final int shapeType = shapeByteBuffer.getInt(0);
        shapeByteBuffer.rewind();

        switch (shapeType) {
            case 0: return Null.fromBytes(shapeByteBuffer);
            case 1: return Point.fromBytes(shapeByteBuffer);
            case 5: return Polygon.fromBytes(shapeByteBuffer);
            default: throw new RuntimeException("Unknown Shape Value: " + shapeType);
        }
    }

    private byte[] readBytes(final FileInputStream fileInputStream, final int numberOfBytes) throws IOException {
        final byte[] bytes = new byte[numberOfBytes];
        fileInputStream.read(bytes);
        return bytes;
    }

}
