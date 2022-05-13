package com.example.bookingc51.service;

import com.example.bookingc51.DTO.PlaceAddDTO;
import com.example.bookingc51.DTO.PlaceEditDTO;
import com.example.bookingc51.entity.Place;
import com.example.bookingc51.repository.PlaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PlaceService {
    @Autowired
    private PlaceRepository placeRepository;

    public boolean existsByName(String placeName){
        boolean result = placeRepository.existsByName(placeName);
        log.info("Place exist by place name ("+placeName+"): "+result);
        return result;
    }

    public Place save(Place place) {
        log.info("Save place: "+place);
        return placeRepository.save(place);
    }

    public Place findByName(String name) {
        log.info("Find place by name(name = "+name+")");
        return placeRepository.findByName(name);
    }

    public Place getById(long id) {
        log.info("Find place by id(id = "+id+")");
        return placeRepository.getById(id);
    }

    public boolean existsById(long id){
        log.info("Exist place by id(id = "+id+")");
        return placeRepository.existsById(id);
    }

    public Optional<Place> findById(long id){
        log.info("Find place by id(id = "+id+")");
        return placeRepository.findById(id);
    }

    public List<Place> findAll() {
        log.info("Find all places");
        return placeRepository.findAll();
    }

    public void updateFieldById(long id, String fieldName, PlaceEditDTO placeEditDTO) throws IOException {
        log.info("Update place field("+fieldName+") by id(id = "+id+")");
        log.info("Place update model: "+placeEditDTO.toString());
        Place place = placeRepository.getById(id);
        switch (fieldName) {
            case "name":
                String formName = placeEditDTO.getName();
                if(!formName.isEmpty()){
                    place.setName(formName);
                    log.info("Field 'name' is set");
                }
                break;
            case "address":
                String formAddress = placeEditDTO.getAddress();
                if(!formAddress.isEmpty()){
                    place.setAddress(formAddress);
                    log.info("Field 'country' is set");
                }
                break;
            case "workTime":
                String formWorkTime = placeEditDTO.getWorkTime();
                log.info(formWorkTime);
                if(!formWorkTime.isEmpty()){
                    place.setWorkTime(formWorkTime);
                    log.info("Field 'workTime' is set");
                }
                break;
            case "tableCount":
                String formTableCount = placeEditDTO.getTableCount();
                log.info(formTableCount);
                if(!formTableCount.isEmpty()){
                    place.setTableCount(Integer.parseInt(formTableCount));
                    log.info("Field 'tableCount' is set");
                }
                break;
            case "description":
                place.setDescription(placeEditDTO.getDescription());
                log.info("Field 'description' is set");
                break;
        }
        placeRepository.save(place);
    }

    public Place addDTOToEntity(PlaceAddDTO placeAddDTO){
        Place place = new Place();
        place.setName(placeAddDTO.getName());
        place.setAddress(placeAddDTO.getAddress());
        place.setWorkTime(placeAddDTO.getWorkTime());
        place.setTableCount(Integer.parseInt(placeAddDTO.getTableCount()));
        place.setDescription(placeAddDTO.getDescription());
        return place;
    }


}
