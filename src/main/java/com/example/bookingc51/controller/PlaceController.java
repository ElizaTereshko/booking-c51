package com.example.bookingc51.controller;

import com.example.bookingc51.DTO.PlaceAddDTO;
import com.example.bookingc51.DTO.PlaceEditDTO;
import com.example.bookingc51.entity.Place;
import com.example.bookingc51.service.PlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping(path = "/place")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @GetMapping("/{id}")
    public ModelAndView getPlaceView(@PathVariable("id") long id, ModelAndView modelAndView){
        log.info("Request to open place by id (id="+id+")");
        Optional<Place> byId = placeService.findById(id);
        if (byId.isPresent()) {
            log.info("Place with id="+id+" exists");
            Place place = byId.get();
            log.info(place.toString());
            modelAndView.addObject("place", place);
        }else {
            log.info("Place with id="+id+" doesn't exist");
            modelAndView.addObject("incorrectId", "Place with id="+id+" doesn't exist!");
        }
        modelAndView.setViewName("place");
        return modelAndView;
    }

    @GetMapping("/all")
    public ModelAndView getAllPlacesView(ModelAndView modelAndView){
        log.info("Request to get all places");
        List<Place> allPlaces = placeService.findAll();
        if (!allPlaces.isEmpty()) {
            log.info("List with places isn't empty");
            modelAndView.addObject("places", allPlaces);
        }else{
            log.info("List with places is empty");
            modelAndView.addObject("emptyList", "Place list is empty");
        }
        modelAndView.setViewName("allPlaces");
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView getPlaceAddView(ModelAndView modelAndView){
        log.info("Request to get add place page");
        modelAndView.addObject("placeAddForm", new PlaceAddDTO());
        modelAndView.setViewName("addPlace");
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView postPlaceAddView(@Valid @ModelAttribute("placeAddForm") PlaceAddDTO placeAddDTO,
                                         BindingResult bindingResult, ModelAndView modelAndView,
                                         RedirectAttributes redirectAttributes) {
        log.info("Try to add place");
        log.info("Add place: "+placeAddDTO.toString());
        if(!bindingResult.hasErrors()){
            log.info("No binding result errors");
            if(!placeService.existsByName(placeAddDTO.getName())){
                log.info("Correct place name");
                Place place = placeService.addDTOToEntity(placeAddDTO);
                Place save = placeService.save(place);
                redirectAttributes.addFlashAttribute("createdPlace", "Place '"+place.getName()+
                        "' was created!");
                log.info("Place "+save+" has been saved");
                modelAndView.setViewName("redirect:/place/add");
            }else {
                log.info("Place with name '"+placeAddDTO.getName()+" already exists");
                modelAndView.addObject("doesPlaceExist",true);
                modelAndView.setViewName("addPlace");
            }
        }else{
            log.info("Binding result has errors");
            modelAndView.setViewName("addPlace");
        }
        return modelAndView;
    }

    @GetMapping(path = "/edit")
    public ModelAndView editView(String nameOfEditableField, Long id, ModelAndView modelAndView){
        log.info("Request to get edit page");
        log.info("Edit parameters: name of editable field = "+nameOfEditableField+", id = "+id);
        if(nameOfEditableField!= null){
            log.info("Name of editable field != null");
            if(id != null && placeService.existsById(id)){
                log.info("id != null and place with this id exists");
                Optional<Place> optionalPlace = placeService.findById(id);
               optionalPlace.ifPresent(place -> modelAndView.addObject("place", place));
                modelAndView.addObject("placeForm", new PlaceEditDTO());
                modelAndView.addObject("nameOfEditableField", nameOfEditableField);
            }else {
                log.info("Input place id is incorrect");
                modelAndView.addObject("incorrectId", "Input id is incorrect!");
            }
        }else{
            log.info("Input editable field name is incorrect");
            modelAndView.addObject("incorrectField", "Input field is incorrect!");
        }
        modelAndView.setViewName("editPlace");
        return modelAndView;
    }

    @PostMapping(path = "/edit")
    public ModelAndView editPlace (long id, String nameOfEditableField, @Valid @ModelAttribute("placeForm") PlaceEditDTO placeEditDTO,
                                   BindingResult bindingResult, ModelAndView modelAndView) throws IOException {
        log.info("Try to edit place");
        log.info("Edit parameters: name of editable field = "+nameOfEditableField+", id = "+id);
        log.info("Edit model: "+placeEditDTO);
        if(!bindingResult.hasErrors()){
            log.info("No binding result errors");
            String name = placeEditDTO.getName();
            if(nameOfEditableField.equals("name") && placeService.existsByName(name)){
                log.info("Input name already exists");
                modelAndView.addObject("doesPlaceNameExist", true);
                modelAndView.setViewName("editPlace");
            }else {
                placeService.updateFieldById(id, nameOfEditableField, placeEditDTO);
                log.info("Update place field was success");
                modelAndView.setViewName("redirect:/place/"+ id);
            }
        }else{
            log.info("Binding result has errors");
            modelAndView.setViewName("editPlace");
        }
        if(modelAndView.getViewName().equals("editPlace")){
            Optional<Place> optionalPlace = placeService.findById(id);
            optionalPlace.ifPresent(place-> modelAndView.addObject("place", place));
            modelAndView.addObject("nameOfEditableField", nameOfEditableField);
            modelAndView.addObject("id", id);
        }
        return modelAndView;
    }
}
