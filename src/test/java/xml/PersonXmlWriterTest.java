package xml;

import flat.ItemStreamHelper;
import model.PersonData;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.util.XmlExpectationsHelper;
import org.springframework.util.StreamUtils;
import xml.jaxb.Person;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

public class PersonXmlWriterTest {
    private List<Person> persons = List.of(
            new PersonData("Anna", "Gramm", LocalDate.of(1967, Month.NOVEMBER, 9)),
            new PersonData("Izmir", "Egal", LocalDate.of(2012, Month.APRIL, 27)))
            .stream().map(this::asPersonType).collect(Collectors.toList());

    private Resource sink = new FileSystemResource("target/Persons.xml");

    private Person asPersonType(PersonData person) {
        Person personElement = new Person();
        personElement.setFirstName(person.getFirstName());
        personElement.setLastName(person.getLastName());
        LocalDate bd = person.getBirthday();
        var cal = DatatypeFactory.newDefaultInstance()
                .newXMLGregorianCalendarDate(bd.getYear(), bd.getMonthValue(), bd.getDayOfMonth(), 0);
        personElement.setBirthday(cal);
        return personElement;
    }

    @Test
    void testWriterPersonToXml() throws Exception {
        StaxEventItemWriter<Person> writer = PersonXmlWriterFactory.createPersonXmlWriter(sink);
        ItemStreamHelper.write(writer, persons);
        assertXmlContentEquals(new ClassPathResource("Persons.xml"), sink);
    }

    private void assertXmlContentEquals(Resource expected, Resource actual) throws Exception {
        new XmlExpectationsHelper().assertXmlEqual(
                asString(expected),
                asString(actual));
    }

    private String asString(Resource resource) throws IOException {
        return StreamUtils.copyToString(resource.getInputStream(), Charset.forName("UTF-8"));
    }
}
