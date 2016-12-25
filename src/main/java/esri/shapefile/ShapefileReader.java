package esri.shapefile;

import esri.shapefile.exceptions.ShapefileConsumerException;
import esri.shapefile.exceptions.ShapefileNotReadableException;
import esri.shapefile.models.MainFileHeader;
import esri.shapefile.models.RecordHeader;
import esri.shapefile.models.shapes.Null;
import esri.shapefile.models.shapes.Point;
import esri.shapefile.models.shapes.Polygon;
import esri.shapefile.models.shapes.Shape;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * An ESRI Shapefile Reader.
 *
 * <p>
 * The main file (.shp) contains a fixed-length file header followed by variable-length
 * records. Each variable-length record is made up of a fixed-length record header followed
 * by variable-length record contents.
 * </p>
 *
 * <p>
 *   While the specification states that "all the non-Null shapes in a shapefile are required to be of the same shape type",
 *   this reader allows for different types of shapes. This is accomplished by using the `shapeType` value associated with
 *   each Record Contents to determine which shape to deserialize.
 * </p>
 *
 * <p>
 *   The following illustrates the main file organization.
 * </p>
 *
 * <pre>
 *+----------------------------------+
 *| Main File Header                 |
 *+---------------+------------------+--------+
 *| Record Header | Record Contents           |
 *+---------------+---------------------------+
 *| Record Header | Record Contents           |
 *+---------------+---------------------------+
 *| Record Header | Record Contents           |
 *+---------------+---------------------------+
 *| Record Header | Record Contents           |
 *+---------------+---------------------------+
 *| Record Header | Record Contents           |
 *+---------------+---------------------------+
 *| Record Header | Record Contents           |
 *+---------------+---------------------------+
 * </pre>
 */
public final class ShapefileReader {

    /**
     * The main file header is 100 bytes long.
     */
    private static final int MAIN_FILE_HEADER_SIZE = 100;

    /**
     * Record headers have a fixed length of 8 bytes.
     */
    private static final int RECORD_HEADER_SIZE = 8;

    /**
     * Interface that must be implemented in order to consume each RecordHeader and Shape contained within a shapefile.
     */
    public interface RecordConsumer {
        void consume(final RecordHeader recordHeader, final Shape shape) throws ShapefileConsumerException;
    }

    /**
     * This method reads the {@link MainFileHeader} of the shapefile located at the provided absolute filepath.
     *
     * <pre>
     * {@code
     *     final ShapefileReader shapefileReader = new ShapefileReader();
     *
     *     try {
     *         final MainFileHeader mainFileHeader = shapefileReader.getMainFileHeader("/path/to/shapefile");
     *         // Do something with the header...
     *     } catch (final ShapefileNotReadableException e) {
     *         // Handle the exception...
     *     }
     * }
     * </pre>
     *
     * @param pathToShapefile
     * @return {@link MainFileHeader}
     * @throws ShapefileNotReadableException
     */
    public MainFileHeader getMainFileHeader(final String pathToShapefile) throws ShapefileNotReadableException {
        final File file = new File(pathToShapefile);
        final FileInputStream fileInputStream = getInputStream(file);

        final byte[] mainFileHeaderBytes = readBytes(file, fileInputStream, MAIN_FILE_HEADER_SIZE);
        return MainFileHeader.fromBytes(mainFileHeaderBytes);
    }

    /**
     * Accepts a String containing an absolute path to a shapefile and a {@link RecordConsumer} and attempts to read
     * the shapefile at the given location and call the {@link RecordConsumer#consume(RecordHeader, Shape)} method
     * for each record in the shapefile.
     *
     * <pre>
     * {@code
     *   final ShapefileReader shapefileReader = new ShapefileReader();
     *
     *   try {
     *       shapefileReader.forEachRecord("/path/to/shapefile", (recordHeader, shape) -> {
     *           System.out.println(recordHeader.getRecordNumber());
     *           System.out.println(shape.getShapeType().name());
     *       });
     *   } catch (final ShapefileNotReadableException e) {
     *       // Something went wrong while attempting to read the shapefile.
     *   } catch (final ShapefileConsumerException e) {
     *       // Something went wrong while attempting to consume the record.
     *   }
     * }
     * </pre>
     *
     * @param pathToShapefile
     * @param recordConsumer
     * @throws ShapefileNotReadableException
     * @throws ShapefileConsumerException
     */
    public void forEachRecord(final String pathToShapefile, final RecordConsumer recordConsumer) throws ShapefileNotReadableException, ShapefileConsumerException {
        final File shapefile = new File(pathToShapefile);
        final FileInputStream fileInputStream = getInputStream(shapefile);

        final byte[] mainFileHeaderBytes = readBytes(shapefile, fileInputStream, MAIN_FILE_HEADER_SIZE);
        final MainFileHeader mainFileHeader = MainFileHeader.fromBytes(mainFileHeaderBytes);

        int offset = mainFileHeaderBytes.length;
        while (offset < mainFileHeader.getFileLengthBytes()) {
            final byte[] recordHeaderBytes = readBytes(shapefile, fileInputStream, RECORD_HEADER_SIZE);
            final RecordHeader recordHeader = RecordHeader.fromBytes(recordHeaderBytes);
            offset += recordHeaderBytes.length;

            final byte[] shapeBytes = readBytes(shapefile, fileInputStream, recordHeader.getContentLengthBytes());
            final Shape shape = buildShapeFromBytes(shapeBytes);
            offset += shapeBytes.length;

            recordConsumer.consume(recordHeader, shape);
        }
    }

    private Shape buildShapeFromBytes(final byte[] shapeBytes) throws ShapefileNotReadableException {
        final ByteBuffer shapeByteBuffer = ByteBuffer.wrap(shapeBytes);
        shapeByteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        final int shapeType = shapeByteBuffer.getInt(0);
        shapeByteBuffer.rewind();

        switch (shapeType) {
            case 0: return Null.fromBytes(shapeByteBuffer);
            case 1: return Point.fromBytes(shapeByteBuffer);
            case 5: return Polygon.fromBytes(shapeByteBuffer);
            default: throw new ShapefileNotReadableException("Unknown Shape Value: " + shapeType);
        }
    }

    private byte[] readBytes(final File shapefile, final FileInputStream fileInputStream, final int numberOfBytes) throws ShapefileNotReadableException {
        final byte[] bytes = new byte[numberOfBytes];

        try {
            fileInputStream.read(bytes);
        } catch (final IOException e) {
            throw new ShapefileNotReadableException(shapefile, e);
        }

        return bytes;
    }

    private FileInputStream getInputStream(final File shapefile) throws ShapefileNotReadableException {
        try {
            return new FileInputStream(shapefile);
        } catch (final FileNotFoundException fileNotFound) {
            throw new ShapefileNotReadableException(shapefile, fileNotFound);
        }
    }

}
