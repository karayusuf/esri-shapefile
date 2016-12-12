package esri.shapefile;

import esri.shapefile.models.MainFileHeader;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class ShapefileReaderTest {

    @Test
    public void testReadingMainFileHeader() throws Exception {
        final String shapefilePath = this.getClass().getResource("/fixtures/2004_us_election/elpo04p020.shp").getFile();
        final ShapefileReader shapefileReader = new ShapefileReader();
        final MainFileHeader mainFileHeader = shapefileReader.getMainFileHeader(shapefilePath);

        assertEquals(9994,     mainFileHeader.getFileCode());
        assertEquals(6435252,  mainFileHeader.getFileLength());
        assertEquals(1000,     mainFileHeader.getVersion());
        assertEquals(5,        mainFileHeader.getShapeType());

        assertEquals(-179.133, mainFileHeader.getXMin(), 0.001);
        assertEquals(  18.915, mainFileHeader.getYMin(), 0.001);
        assertEquals( 179.788, mainFileHeader.getXMax(), 0.001);
        assertEquals(  71.398, mainFileHeader.getYMax(), 0.001);

        assertEquals(0.0, mainFileHeader.getZMin(), 0.001);
        assertEquals(0.0, mainFileHeader.getZMax(), 0.001);
        assertEquals(0.0, mainFileHeader.getMMin(), 0.001);
        assertEquals(0.0, mainFileHeader.getMMax(), 0.001);
    }

    @Test
    public void testReadingPolygons() throws Exception {
        final String shapefilePath = this.getClass().getResource("/fixtures/2004_us_election/elpo04p020.shp").getFile();
        final ShapefileReader shapefileReader = new ShapefileReader();

        final AtomicInteger recordCount = new AtomicInteger(0);
        shapefileReader.forEachRecord(shapefilePath, (recordHeader, polygon) -> {
            recordCount.getAndAdd(1);
        });

        assertEquals(4755, recordCount.intValue());
    }

}