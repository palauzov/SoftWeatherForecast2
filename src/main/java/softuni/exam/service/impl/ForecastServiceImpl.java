package softuni.exam.service.impl;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.apache.tomcat.jni.File;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastDto;
import softuni.exam.models.dto.ForecastWrapperDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.DaysOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ForecastServiceImpl implements ForecastService {

    ForecastRepository forecastRepository;
     ModelMapper modelMapper;
    XmlParser xmlParser;
    Gson gson;
    ValidationUtil validationUtil;
    CityRepository cityRepository;


    @Override
    public boolean areImported() {
        return forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {

        return Files.readString(Path.of("src/main/resources/files/xml/forecasts.xml"));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        ForecastWrapperDto forecastWrappers = xmlParser.fromFile(
                Path.of("src/main/resources/files/xml/forecasts.xml").toFile(), ForecastWrapperDto.class);

        List<ForecastDto> forecastDtos = forecastWrappers.getForecastDtos();

        List<Forecast> collection = forecastDtos.stream().filter(forecastDto -> {
            boolean isValid = this.validationUtil.isValid(forecastDto);
            if (isValid) {

                if (cityRepository.findById(forecastDto.getCity()).isPresent()) {
                    sb.append(String.format("Successfully import forecast %s - %.2f%n",
                            forecastDto.getDayOfWeek(),
                            forecastDto.getMaxTemperature()));
                    City city = cityRepository.getById(forecastDto.getCity());
                    Forecast forecastToSave = modelMapper.map(forecastDto, Forecast.class);
                    forecastToSave.setCity(city);
                    forecastToSave.setSunrise(LocalTime.parse(forecastDto.getSunrise(),
                            DateTimeFormatter.ofPattern("HH:mm:ss")));

                    forecastToSave.setSunset(LocalTime.parse(forecastDto.getSunset(),
                            DateTimeFormatter.ofPattern("HH:mm:ss")));

                    forecastRepository.saveAndFlush(forecastToSave);
                } else {
                    sb.append("Invalid forecast").append(System.lineSeparator());
                }

            } else {
                sb.append("Invalid forecast").append(System.lineSeparator());
            }


            return isValid;
        }).map(forecastDto -> modelMapper.map(forecastDto, Forecast.class)).collect(Collectors.toList());

        return sb.toString();
    }

    @Override
    public String exportForecasts() {
        StringBuilder sb = new StringBuilder();
         List<Forecast> forecasts = forecastRepository.findAllByDayOfWeekAndCity_PopulationLessThanOrderByMaxTemperatureDescIdAsc
                 (DaysOfWeek.SUNDAY, 150000);

         for(Forecast f : forecasts) {
             sb.append(String.format("City: %s:%n" +
                             "-min temperature: %.2f%n" +
                             "--max temperature: %.2f%n" +
                             "---sunrise: %s%n" +
                             "----sunset: %s%n", f.getCity().getCityName(), f.getMinTemperature(),
                     f.getMaxTemperature(), f.getSunrise(), f.getSunset()));
         }
        return sb.toString();

    }
}
