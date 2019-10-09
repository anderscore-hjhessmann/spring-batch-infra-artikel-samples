package xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.jupiter.api.Test;

import xml.jaxb.ObjectFactory;
import xml.jaxb.PersonType;
import xml.jaxb.Persons;

public class JaxbTest {

    @Test
    public void testReadWrite() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Persons.class.getPackage().getName());

        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object personen = unmarshaller.unmarshal(new File("src/test/resources/Persons.xml"));
        System.out.println("personen: " + personen);

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(personen, System.out);
    }

    @Test
    public void testCreateWrite() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Persons.class.getPackage().getName());

        ObjectFactory objectFactory = new ObjectFactory();
        Persons persons = objectFactory.createPersons();
        PersonType person = objectFactory.createPersonType();
        person.setFirstName("Hugo");
        person.setLastName("Blub");
        persons.getPerson().add(person);

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(persons, System.out);
    }

}
