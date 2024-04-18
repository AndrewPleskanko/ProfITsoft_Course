package org.example.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.example.model.Item;
import org.example.model.Statistics;
import org.junit.jupiter.api.Test;


public class StatisticXmlFileWriterImplTest {
    private static final String ATTRIBUTE_NAME = "role";
    private static final String OUTPUT_FILE_NAME = "statistics_by_" + ATTRIBUTE_NAME + ".xml";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";
    private static final int ADMIN_COUNT = 1;
    private static final int USER_COUNT = 2;

    @Test
    public void write_whenStatisticsProvided_shouldCreateOutputFile() throws Exception {
        // Given
        List<Item> items = Arrays.asList(
                new Item(ADMIN_ROLE, ADMIN_COUNT),
                new Item(USER_ROLE, USER_COUNT)
        );
        Statistics statistics = new Statistics(items);
        StatisticXmlFileWriterImpl xmlStatisticXmlFileWriterImpl = new StatisticXmlFileWriterImpl();

        // When
        xmlStatisticXmlFileWriterImpl.write(statistics, ATTRIBUTE_NAME);

        // Then
        File outputFile = new File(OUTPUT_FILE_NAME);
        assertTrue(outputFile.exists());
    }
}
