package batch;

import flat.PersonReaderFactory;
import json.PersonJsonWriterFactory;
import model.PersonData;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

// tag::code[]
@SpringBootApplication
@EnableBatchProcessing
public class CopyApp {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    public static void main(String[] args) {
        SpringApplication.run(CopyApp.class, args);
    }

    @Bean
    Job createCopyJob() {
// end::code[]
        ItemStreamReader<PersonData> reader = PersonReaderFactory
                .createPersonCsvReader(new ClassPathResource("Persons.csv"));
        ItemStreamWriter<PersonData> writer = PersonJsonWriterFactory
                .createPersonJsonWriter(new FileSystemResource("target/Persons.json"));
        TaskletStep step = stepBuilderFactory
                .get("copyStep")
                .<PersonData, PersonData>chunk(1)
                .reader(reader)
                .writer(writer)
                .build();
        return jobBuilderFactory.get("copyJob").start(step).build();
// tag::code[]
        // ...
    }
}
// end::code[]
