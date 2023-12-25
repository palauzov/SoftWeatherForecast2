package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Forecast extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DaysOfWeek dayOfWeek;

    @DecimalMin(value = "-20")
    @DecimalMax(value = "60")
    @Column(nullable = false)
    private double maxTemperature;

    @DecimalMin(value = "-50")
    @DecimalMax(value = "40")
    @Column(nullable = false)
    private double minTemperature;

    @Column(nullable = false)
    private LocalTime sunset;

    @Column(nullable = false)
    private LocalTime sunrise;

    @ManyToOne
    private City city;


}
