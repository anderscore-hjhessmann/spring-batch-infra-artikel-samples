package xml;

import java.util.Collections;

import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import xml.jaxb.Person;

public final class PersonXmlWriterFactory {
    private static Marshaller marshaller;

    private PersonXmlWriterFactory() {
    }

    public static StaxEventItemWriter<Person> createPersonXmlWriter(Resource sink) {
        // tag::writer[]
        return new StaxEventItemWriterBuilder<Person>()
                .name("personXmlWriter")
                .saveState(false)
                .marshaller(getMarshaller())
                .resource(sink)
                .rootTagName("persons") // root-tag of the file
                .overwriteOutput(true) // overwrite existing file
                .build();
        // end::writer[]
    }

    private static Marshaller getMarshaller() {
        if (marshaller == null) {
            marshaller = createJaxb2Marshaller();
        }
        return marshaller;
    }

    private static Jaxb2Marshaller createJaxb2Marshaller() {
        // tag::marshaller[]
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(Person.class.getPackage().getName());
        marshaller.setMarshallerProperties(Collections.singletonMap( // will be ignored
                javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE));
        try {
            marshaller.afterPropertiesSet();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to initialize JAXB2 marshaller");
        }
        // end::marshaller[]
        return marshaller;
    }

}
