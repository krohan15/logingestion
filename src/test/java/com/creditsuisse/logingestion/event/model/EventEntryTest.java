package com.creditsuisse.logingestion.event.model;



import com.creditsuisse.logingestion.log.model.ApplicationLogEntry;
import com.creditsuisse.logingestion.log.model.LogEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventEntryTest {

  @Test
  public void createEventEntry() {
    EventEntry entry = new EventEntry(new LogEntry("id", "state", 1L), 3);
    assertEquals("id", entry.getId());
    assertEquals(3, entry.getDuration());
    assertNull(entry.getType());
    assertNull(entry.getHost());
    assertFalse(entry.isAlert());

    entry = new EventEntry(new LogEntry("id", "state", 1L), 5);
    assertEquals("id", entry.getId());
    assertEquals(5, entry.getDuration());
    assertNull(entry.getType());
    assertNull(entry.getHost());
    assertTrue(entry.isAlert());

    entry = new EventEntry(new ApplicationLogEntry("id", "state", 1L, Type.APPLICATION_LOG.name(), "123"), 3);
    assertEquals("id", entry.getId());
    assertEquals(3, entry.getDuration());
    assertEquals(Type.APPLICATION_LOG, entry.getType());
    assertEquals("123", entry.getHost());
    assertFalse(entry.isAlert());

    entry = new EventEntry(new ApplicationLogEntry("id", "state", 1L, Type.APPLICATION_LOG.name(), "123"), 5);
    assertEquals("id", entry.getId());
    assertEquals(5, entry.getDuration());
    assertEquals(Type.APPLICATION_LOG, entry.getType());
    assertEquals("123", entry.getHost());
    assertTrue(entry.isAlert());
  }
}