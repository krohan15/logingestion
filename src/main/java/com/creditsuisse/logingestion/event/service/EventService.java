package com.creditsuisse.logingestion.event.service;

import com.creditsuisse.logingestion.event.model.EventEntry;
import com.creditsuisse.logingestion.log.service.LogScanner;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
@Service
public class EventService {
  private final EventRepository repository;
  private final LogScanner logScanner;

  public EventService(EventRepository repository, LogScanner logScanner) {
    this.repository = repository;
    this.logScanner = logScanner;
  }

  public void parseEvents(String path) {
    logScanner.read(path);
  }

  @EventListener
  public void saveEvent(EventEntry entry) {
    repository.save(entry);
  }
}
