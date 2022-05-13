package com.example.bookingc51.service;

import com.example.bookingc51.entity.Reservation;
import com.example.bookingc51.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation save(Reservation reservation) {
        log.info("Save client reservation: "+reservation);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservation() {
        log.info("Find all client reservations");
        return reservationRepository.findAll();
    }

    public Optional<Reservation> findById(long id) {
        log.info("Find client reservation by id(id = "+id+")");
        return reservationRepository.findById(id);
    }

    public void deleteById(long id) {
        log.info("Delete reservation by id(id = "+id+")");
        reservationRepository.deleteById(id);
    }
}
