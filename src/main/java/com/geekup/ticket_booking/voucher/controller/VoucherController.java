package com.geekup.ticket_booking.voucher.controller;

import com.geekup.ticket_booking.voucher.dto.VoucherRequest;
import com.geekup.ticket_booking.voucher.dto.VoucherResponse;
import com.geekup.ticket_booking.voucher.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VoucherResponse create(@Valid @RequestBody VoucherRequest request) {
        return voucherService.create(request);
    }

    @GetMapping
    public List<VoucherResponse> getAll() {
        return voucherService.getAll();
    }
}