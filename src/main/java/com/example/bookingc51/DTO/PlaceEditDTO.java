package com.example.bookingc51.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceEditDTO {
    @Pattern(regexp = "([A-Za-z0-9\\s]{5,20})|(^$)", message = "Name may have only upper and lower case and digits. " +
            "Min length - 5, max - 15!")
    private String name;
    @Pattern(regexp = "([A-Za-z0-9-\\s,.]{3,20})|(^$)", message = "Only upper and lower case, digits and '-' '_' ',' '.' ' '" +
            "Min length - 3, max - 20")
    private String address;
    private String workTime;
    @Pattern(regexp = "(\\d+)|(^$)", message = "Only numbers")
    private String tableCount;
    private List<MultipartFile> images;
    @Size(max = 1500, message = "Max size - 1500")
    private String description;
}
