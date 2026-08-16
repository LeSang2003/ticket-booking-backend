package com.geekup.ticket_booking.concert.service.impl;

import com.geekup.ticket_booking.concert.dto.ConcertRequest;
import com.geekup.ticket_booking.concert.dto.ConcertResponse;
import com.geekup.ticket_booking.concert.entity.Concert;
import com.geekup.ticket_booking.concert.entity.ConcertStatus;
import com.geekup.ticket_booking.concert.repository.ConcertRepository;
import com.geekup.ticket_booking.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepository;

    @Override
    public ConcertResponse create(ConcertRequest request) {

        Concert concert = Concert.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .eventDate(request.getEventDate())
                .totalSeats(request.getTotalSeats())
                .availableSeats(request.getTotalSeats())
                .status(ConcertStatus.UPCOMING)
                .build();

        return mapToResponse(concertRepository.save(concert));
    }

    @Override
    public List<ConcertResponse> getAll() {

        return concertRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ConcertResponse getById(Long id) {

        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concert not found"));

        return mapToResponse(concert);
    }

    @Override
    public ConcertResponse update(Long id, ConcertRequest request) {

        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concert not found"));

        int seatDelta = request.getTotalSeats() - concert.getTotalSeats();

        concert.setTitle(request.getTitle());
        concert.setDescription(request.getDescription());
        concert.setLocation(request.getLocation());
        concert.setEventDate(request.getEventDate());
        concert.setTotalSeats(request.getTotalSeats());
        concert.setAvailableSeats(concert.getAvailableSeats() + seatDelta);

        return mapToResponse(concertRepository.save(concert));
    }
    
    @Override
    public void delete(Long id) {

        concertRepository.deleteById(id);
    }

    private ConcertResponse mapToResponse(Concert concert){

        return ConcertResponse.builder()
                .id(concert.getId())
                .title(concert.getTitle())
                .description(concert.getDescription())
                .location(concert.getLocation())
                .eventDate(concert.getEventDate())
                .totalSeats(concert.getTotalSeats())
                .availableSeats(concert.getAvailableSeats())
                .status(concert.getStatus())
                .build();
    }
}