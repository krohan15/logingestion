package com.creditsuisse.logingestion.event.service;

import com.creditsuisse.logingestion.event.model.EventEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<EventEntry, String> {
}
