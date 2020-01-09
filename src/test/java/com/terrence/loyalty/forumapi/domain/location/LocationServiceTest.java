package com.terrence.loyalty.forumapi.domain.location;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationServiceTest {

    @InjectMocks
    private LocationService locationService;

    @Test
    public void testTemperatureConversion() {
        Assertions.assertEquals(2.0, locationService.fahrenheitToCelsius(35));
    }
}
