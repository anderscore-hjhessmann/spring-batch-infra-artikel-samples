package xml;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.core.io.ClassPathResource;

import flat.ItemStreamHelper;
import model.PersonData;
import xml.jaxb.PersonType;

import javax.xml.datatype.XMLGregorianCalendar;

public class PersonXmlReaderTest {

    @Test
    void testReadPersonsXml() throws IOException {
        ItemStreamReader<PersonType> reader = PersonXmlReaderFactory.createPersonXmlReader(new ClassPathResource("Persons.xml"));

        List<PersonType> persons = ItemStreamHelper.read(reader);
        assertThat(persons).hasSize(2);
        assertThat(persons.stream().map(this::asPerson)).containsExactly(
                new PersonData("Anna", "Gramm", LocalDate.of(1967, Month.NOVEMBER, 9)),
                new PersonData("Izmir", "Egal", LocalDate.of(2012, Month.APRIL, 27)));
    }

    private PersonData asPerson(PersonType personType) {
        XMLGregorianCalendar bd = personType.getBirthday();
        return new PersonData(personType.getFirstName(), personType.getLastName(),
                LocalDate.of(bd.getYear(), bd.getMonth(), bd.getDay()));
    }

}
