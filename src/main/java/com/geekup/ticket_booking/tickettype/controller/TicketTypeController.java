package com.geekup.ticket_booking.tickettype.controller;

import com.geekup.ticket_booking.tickettype.dto.TicketTypeRequest;
import com.geekup.ticket_booking.tickettype.dto.TicketTypeResponse;
import com.geekup.ticket_booking.tickettype.service.TicketTypeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-types")
@RequiredArgsConstructor
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketTypeResponse create(@Valid @RequestBody TicketTypeRequest request) {
        return ticketTypeService.create(request);
    }

    @GetMapping("/concert/{concertId}")
    public List<TicketTypeResponse> getByConcert(@PathVariable Long concertId) {
        return ticketTypeService.getByConcert(concertId);
    }
}