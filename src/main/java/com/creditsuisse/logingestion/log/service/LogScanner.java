package com.creditsuisse.logingestion.log.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
public class LogScanner {
  private final ApplicationEventPublisher eventPublisher;

  public LogScanner(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  public void read(String path) {
    try {
      Files.lines(Paths.get(path))
        .parallel()
        .filter(StringUtils::hasText)
        .forEach(eventPublisher::publishEvent);
    } catch (IOException e) {
      log.error("Error occurred during read, path: " + path, e);
    }
  }
}
