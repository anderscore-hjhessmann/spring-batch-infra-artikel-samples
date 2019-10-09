package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.batch.item.json.GsonJsonObjectReader;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.core.io.Resource;

import model.PersonData;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class PersonJsonReaderFactory {

    private PersonJsonReaderFactory() {
    }

    public static JsonItemReader<PersonData> createPersonJsonReader(Resource source) {
        // tag::reader[]
        return new JsonItemReaderBuilder<PersonData>()
                .name("personJsonReader")
                .saveState(false)
                .jsonObjectReader(new JacksonJsonObjectReader<>(PersonData.class) {{
                    var om = new ObjectMapper();
                    om.registerModule(new JavaTimeModule());
                    setMapper(om);
                }})
                .resource(source)
                .build();
        // end::reader[]
    }

    public static JsonItemReader<PersonData> createPersonGsonReader(Resource source) {
        return new JsonItemReaderBuilder<PersonData>()
                .name("personJsonReader")
                .saveState(false)
                .jsonObjectReader(new GsonJsonObjectReader<>(PersonData.class) {{
                    setMapper(GsonFactory.createGson());
                }})
                .resource(source)
                .build();
    }
}
