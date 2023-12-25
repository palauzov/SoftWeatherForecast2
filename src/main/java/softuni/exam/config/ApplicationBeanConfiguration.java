package softuni.exam.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter((Converter<String, LocalTime>) mappingContext ->
                LocalTime.parse(mappingContext.getSource(), DateTimeFormatter.ofPattern("HH::mm:ss")));
        return modelMapper;
    }

    @Bean
    public Gson gson(){
        return new GsonBuilder().setPrettyPrinting().create();
    }
}
