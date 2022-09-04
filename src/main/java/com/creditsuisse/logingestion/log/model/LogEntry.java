package com.creditsuisse.logingestion.log.model;

import lombok.Data;

@Data
public class LogEntry {
  private String id;
  private String state;
  private long timestamp;

  LogEntry() {
    this.id = null;
    this.state = null;
    this.timestamp = 0L;
  }

  public LogEntry(String id, String state, long timestamp) {
    this.id = id;
    this.state = state;
    this.timestamp = timestamp;
  }
}
