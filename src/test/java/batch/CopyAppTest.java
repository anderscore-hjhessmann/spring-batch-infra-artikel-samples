package batch;

import json.JsonAssertions;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
public class CopyAppTest {

    @BeforeClass
    void cleanUp() throws IOException {
        Files.deleteIfExists(Path.of("target/Persons.json"));
    }

    @Test
    void test() throws Exception {
        // make sure the job has been run
        JsonAssertions.assertJsonContentEquals(new ClassPathResource("Persons.json"),
                new FileSystemResource("target/Persons.json"));
    }
}
