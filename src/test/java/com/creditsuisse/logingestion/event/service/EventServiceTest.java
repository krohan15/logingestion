package com.creditsuisse.logingestion.event.service;

import com.creditsuisse.logingestion.event.model.EventEntry;
import com.creditsuisse.logingestion.log.model.LogEntry;
import com.creditsuisse.logingestion.log.service.LogScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
  @Mock
  private EventRepository repository;
  @Mock
  private LogScanner logScanner;

  private EventService eventService;

  @BeforeEach
  public void setUp() {
    eventService = new EventService(repository, logScanner);
  }

  @Test
  public void parseEvents() {
    String somePath = "somePath";
    eventService.parseEvents(somePath);

    ArgumentCaptor<String> pathCaptor = ArgumentCaptor.forClass(String.class);
    verify(logScanner, times(1)).read(pathCaptor.capture());
    String capturedEvent = pathCaptor.getValue();
    assertSame(somePath, capturedEvent);
  }

  @Test
  public void saveEvent() {
    EventEntry entry = new EventEntry(new LogEntry("id", "state", 1L), 1L);
    eventService.saveEvent(entry);
    ArgumentCaptor<EventEntry> pathCaptor = ArgumentCaptor.forClass(EventEntry.class);
    verify(repository, times(1)).save(pathCaptor.capture());
    EventEntry capturedEvent = pathCaptor.getValue();
    assertSame(entry, capturedEvent);
  }
}