package json;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.core.io.ClassPathResource;

import flat.ItemStreamHelper;
import model.PersonData;

public class PersonJsonReaderTest {

    @Test
    void testReadPersonsJsonUsingJackson() throws IOException {
        JsonItemReader<PersonData> reader = PersonJsonReaderFactory
                .createPersonJsonReader(new ClassPathResource("Persons.json"));

        checkReading(reader);
    }

    @Test
    void testReadPersonsJsonUsingGson() throws IOException {
        JsonItemReader<PersonData> reader = PersonJsonReaderFactory
                .createPersonGsonReader(new ClassPathResource("Persons.json"));

        checkReading(reader);
    }

    private void checkReading(JsonItemReader<PersonData> reader) throws IOException {
        List<PersonData> persons = ItemStreamHelper.read(reader);
        assertThat(persons)
                .hasSize(3)
                .containsExactly(
                        new PersonData("Franz", "Brantwein", LocalDate.of(1958, Month.DECEMBER, 17)),
                        new PersonData("Anna", "Bolika", LocalDate.of(1972, Month.JULY, 3)),
                        new PersonData("Jim", "Panse", LocalDate.of(2002, Month.FEBRUARY, 8)));
    }
}
