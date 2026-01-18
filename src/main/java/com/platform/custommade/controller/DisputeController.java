package com.platform.custommade.controller;

import com.platform.custommade.dto.request.RequestDisputeDTO;
import com.platform.custommade.dto.response.DisputeResponseDTO;
import com.platform.custommade.model.Dispute;
import com.platform.custommade.model.DisputeStatus;
import com.platform.custommade.service.DisputeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/disputes")
public class DisputeController {

    private final DisputeService disputeService;

    public DisputeController(DisputeService disputeService) {
        this.disputeService = disputeService;
    }

    @PutMapping("/{disputeId}/resolve")
    public ResponseEntity<DisputeResponseDTO> resolveDispute(
            @PathVariable("disputeId") Long disputeId,
            @RequestParam("status") DisputeStatus status,
            @RequestParam(value = "adminNotes", required = false) String adminNotes
    ) {
        Dispute dispute = disputeService.resolveDispute(
                disputeId,
                status,
                adminNotes
        );

        return ResponseEntity.ok(mapToResponse(dispute));
    }

    private DisputeResponseDTO mapToResponse(Dispute dispute) {
        DisputeResponseDTO dto = new DisputeResponseDTO();
        dto.setId(dispute.getId());
        dto.setOrderId(dispute.getOrder().getId());
        dto.setReason(dispute.getReason());
        dto.setDescription(dispute.getDescription());
        dto.setStatus(dispute.getStatus().name());
        dto.setAdminNotes(dispute.getAdminNotes());
        dto.setCreatedAt(dispute.getCreatedAt());
        return dto;
    }
}
