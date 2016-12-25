/*package esri.shapefile.converters.svg;

import esri.shapefile.ShapefileReader;
import esri.shapefile.models.MainFileHeader;
import esri.shapefile.models.shapes.Polygon;

import java.io.*;
import java.util.stream.Collectors;

public class ShapefileToSvgConverter {

    public File convert(final String pathToShapefile) throws IOException {
        final File file = File.createTempFile("shapefile-to-svg-converter", ".html");
        final FileWriter fileWriter = new FileWriter(file);
        final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        final ShapefileReader shapefileReader = new ShapefileReader();
        final MainFileHeader mainFileHeader = shapefileReader.getMainFileHeader(pathToShapefile);
        writeHeader(bufferedWriter, mainFileHeader);

        shapefileReader.forEachRecord(pathToShapefile, ((recordHeader, shape) -> {
            switch (shape.getShapeType()) {
                case Polygon:
                    writePolygon(bufferedWriter, (Polygon) shape);
                    break;
            }

            try {
                if (recordHeader.getRecordNumber() % 4 == 0) {
                    bufferedWriter.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        bufferedWriter.write("</svg></body></html>");
        bufferedWriter.flush();
        return file;
    }

    private void writeHeader(final BufferedWriter bufferedWriter, final MainFileHeader mainFileHeader) throws IOException {
        bufferedWriter.write("<!DOCTYPE html>");
        bufferedWriter.append("<html>");
        bufferedWriter.append("<body>");

        final double width = mainFileHeader.getXMax() - mainFileHeader.getXMin();
        final double height = mainFileHeader.getYMax() - mainFileHeader.getYMin();

        bufferedWriter.append("<svg ");
        bufferedWriter.append(" height=\"100%\"");
        bufferedWriter.append(" width=\"100%\"");
        bufferedWriter.append(String.format(" viewbox=\"%.3f %.3f %.3f %.3f\"", mainFileHeader.getXMin(), -mainFileHeader.getYMax(), width, height));
        bufferedWriter.append(">");
    }

    private void writePolygon(final BufferedWriter bufferedWriter, final Polygon polygon) {
        try {
            bufferedWriter.write("<g>");

            polygon.getRings().forEach((points) -> {
                try {
                    bufferedWriter.write(points.stream().
                            map(point -> String.format("%.3f,%.3f", point.getX(), -point.getY())).
                            collect(Collectors.joining(" ", "<polygon points=\"", "\" />")));
                } catch (IOException e) {
                    // Super Annoying...
                }
            });

            bufferedWriter.write("</g>");
        } catch (IOException e) {
            // Super Annoting...
        }
    }

}*/
