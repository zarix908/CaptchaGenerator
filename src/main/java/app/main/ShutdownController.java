package app.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Timer;
import java.util.TimerTask;

@RestController
public class ShutdownController {
    private final ApplicationContext applicationContext;

    @Autowired
    public ShutdownController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = "/shutdown", method = RequestMethod.GET)
    public String shutdownService() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SpringApplication.exit(applicationContext);
            }
        }, 1000);

        System.out.println("Service was stopped.");
        return "{\"response:\":\"service was stopped\"}";
    }
}