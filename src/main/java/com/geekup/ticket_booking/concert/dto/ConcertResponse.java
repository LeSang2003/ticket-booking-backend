package com.geekup.ticket_booking.concert.dto;

import com.geekup.ticket_booking.concert.entity.ConcertStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConcertResponse {

    private Long id;

    private String title;

    private String description;

    private String location;

    private LocalDateTime eventDate;

    private Integer totalSeats;

    private Integer availableSeats;

    private ConcertStatus status;
}