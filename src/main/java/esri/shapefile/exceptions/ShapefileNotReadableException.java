package esri.shapefile.exceptions;

import java.io.File;
import java.io.IOException;

public class ShapefileNotReadableException extends IOException {

    public ShapefileNotReadableException(final String message) {
        super(message);
    }

    public ShapefileNotReadableException(final File file, final Throwable cause) {
        super(file.getAbsolutePath(), cause);
    }

}
