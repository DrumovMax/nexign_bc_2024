package com.nexign.bootcamp.emulator.repository;

import com.nexign.bootcamp.emulator.model.CDR;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CDRRepository extends ListCrudRepository<CDR, Long> {
}
