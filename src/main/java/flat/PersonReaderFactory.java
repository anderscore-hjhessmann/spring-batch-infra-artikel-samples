package flat;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import model.PersonData;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.validation.DataBinder;

public final class PersonReaderFactory {

    private PersonReaderFactory() {
    }

    public static List<PersonData> readPersonsCsv(File file) throws IOException {
        return readPersonsCsv(new FileSystemResource(file));
    }

    public static List<PersonData> readPersonsCsv(Resource source) throws IOException {
        return ItemStreamHelper.read(createPersonCsvReader(source));
    }

    // tag::reader[]
    public static FlatFileItemReader<PersonData> createPersonCsvReader(Resource source) {
        return new FlatFileItemReaderBuilder<PersonData>()
                .name("personCsvReader") // arbitrary name
                .saveState(false) // don't save progress in ExecutionContext
                .resource(source) // read from this Resource
                .delimited() // expect a delimited (CSV) file
                .delimiter(";") // use ';' as delimiter instead of ','
                .quoteCharacter('\"') // remove quotation from content
                .names(new String[] { "lastName", "firstName", "birthday" }) // map column to names
                .fieldSetMapper(new BeanWrapperFieldSetMapper<PersonData>() {
                    {
                        setTargetType(PersonData.class); // initialize mapper
                        var cv = new DefaultFormattingConversionService();
                        setConversionService(cv); // convert date/time values
                    }
                })
                .linesToSkip(1) // skip first (header) row
                .build(); // create the reader
    }
    // end::reader[]

    // tag::conversionService[]
    private static ConversionService createDateFormattingConversionService() {
        var conversionService = new DefaultFormattingConversionService(false);
        var registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        registrar.registerFormatters(conversionService);
        return conversionService;
    }
    // end::conversionService[]
}
