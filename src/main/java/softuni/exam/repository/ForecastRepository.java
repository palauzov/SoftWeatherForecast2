package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.DaysOfWeek;
import softuni.exam.models.entity.Forecast;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {


    Optional<Forecast> findDayOfWeekByCityId(Long id);

    List<Forecast> findAllByDayOfWeekAndCity_PopulationLessThanOrderByMaxTemperatureDescIdAsc
            (DaysOfWeek dayOfWeek, int population);
}
