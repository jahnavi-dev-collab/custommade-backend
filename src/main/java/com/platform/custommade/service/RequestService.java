package com.platform.custommade.service;

import com.platform.custommade.dto.request.CreateRequestDTO;
import com.platform.custommade.dto.response.RequestResponseDTO;
import com.platform.custommade.model.Request;
import com.platform.custommade.model.RequestStatus;
import com.platform.custommade.model.User;
import com.platform.custommade.repository.RequestRepository;
import com.platform.custommade.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public RequestService(RequestRepository requestRepository,
                          UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    // ================= CREATE =================
    public RequestResponseDTO createRequest(Long customerId, CreateRequestDTO dto) {

        // 1️⃣ Fetch customer
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2️⃣ Create request entity
        Request request = new Request();
        request.setCustomer(customer);
        request.setCategory(dto.getCategory());
        request.setDescription(dto.getDescription());
        request.setFabricPref(dto.getFabricPref());
        request.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());

        // 3️⃣ Set status and timestamp
        request.setStatus(RequestStatus.OPEN);
        request.setCreatedAt(LocalDateTime.now());

        // 4️⃣ Save request
        Request saved = requestRepository.save(request);

        // 5️⃣ Map to DTO for response
        return mapToResponseDTO(saved);
    }

    // ================= READ =================

    public List<RequestResponseDTO> getRequestsByCustomer(Long customerId) {
        List<Request> requests = requestRepository.findByCustomerId(customerId);

        return requests.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public RequestResponseDTO getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        return mapToResponseDTO(request);
    }

    // ================= MAPPER =================
    private RequestResponseDTO mapToResponseDTO(Request request) {
        RequestResponseDTO dto = new RequestResponseDTO();
        dto.setId(request.getId());
        dto.setCategory(request.getCategory());
        dto.setDescription(request.getDescription());
        dto.setFabricPref(request.getFabricPref());
        dto.setStatus(request.getStatus() != null ? request.getStatus().name() : null);
        dto.setCreatedAt(request.getCreatedAt());
        dto.setExpectedDeliveryDate(request.getExpectedDeliveryDate());
        return dto;
    }
}
