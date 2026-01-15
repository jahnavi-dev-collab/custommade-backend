package com.platform.custommade.repository;

import com.platform.custommade.model.RequestImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestImageRepository extends JpaRepository<RequestImage, Long> {

    // Find all images for a specific request
    List<RequestImage> findByRequestId(Long requestId);
}
