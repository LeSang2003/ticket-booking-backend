package com.geekup.ticket_booking.tickettype.service.impl;

import com.geekup.ticket_booking.concert.entity.Concert;
import com.geekup.ticket_booking.concert.repository.ConcertRepository;
import com.geekup.ticket_booking.tickettype.dto.TicketTypeRequest;
import com.geekup.ticket_booking.tickettype.dto.TicketTypeResponse;
import com.geekup.ticket_booking.tickettype.entity.TicketType;
import com.geekup.ticket_booking.tickettype.repository.TicketTypeRepository;
import com.geekup.ticket_booking.tickettype.service.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;
    private final ConcertRepository concertRepository;

    @Override
    public TicketTypeResponse create(TicketTypeRequest request) {

        Concert concert = concertRepository.findById(request.getConcertId())
                .orElseThrow(() -> new RuntimeException("Concert not found"));

        TicketType ticketType = TicketType.builder()
                .name(request.getName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .concert(concert)
                .build();

        ticketTypeRepository.save(ticketType);

        return TicketTypeResponse.builder()
                .id(ticketType.getId())
                .name(ticketType.getName())
                .price(ticketType.getPrice())
                .quantity(ticketType.getQuantity())
                .concertId(concert.getId())
                .build();
    }

    @Override
    public List<TicketTypeResponse> getByConcert(Long concertId) {

        return ticketTypeRepository.findByConcertId(concertId)
                .stream()
                .map(ticket -> TicketTypeResponse.builder()
                        .id(ticket.getId())
                        .name(ticket.getName())
                        .price(ticket.getPrice())
                        .quantity(ticket.getQuantity())
                        .concertId(ticket.getConcert().getId())
                        .build())
                .toList();
    }
}