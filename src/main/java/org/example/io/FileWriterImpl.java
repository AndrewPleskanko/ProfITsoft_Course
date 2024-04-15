package org.example.io;

import java.io.File;
import java.io.IOException;

import org.example.io.interfaces.FileWriter;
import org.example.model.Statistics;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible for writing a Statistics object to an XML file.
 */
@Slf4j
public class FileWriterImpl implements FileWriter {
    private final XmlMapper xmlMapper = new XmlMapper();

    /**
     * Writes the specified Statistics object to an XML file.
     *
     * @param statistics    the Statistics object to write
     * @param attributeName the attribute name to use for the XML file name
     * @throws IOException if an error occurs during writing
     */
    @Override
    public void write(Statistics statistics, String attributeName) throws IOException {
        File outputFile = new File("statistics_by_" + attributeName + ".xml");
        log.info("Writing statistics to XML file: {}", outputFile.getName());
        xmlMapper.writeValue(outputFile, statistics);
        log.info("Finished writing statistics to XML file: {}", outputFile.getName());
    }
}
