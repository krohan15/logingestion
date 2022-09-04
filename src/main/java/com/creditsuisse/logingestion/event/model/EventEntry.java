package com.creditsuisse.logingestion.event.model;

import com.creditsuisse.logingestion.log.model.ApplicationLogEntry;
import com.creditsuisse.logingestion.log.model.LogEntry;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class EventEntry {
  @Id
  private String id;
  private long duration;
  @Enumerated(value = EnumType.STRING)
  private Type type;
  private String host;
  private boolean alert;

  public EventEntry(LogEntry entry, long duration) {
    this.id = entry.getId();
    this.duration = duration;
    this.alert = duration > 4;

    boolean isApplicationLogEntry = entry instanceof ApplicationLogEntry;
    this.type = isApplicationLogEntry ? Type.valueOf(((ApplicationLogEntry) entry).getType()) : null;
    this.host = isApplicationLogEntry ? ((ApplicationLogEntry) entry).getHost() : null;
  }
}
