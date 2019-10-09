package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.batch.item.json.GsonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.core.io.Resource;

import model.PersonData;

public final class PersonJsonWriterFactory {

    private PersonJsonWriterFactory() {
    }

    public static JsonFileItemWriter<PersonData> createPersonJsonWriter(Resource sink) {
        // tag::writer[]
        return new JsonFileItemWriterBuilder<PersonData>()
                .name("personJsonWriter")
                .saveState(false)
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>() {{
                    var om = new ObjectMapper();
                    om.registerModule(new JavaTimeModule());
                    om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                    setObjectMapper(om);
                }})
                .resource(sink)
                .shouldDeleteIfExists(true)
                .build();
        // end::writer[]
    }

    public static JsonFileItemWriter<PersonData> createPersonGsonWriter(Resource sink) {
        return new JsonFileItemWriterBuilder<PersonData>()
                .name("personJsonWriter")
                .saveState(false)
                .jsonObjectMarshaller(new GsonJsonObjectMarshaller<>() {{
                    setGson(GsonFactory.createGson());
                }})
                .resource(sink)
                .shouldDeleteIfExists(true)
                .build();
    }
}
