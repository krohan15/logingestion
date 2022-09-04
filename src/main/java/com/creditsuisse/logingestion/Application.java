package com.creditsuisse.logingestion;

import com.creditsuisse.logingestion.event.service.EventService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.creditsuisse.logingestion")
public class Application implements CommandLineRunner {
  private final EventService eventService;

  public Application(EventService eventService) {
    this.eventService = eventService;
  }

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);
    app.run(args);
  }

  @Override
  public void run(String... args) {
    if (args == null || args.length != 1) {
      throw new IllegalArgumentException("Given arguments is not valid. There has to be 1 argument.");
    }

    eventService.parseEvents(args[0]);
    System.exit(0);
  }
}
