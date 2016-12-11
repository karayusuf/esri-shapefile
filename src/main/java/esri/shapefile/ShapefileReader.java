package esri.shapefile;

import esri.shapefile.models.MainFileHeader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ShapefileReader {

    public static ShapefileReader forShapefile(final String shapefilePath) throws FileNotFoundException {
        final File file = new File(shapefilePath);

        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }

        return new ShapefileReader(file);
    }

    private final File shapefile;
    private final MainFileHeader mainFileHeader;

    private ShapefileReader(final File shapefile) {
        this.shapefile = shapefile;
        this.mainFileHeader = readMainFileHeader();
    }

    public MainFileHeader getMainFileHeader() {
        return mainFileHeader;
    }

    private MainFileHeader readMainFileHeader() {
        try {
            final byte[] mainFileHeaderBytes = new byte[100];
            final FileInputStream fileInputStream = new FileInputStream(shapefile);
            fileInputStream.read(mainFileHeaderBytes);
            return MainFileHeader.fromBytes(mainFileHeaderBytes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }




}
