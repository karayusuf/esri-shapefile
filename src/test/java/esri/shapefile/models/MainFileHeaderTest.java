package esri.shapefile.models;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;

public class MainFileHeaderTest {

    @Test
    public void testBuildingMainFileHeaderFromBytes() throws Exception {
        final ByteBuffer mainFileHeaderBytes = ByteBuffer.allocate(100);
        mainFileHeaderBytes.order(ByteOrder.BIG_ENDIAN);
        mainFileHeaderBytes.putInt(9994);
        mainFileHeaderBytes.putInt(0);
        mainFileHeaderBytes.putInt(0);
        mainFileHeaderBytes.putInt(0);
        mainFileHeaderBytes.putInt(0);
        mainFileHeaderBytes.putInt(0);
        mainFileHeaderBytes.putInt(100);

        mainFileHeaderBytes.order(ByteOrder.LITTLE_ENDIAN);
        mainFileHeaderBytes.putInt(1000);
        mainFileHeaderBytes.putInt(1);

        mainFileHeaderBytes.putDouble(1.1);
        mainFileHeaderBytes.putDouble(2.1);
        mainFileHeaderBytes.putDouble(1.2);
        mainFileHeaderBytes.putDouble(2.2);

        mainFileHeaderBytes.putDouble(3.1);
        mainFileHeaderBytes.putDouble(3.2);
        mainFileHeaderBytes.putDouble(4.1);
        mainFileHeaderBytes.putDouble(4.2);

        final MainFileHeader mainFileHeader = MainFileHeader.fromBytes(mainFileHeaderBytes.array());
        assertEquals(9994, mainFileHeader.getFileCode());
        assertEquals(100,  mainFileHeader.getFileLength());
        assertEquals(1000, mainFileHeader.getVersion());
        assertEquals(1,    mainFileHeader.getShapeType());

        assertEquals(1.1,  mainFileHeader.getXMin(), 0.001);
        assertEquals(2.1,  mainFileHeader.getYMin(), 0.001);
        assertEquals(1.2,  mainFileHeader.getXMax(), 0.001);
        assertEquals(2.2,  mainFileHeader.getYMax(), 0.001);

        assertEquals(3.1,  mainFileHeader.getZMin(), 0.001);
        assertEquals(3.2,  mainFileHeader.getZMax(), 0.001);
        assertEquals(4.1,  mainFileHeader.getMMin(), 0.001);
        assertEquals(4.2,  mainFileHeader.getMMax(), 0.001);
    }

}