package com.geekup.ticket_booking.concert.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
public class ConcertRequest {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String location;

    @NotNull
    @Future
    private LocalDateTime eventDate;
    
    @NotNull
    @Positive
    private Integer totalSeats;
}