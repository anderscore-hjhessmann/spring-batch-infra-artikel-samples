package xml;

import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import xml.jaxb.PersonType;
import xml.jaxb.Persons;

public final class PersonXmlReaderFactory {
    private static Unmarshaller unmarshaller;

    private PersonXmlReaderFactory() {
    }

    public static StaxEventItemReader<PersonType> createPersonXmlReader(Resource source) {
        // tag::reader[]
        return new StaxEventItemReaderBuilder<PersonType>()
                .name("personXmlReader")
                .saveState(false)
                .resource(source)
                .addFragmentRootElements("person")
                .unmarshaller(getUnmarshaller())
                .build();
        // end::reader[]
    }

    private static Unmarshaller getUnmarshaller() {
        if (unmarshaller == null) {
            unmarshaller = createJaxb2Marshaller();
        }
        return unmarshaller;
    }

    private static Jaxb2Marshaller createJaxb2Marshaller() {
        // tag::marshaller[]
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(Persons.class.getPackage().getName());
        marshaller.setMappedClass(PersonType.class);
        try {
            marshaller.afterPropertiesSet();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to initialize JAXB2 marshaller");
        }
        // end::marshaller[]
        return marshaller;
    }

}
