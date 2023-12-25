package softuni.exam.models.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {

    @Size(min = 2, max = 60)
    @NonNull
    private String countryName;

    @NonNull
    @Size(min = 2, max = 20)
    private String currency;
}
