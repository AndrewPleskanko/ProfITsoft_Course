package org.example.io.interfaces;

import java.io.IOException;

import org.example.model.Statistics;

public interface FileWriter {
    void write(Statistics statistics, String attributeName) throws IOException;
}
