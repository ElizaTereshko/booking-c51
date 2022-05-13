package com.example.bookingc51.controller;


import com.example.bookingc51.entity.Place;
import com.example.bookingc51.entity.Reservation;
import com.example.bookingc51.service.PlaceService;
import com.example.bookingc51.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PlaceService placeService;

    @GetMapping( "/all")
    public ModelAndView getListReservationView(ModelAndView modelAndView){
        log.info("Try to get list with reservations");
        List<Reservation> allReservation= reservationService.getAllReservation();
        modelAndView.addObject("list", allReservation);
        log.info("Number of reservations: "+allReservation.size());
        modelAndView.setViewName("allReservations");
        return modelAndView;
    }

    @GetMapping()
    public ModelAndView getReservationView(ModelAndView modelAndView){
        log.info("Try to open page with a reservation form");
        modelAndView.addObject("reservation", new Reservation());
        modelAndView.setViewName("reservation");
        return modelAndView;
    }


    @PostMapping()
    public ModelAndView postReservationView(@Valid @ModelAttribute("reservation") Reservation reservation,
                                        BindingResult bindingResult, ModelAndView modelAndView, HttpSession httpSession,
                                        RedirectAttributes redirectAttributes){
        log.info("Client reservation: "+reservation.toString());
        if(!bindingResult.hasErrors()){
            log.info("No binding result errors");
            Place reservationsPlace = (Place) httpSession.getAttribute("reservation");
            reservation.setPlace(reservationsPlace);
            reservationService.save(reservation);
            modelAndView.addObject("reservation", new Reservation());
            httpSession.setAttribute("reservation", reservation);
            redirectAttributes.addFlashAttribute("result", "Your reservation was successful.");
            modelAndView.setViewName("redirect:/place/all");
        }else {
            log.info("Binding result has errors");
            modelAndView.setViewName("reservation");
        }
        return modelAndView;
    }

    @PostMapping(path = "remove/reservation/{id}")
    public ModelAndView removeReservation(@PathVariable("id") long id, ModelAndView modelAndView, HttpSession session,
                                   RedirectAttributes redirectAttributes){
        log.info("Try to remove reservation with id="+id);
        Reservation reservation = (Reservation) session.getAttribute("reservation");
        reservationService.deleteById(reservation.getId());
            log.info("Reservation with id="+id+" has been removed");
            redirectAttributes.addFlashAttribute("remove", "Reservation has been removed");
        modelAndView.setViewName("redirect:/allReservations");
        return modelAndView;
    }
}
