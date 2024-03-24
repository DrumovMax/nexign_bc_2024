package com.nexign.bootcamp.emulator.service;

import com.nexign.bootcamp.emulator.model.Subscriber;
import com.nexign.bootcamp.emulator.repository.SubscriberRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Service for managing subscriber data and initialization.
 */
@Service
public class SubscriberService {

    /**
     * Repository for managing subscriber entities.
     */
    @Resource
    private SubscriberRepository subscriberRepository;

    /**
     * Generates a random phone number in format (starting with "79").
     *
     * @return randomly generated phone number
     */
    private Long generatePhoneNumber () {
        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder("79");

        for (int i = 2; i < 11; i++) {
            phoneNumber.append(random.nextInt(10));
        }

        return Long.parseLong(phoneNumber.toString());
    }

    /**
     * Initializes subscriber data by generating and saving 10 subscriber entities.
     */
    public void initSubs () {
        for (int i = 0; i < 10; i++) {
            Long newPhoneNumber = generatePhoneNumber();
            Subscriber sub = new Subscriber();
            sub.setPhoneNumber(newPhoneNumber);
            subscriberRepository.save(sub);
        }
    }
}
