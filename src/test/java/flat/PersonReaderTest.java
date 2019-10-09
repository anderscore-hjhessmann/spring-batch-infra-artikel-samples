package flat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import model.PersonData;

public class PersonReaderTest {

    @Test
    void testReadPersonsCsv() throws IOException {
        List<PersonData> persons = PersonReaderFactory.readPersonsCsv(new ClassPathResource("Persons.csv"));
        assertThat(persons).hasSize(3)
                .containsExactly(new PersonData("Franz", "Brantwein", LocalDate.of(1958, Month.DECEMBER, 17)),
                        new PersonData("Anna", "Bolika", LocalDate.of(1972, Month.JULY, 3)),
                        new PersonData("Jim", "Panse", LocalDate.of(2002, Month.FEBRUARY, 8)));
    }

    @Test
    void testFileDoesNotExist() {
        File file = new File("target/NotThere.csv");
        assertThat(file).doesNotExist();
        assertThrows(IOException.class, () -> PersonReaderFactory.readPersonsCsv(file));
    }
}
