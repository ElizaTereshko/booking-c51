package com.example.bookingc51.repository;


import com.example.bookingc51.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    boolean existsByName(String name);
    Place findByName(String name);
    Place getById(long id);
    Optional<Place> getByName(String name);
}
