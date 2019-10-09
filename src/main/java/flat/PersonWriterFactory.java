package flat;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.core.io.Resource;

import model.PersonData;

public final class PersonWriterFactory {

    private PersonWriterFactory() {
    }

    public static FlatFileItemWriter<PersonData> createPersonToStringWriter(Resource sink) {
        return new FlatFileItemWriterBuilder<PersonData>()
                .name("personToStringWriter")
                .resource(sink)
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }

    // tag::writer[]
    public static FlatFileItemWriter<PersonData> createPersonWriter(Resource sink) {
        var fieldExtractor = new BeanWrapperFieldExtractor<PersonData>();
        fieldExtractor.setNames(new String[] { "lastName", "firstName", "birthday" });
        fieldExtractor.afterPropertiesSet();

        var lineAggregator = new FormatterLineAggregator<PersonData>();
        lineAggregator.setFormat("%1$-20s%2$-20s %3$td.%3$tm.%3$tY");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<PersonData>()
                .name("personFixedColumnsWriter")
                .resource(sink)
                .lineAggregator(lineAggregator)
                .build();
    }
    // end::writer[]

    public static FlatFileItemWriter<PersonData> createPersonWriterFluent(Resource sink) {
        var fieldExtractor = new BeanWrapperFieldExtractor<PersonData>();
        fieldExtractor.setNames(new String[] { "lastName", "firstName", "birthday" });
        fieldExtractor.afterPropertiesSet();

        return new FlatFileItemWriterBuilder<PersonData>()
                .name("personFixedColumnsWriter")
                .resource(sink)
                .formatted()
                .format("%1$-20s%2$-20s %3$td.%3$tm.%3$tY")
                .fieldExtractor(fieldExtractor)
                .build();
    }

}
