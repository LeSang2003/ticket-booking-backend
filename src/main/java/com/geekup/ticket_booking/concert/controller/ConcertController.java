package com.geekup.ticket_booking.concert.controller;

import com.geekup.ticket_booking.concert.dto.ConcertRequest;
import com.geekup.ticket_booking.concert.dto.ConcertResponse;
import com.geekup.ticket_booking.concert.service.ConcertService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    @PostMapping
    public ConcertResponse create(@Valid @RequestBody ConcertRequest request){
        return concertService.create(request);
    }

    @GetMapping
    public List<ConcertResponse> getAll(){
        return concertService.getAll();
    }

    @GetMapping("/{id}")
    public ConcertResponse getById(@PathVariable Long id){
        return concertService.getById(id);
    }

    @PutMapping("/{id}")
    public ConcertResponse update(@PathVariable Long id,
                                  @Valid @RequestBody ConcertRequest request){
        return concertService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        concertService.delete(id);
    }
}