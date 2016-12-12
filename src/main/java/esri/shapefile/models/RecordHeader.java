package esri.shapefile.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The header for each record stores the record number and content length
 * for the record. Record headers have a fixed length of 8 bytes. The following
 * table shows the fields in the file header with their byte position, value,
 * type, and byte order. In the table, position is with respect to the start
 * of the record.
 *
 * Description of Record Headers
 *
 * Position  Field           Value          Type    Order
 * --------  -----           -----          ----    -----
 * Byte 0    Record Number   Record Number  Integer Big
 * Byte 4    Content Length  Content Length Integer Big
 */
public class RecordHeader {

    public static RecordHeader fromBytes(final byte[] bytes) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        final RecordHeader recordHeader = new RecordHeader();

        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        recordHeader.recordNumber = byteBuffer.getInt(0);
        recordHeader.contentLength = byteBuffer.getInt(4);

        return recordHeader;
    }

    private RecordHeader() {}

    /**
     * Record numbers begin at 1.
     */
    private int recordNumber;

    /**
     * The content length for a record is the length of the record contents
     * section measured in 16-bit words. Each record, therefore, contributes
     * (4 + content length) 16-bit words toward the total length of the file,
     * as stored at Byte 24 in the file header.
     */
    private int contentLength;

    public int getRecordNumber() {
        return recordNumber;
    }

    public int getContentLength() {
        return contentLength;
    }

    public int getContentLengthBytes() {
        return contentLength * 2;
    }
}
