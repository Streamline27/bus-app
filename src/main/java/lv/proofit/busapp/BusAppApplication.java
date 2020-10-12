package lv.proofit.busapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
public class BusAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusAppApplication.class, args);
    }
}
