package flat;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.test.AssertFile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import model.PersonData;

public class PersonWriterTest {
    private List<PersonData> persons = List.of(
            new PersonData("Franz", "Brantwein", LocalDate.of(1958, Month.DECEMBER, 17)),
            new PersonData("Anna", "Bolika", LocalDate.of(1972, Month.JULY, 3)),
            new PersonData("Jim", "Panse", LocalDate.of(2002, Month.FEBRUARY, 8)));

    private Resource sink = new FileSystemResource("target/Persons.txt");

    @Test
    void testPersonToStringWriter() throws Exception {
        check(PersonWriterFactory.createPersonToStringWriter(sink), "PersonsToString.txt");
    }

    @Test
    void testPersonWriter() throws Exception {
        check(PersonWriterFactory.createPersonWriter(sink), "PersonsFixedColumns.txt");
    }

    @Test
    void testPersonWriterFluent() throws Exception {
        check(PersonWriterFactory.createPersonWriterFluent(sink), "PersonsFixedColumns.txt");
    }

    void check(ItemStreamWriter<PersonData> writer, String resultFile) throws Exception {
        ItemStreamHelper.write(writer, persons);
        AssertFile.assertFileEquals(new ClassPathResource(resultFile), sink);
    }
}
