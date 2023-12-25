package softuni.exam.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.entity.DaysOfWeek;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "forecast")
@XmlAccessorType(XmlAccessType.FIELD)
public class ForecastDto {

    @NotNull
    @XmlElement(name = "day_of_week")
    private DaysOfWeek dayOfWeek;

    @NotNull
    @DecimalMin(value = "-20")
    @DecimalMax(value = "60")
    @XmlElement(name = "max_temperature")
    private double maxTemperature;

    @NotNull
    @DecimalMin(value = "-50")
    @DecimalMax(value = "40")
    @XmlElement(name = "min_temperature")
    private double minTemperature;

    @NotNull
    @XmlElement
    private String sunrise;

    @NotNull
    @XmlElement
    private String sunset;

    @NotNull
    @XmlElement
    private Long city;
}
