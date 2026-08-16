package com.geekup.ticket_booking.concert.service;

import com.geekup.ticket_booking.concert.dto.ConcertRequest;
import com.geekup.ticket_booking.concert.dto.ConcertResponse;

import java.util.List;

public interface ConcertService {

    ConcertResponse create(ConcertRequest request);

    List<ConcertResponse> getAll();

    ConcertResponse getById(Long id);

    ConcertResponse update(Long id, ConcertRequest request);

    void delete(Long id);
}