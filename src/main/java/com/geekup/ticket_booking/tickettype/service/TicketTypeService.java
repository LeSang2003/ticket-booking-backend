package com.geekup.ticket_booking.tickettype.service;

import com.geekup.ticket_booking.tickettype.dto.TicketTypeRequest;
import com.geekup.ticket_booking.tickettype.dto.TicketTypeResponse;

import java.util.List;

public interface TicketTypeService {

    TicketTypeResponse create(TicketTypeRequest request);

    List<TicketTypeResponse> getByConcert(Long concertId);
}