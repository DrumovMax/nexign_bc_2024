package com.nexign.bootcamp.emulator.repository;

import com.nexign.bootcamp.emulator.model.Subscriber;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface SubscriberRepository extends ListCrudRepository<Subscriber, Long> {

    Optional<Subscriber> findByPhoneNumber (Long phoneNumber);
}
