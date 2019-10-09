package batch;

import static org.assertj.core.api.Assertions.assertThat;

import flat.PersonReaderFactory;
import json.JsonAssertions;
import json.PersonJsonWriterFactory;
import model.PersonData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

public class ManualBatchTest {
    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private JobLauncher jobLauncher;

    @BeforeEach
    void prepareInfrastructure() throws Exception {
        // tag::infra[]
        jobRepository = new MapJobRepositoryFactoryBean().getObject();
        transactionManager = new ResourcelessTransactionManager();
        jobBuilderFactory = new JobBuilderFactory(jobRepository);
        stepBuilderFactory = new StepBuilderFactory(jobRepository, transactionManager);
        // end::infra[]
        jobLauncher = createJobLauncher();
    }

    private JobLauncher createJobLauncher() throws Exception {
        // tag::launcher[]
        var launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        launcher.afterPropertiesSet();
        // end::launcher[]
        return launcher;
    }

    @Test
    public void testCopyJob() throws Exception {
        // tag::run[]
        // create the job
        Job job = createCopyJob();

        // launch the job
        JobExecution execution = jobLauncher.run(job, new JobParameters());

        // make sure job finished successfully
        assertThat(execution.getAllFailureExceptions()).isEmpty();
        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        // end::run[]

        // check content of copied file
        JsonAssertions.assertJsonContentEquals(new ClassPathResource("Persons.json"),
                new FileSystemResource("target/Persons.json"));
    }

    // tag::job[]
    private Job createCopyJob() {
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
    }
    // end::job[]
}
