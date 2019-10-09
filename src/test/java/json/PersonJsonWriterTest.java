package json;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.util.JsonExpectationsHelper;
import org.springframework.util.StreamUtils;

import flat.ItemStreamHelper;
import model.PersonData;

public class PersonJsonWriterTest {
    private List<PersonData> persons = List.of(
            new PersonData("Franz", "Brantwein", LocalDate.of(1958, Month.DECEMBER, 17)),
            new PersonData("Anna", "Bolika", LocalDate.of(1972, Month.JULY, 3)),
            new PersonData("Jim", "Panse", LocalDate.of(2002, Month.FEBRUARY, 8)));

    private Resource sink = new FileSystemResource("target/Persons.json");

    @Test
    void testWriterPersonToJsonUsingJackson() throws Exception {
        JsonFileItemWriter<PersonData> writer = PersonJsonWriterFactory.createPersonJsonWriter(sink);
        ItemStreamHelper.write(writer, persons);
        JsonAssertions.assertJsonContentEquals(new ClassPathResource("Persons.json"), sink);
    }

    @Test
    void testWriterPersonToJsonUsingGson() throws Exception {
        JsonFileItemWriter<PersonData> writer = PersonJsonWriterFactory.createPersonGsonWriter(sink);
        ItemStreamHelper.write(writer, persons);
        JsonAssertions.assertJsonContentEquals(new ClassPathResource("Persons.json"), sink);
    }
}
