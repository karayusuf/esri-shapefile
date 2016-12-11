package esri.shapefile.models;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class RecordHeaderTest {

    @Test
    public void testBuildingARecordHeaderFromBytes() throws Exception {
        final ByteBuffer recordHeaderBytes = ByteBuffer.allocate(8);
        recordHeaderBytes.order(ByteOrder.BIG_ENDIAN);
        recordHeaderBytes.putInt(1);
        recordHeaderBytes.putInt(256);

        final RecordHeader recordHeader = RecordHeader.fromBytes(recordHeaderBytes.array());
        assertEquals(1, recordHeader.getRecordNumber());
        assertEquals(256, recordHeader.getContentLength());
    }
}