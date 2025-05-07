package repository;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import model.Weather;

import java.time.LocalDate;
import java.util.List;

@Dependent
public class WeatherRepository {

    @Inject
    private EntityManager em;

    @Transactional
    public Weather createWeather(Weather weather) { return em.merge(weather); }

    public boolean existsByCity(String cityName) {
        List<Weather> list = em.createNamedQuery(Weather.GET_WEATHER_FOR_CITY).setParameter("name", cityName).getResultList();
        return list.size() > 0;
    }
}
