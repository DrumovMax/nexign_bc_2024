package com.nexign.bootcamp.emulator.repository;

import com.nexign.bootcamp.emulator.model.Subscriber;
import org.springframework.data.repository.ListCrudRepository;

public interface SubscriberRepository extends ListCrudRepository<Subscriber, Long> {
}
