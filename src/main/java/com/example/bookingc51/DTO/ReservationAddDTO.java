package com.example.bookingc51.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationAddDTO {
    @Size(max = 1500, message = "Max size - 1500")
    private String comment;

}
