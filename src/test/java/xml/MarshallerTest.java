package xml;

import java.util.Collections;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import xml.jaxb.ObjectFactory;
import xml.jaxb.PersonType;
import xml.jaxb.Persons;

public class MarshallerTest {

    @Test
    void testReadWrite() throws Exception {
        Marshaller marshaller = getJaxb2Marshaller();
        Unmarshaller unmarshaller = getJaxb2Marshaller();

        Object persons = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("Persons.xml").getInputStream()));
        System.out.println("persons = " + persons);
        marshaller.marshal(persons, new StreamResult(System.out));
    }

    @Test
    void testCreate() throws Exception {
        Marshaller marshaller = getJaxb2Marshaller();
        ObjectFactory objectFactory = new ObjectFactory();
        Persons persons = objectFactory.createPersons();
        PersonType person = objectFactory.createPersonType();
        person.setFirstName("Hugo");
        person.setLastName("Blub");
        persons.getPerson().add(person);
        marshaller.marshal(persons, new StreamResult(System.out));
    }

    private Jaxb2Marshaller getJaxb2Marshaller() throws Exception {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(Persons.class.getPackage().getName());
        marshaller.setMarshallerProperties(Collections.singletonMap(
                javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE));
        marshaller.afterPropertiesSet();
        return marshaller;
    }

}
