package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountryDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CountryServiceImpl implements CountryService {


    private CountryRepository countryRepository;
    private ModelMapper modelMapper;
    private Gson gson;
    private ValidationUtil validationUtil;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper,
                              Gson gson, ValidationUtil validationUtil) {

        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }


    @Override
    public boolean areImported() {
        return countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        String file = "src/main/resources/files/json/countries.json";

        return Files.readString(Path.of(file)) ;
    }

    @Override
    public String importCountries() throws IOException {


        StringBuilder sb = new StringBuilder();

        List<Country> countries = Arrays.stream(gson.fromJson(readCountriesFromFile(), CountryDto[].class))
                .filter(countryDto -> {
                    boolean isValid = validationUtil.isValid(countryDto);

                    if (countryRepository.findByCountryName(countryDto.getCountryName()).isPresent()){
                        isValid = false;
                    }

                    if (isValid) {

                        sb.append(String.format("Successfully imported country %s - %s%n",
                                countryDto.getCountryName(), countryDto.getCurrency() ));
                        countryRepository.saveAndFlush(modelMapper.map(countryDto, Country.class));
                    } else {
                        sb.append("Invalid country").append(System.lineSeparator());
                    }


                    return isValid;
                }).map(countryDto -> modelMapper.map(countryDto, Country.class))
                .collect(Collectors.toList());


        return sb.toString();
    }
}
