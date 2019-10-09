package xml;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.Test;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import model.PersonData;
import xml.jaxb.Person;

public class StaxJaxbWritingTest {
    private XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
    private XMLEventFactory eventFactory = XMLEventFactory.newInstance();
    private SAXTransformerFactory transformerFactory = (SAXTransformerFactory) TransformerFactory.newInstance();

    private List<Person> persons = List.of(new PersonData("Anna", "Gramm", LocalDate.of(1972, Month.JULY, 3)),
            new PersonData("Izmir", "Egal", LocalDate.of(1972, Month.JULY, 3))).stream().map(this::asPersonType).collect(Collectors.toList());

    private Person asPersonType(PersonData person) {
        Person personElement = new Person();
        personElement.setFirstName(person.getFirstName());
        personElement.setLastName(person.getLastName());
        return personElement;
    }

    @Test
    public void testPlainStaX() throws XMLStreamException {
        XMLEventWriter writer = outputFactory.createXMLEventWriter(System.out);
        writer.add(eventFactory.createStartDocument());
        writer.add(eventFactory.createStartElement("", "", "persons"));
        writer.add(eventFactory.createStartElement("", "", "person"));
        writer.add(eventFactory.createStartElement("", "", "firstName"));
        writer.add(eventFactory.createCharacters("Anna"));
        writer.add(eventFactory.createEndElement("", "", "firstName"));
        writer.add(eventFactory.createEndElement("", "", "person"));
        writer.add(eventFactory.createEndElement("", "", "persons"));
        writer.flush();
        System.out.println();
    }

    @Test
    public void testMarshallToStream() throws Exception {
        Result result = new StreamResult(System.out);
        marshallTo(result);
        System.out.println();
    }

    @Test
    public void testMarshallToFormattingTransformer() throws Exception {
        TransformerHandler transformerHandler = transformerFactory.newTransformerHandler();
        transformerHandler.setResult(new StreamResult(System.out));
        transformerHandler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
        SAXResult result = new SAXResult(transformerHandler);
        marshallTo(result);
        System.out.println();
    }

    private void marshallTo(Result result) throws Exception, IOException {
        Marshaller marshaller = getJaxb2Marshaller();
        for (Person person : persons) {
            marshaller.marshal(person, result);
        }
    }

    private Jaxb2Marshaller getJaxb2Marshaller() throws Exception {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(Person.class.getPackage().getName());
        marshaller.setMarshallerProperties(Collections.singletonMap(
                javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE));
        marshaller.afterPropertiesSet();
        return marshaller;
    }

}
