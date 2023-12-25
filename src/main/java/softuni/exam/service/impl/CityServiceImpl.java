package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CityDto;
import softuni.exam.models.entity.City;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    private ModelMapper modelMapper;
    private Gson gson;
    private ValidationUtil validationUtil;
    private CityRepository cityRepository;
    private CountryRepository countryRepository;

    public CityServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil,
                           CityRepository cityRepository, CountryRepository countryRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }


    @Override
    public boolean areImported() {
        return cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/json/cities.json"));
    }

    @Override
    public String importCities() throws IOException {
        StringBuilder sb = new StringBuilder();

        List<City> cities = Arrays.stream(gson.fromJson(readCitiesFileContent(), CityDto[].class))
                .filter(cityDto -> {
                    boolean isValid = validationUtil.isValid(cityDto);

                    if (cityRepository.findByCityName(cityDto.getCityName()).isPresent()) {
                        isValid = false;
                    }
                    if (isValid) {
                        if (countryRepository.findById(cityDto.getCountry()).isPresent()) {
                            City cityToSave = modelMapper.map(cityDto, City.class);

                            cityToSave.setCountry(countryRepository.getById(cityDto.getCountry()));

                            sb.append(String.format("Successfully imported city %s - %d%n",
                                    cityDto.getCityName(), cityDto.getPopulation()));

                            cityRepository.saveAndFlush(cityToSave);
                        } else {
                            sb.append("Invalid city").append(System.lineSeparator());
                            isValid = false;
                        }
                    } else {
                        sb.append("Invalid city").append(System.lineSeparator());
                    }

                    return isValid;
                }).map(city -> modelMapper.map(city, City.class)).collect(Collectors.toList());


        return sb.toString();
    }
}
