package com.thirupathi.gym.Service;

import com.thirupathi.gym.Dto.GymDto;
import com.thirupathi.gym.Entity.Gym;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GymService {

    ResponseEntity<?> addGym(GymDto gym);

   List<Gym> getAllProducts();

    Gym getGymById(Long gymId);

    ResponseEntity<?> updateProduct (Long id, GymDto gym);

    String deleteProduct(Long gymId);

}
