package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// tag::code[]
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonData {
    private String firstName;
    private String lastName;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;
}
// end::code[]
