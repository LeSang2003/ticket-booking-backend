package com.geekup.ticket_booking.voucher.service;

import com.geekup.ticket_booking.voucher.dto.VoucherRequest;
import com.geekup.ticket_booking.voucher.dto.VoucherResponse;

import java.util.List;

public interface VoucherService {
    VoucherResponse create(VoucherRequest request);
    List<VoucherResponse> getAll();
}