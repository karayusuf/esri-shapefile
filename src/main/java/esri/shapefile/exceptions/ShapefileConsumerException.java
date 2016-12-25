package esri.shapefile.exceptions;

import esri.shapefile.models.RecordHeader;
import esri.shapefile.models.shapes.Shape;

public class ShapefileConsumerException extends Exception {

    public ShapefileConsumerException(final RecordHeader recordHeader, final Shape shape, final Throwable cause) {
        super(String.valueOf(recordHeader.getRecordNumber()), cause);
    }

}
