package com.geekup.ticket_booking.voucher.service.impl;

import com.geekup.ticket_booking.voucher.dto.VoucherRequest;
import com.geekup.ticket_booking.voucher.dto.VoucherResponse;
import com.geekup.ticket_booking.voucher.entity.Voucher;
import com.geekup.ticket_booking.voucher.repository.VoucherRepository;
import com.geekup.ticket_booking.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    @Override
    public VoucherResponse create(VoucherRequest request) {

        if (voucherRepository.findByCode(request.getCode()).isPresent()) {
            throw new RuntimeException("Voucher code already exists");
        }

        Voucher voucher = Voucher.builder()
                .code(request.getCode())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .totalQuantity(request.getTotalQuantity())
                .remainingQuantity(request.getTotalQuantity())
                .validFrom(request.getValidFrom())
                .validTo(request.getValidTo())
                .build();

        return toResponse(voucherRepository.save(voucher));
    }

    @Override
    public List<VoucherResponse> getAll() {
        return voucherRepository.findAll().stream().map(this::toResponse).toList();
    }

    private VoucherResponse toResponse(Voucher v) {
        return VoucherResponse.builder()
                .id(v.getId())
                .code(v.getCode())
                .discountType(v.getDiscountType())
                .discountValue(v.getDiscountValue())
                .totalQuantity(v.getTotalQuantity())
                .remainingQuantity(v.getRemainingQuantity())
                .validFrom(v.getValidFrom())
                .validTo(v.getValidTo())
                .build();
    }
}