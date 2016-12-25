# EsriShapefile

Java library to read an [ESRI Shapefile](https://www.esri.com/library/whitepapers/pdfs/shapefile.pdf)

## Usage

### Read all of the records in a shapefile

```java
final ShapefileReader shapefileReader = new ShapefileReader();

try {
    shapefileReader.forEachRecord("/path/to/shapefile", (recordHeader, shape) -> {
        System.out.println(recordHeader.getRecordNumber());
        System.out.println(shape.getShapeType().name());
    });
} catch (final ShapefileNotReadableException e) {
    // Something went wrong while attempting to read the shapefile.
} catch (final ShapefileConsumerException e) {
    // Something went wrong while attempting to consume the record.
}
```

### Read the main file header of a shapefile

```java
final ShapefileReader shapefileReader = new ShapefileReader();

try {
    final MainFileHeader mainFileHeader = shapefileReader.getMainFileHeader("/path/to/shapefile");
} catch (final ShapefileNotReadableException e) {
    // Something went wrong while attempting to read the shapefile.
}
```

## Contributing

1. Fork it ( https://github.com/karayusuf/esri-shapefile/fork )
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create a new Pull Request
