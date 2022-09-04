package com.creditsuisse.logingestion.event.service;

import com.creditsuisse.logingestion.event.model.EventEntry;
import com.creditsuisse.logingestion.event.model.Type;
import com.creditsuisse.logingestion.log.model.ApplicationLogEntry;
import com.creditsuisse.logingestion.log.model.LogEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class EventConverterTest {
  private ConcurrentMap<String, LogEntry> map;
  private EventConverter converter;

  @BeforeEach
  public void setUp() {
    map = new ConcurrentHashMap<>();
    converter = new EventConverter(map);
  }

  @AfterEach
  public void tearDown() {
    map.clear();
  }

  @Test
  public void convertFirstLogEntry() {
    LogEntry entry = new LogEntry("id", "state", 1L);
    EventEntry eventEntry = converter.convert(entry);

    assertNull(eventEntry);
    assertSame(entry, map.get("id"));
  }

  @Test
  public void convertSecondLogEntryWithSameId() {
    LogEntry entry1 = new LogEntry("id", "state", 1L);
    LogEntry entry2 = new LogEntry("id", "state", 2L);
    EventEntry eventEntry1 = converter.convert(entry1);
    EventEntry eventEntry2 = converter.convert(entry2);

    assertNull(eventEntry1);
    assertNotNull(eventEntry2);
    assertEquals("id", eventEntry2.getId());
    assertNull(eventEntry2.getHost());
    assertEquals(1L, eventEntry2.getDuration());
    assertNull(eventEntry2.getType());

    assertSame(entry1, map.get("id"));
  }

  @Test
  public void convertFirstApplicationLogEntry() {
    LogEntry entry = new ApplicationLogEntry("id", "state", 1L, "type", "host");
    EventEntry eventEntry = converter.convert(entry);

    assertNull(eventEntry);
    assertSame(entry, map.get("id"));
  }

  @Test
  public void convertSecondApplicationLogEntryWithSameId() {
    LogEntry entry1 = new ApplicationLogEntry("id", "state", 1L, Type.APPLICATION_LOG.name(), "host");
    LogEntry entry2 = new ApplicationLogEntry("id", "state", 2L, Type.APPLICATION_LOG.name(), "host");
    EventEntry eventEntry1 = converter.convert(entry1);
    EventEntry eventEntry2 = converter.convert(entry2);

    assertNull(eventEntry1);
    assertNotNull(eventEntry2);
    assertEquals("id", eventEntry2.getId());
    assertEquals("host", eventEntry2.getHost());
    assertEquals(1L, eventEntry2.getDuration());
    assertEquals(Type.APPLICATION_LOG, eventEntry2.getType());

    assertSame(entry1, map.get("id"));
  }

  @Test
  public void convertRandomLogEntries() {
    Set<LogEntry> logEntries = new HashSet<>();

    for (int i = 0; i < 50; i++) {
      int id = i % 25;
      LogEntry entry = new LogEntry("lid" + id, i < 25 ? "STARTED" : "FINISHED", i % 4);
      logEntries.add(entry);
    }

    for (int i = 50; i < 100; i++) {
      int id = (i - 50) % 25;
      LogEntry entry = new ApplicationLogEntry("alid" + id, i < 75 ? "STARTED" : "FINISHED", i, Type.APPLICATION_LOG.name(), "host" + id);
      logEntries.add(entry);
    }

    Set<EventEntry> eventEntries = new HashSet<>();
    for (LogEntry logEntry : logEntries) {
      EventEntry convert = converter.convert(logEntry);
      if (convert == null) {
        continue;
      }
      eventEntries.add(convert);
    }

    assertEquals(50, eventEntries.size());
    assertEquals(25, eventEntries.stream().filter(eventEntry -> eventEntry.getDuration() > 4).collect(Collectors.toList()).size());
    assertEquals(25, eventEntries.stream().filter(EventEntry::isAlert).collect(Collectors.toList()).size());
    assertTrue(eventEntries.stream().filter(eventEntry -> eventEntry.getDuration() > 4).allMatch(EventEntry::isAlert));
    assertTrue(eventEntries.stream().filter(eventEntry -> StringUtils.hasText(eventEntry.getHost())).allMatch(eventEntry -> eventEntry.getType() == Type.APPLICATION_LOG));
    assertTrue(eventEntries.stream().filter(eventEntry -> eventEntry.getHost() == null).allMatch(eventEntry -> eventEntry.getType() == null));
  }
}