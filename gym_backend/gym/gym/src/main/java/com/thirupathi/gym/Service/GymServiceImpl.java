package com.thirupathi.gym.Service;

import com.thirupathi.gym.Dto.GymDto;
import com.thirupathi.gym.Entity.Enum.Status;
import com.thirupathi.gym.Entity.Gym;
import com.thirupathi.gym.Repository.GymRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GymServiceImpl implements GymService {

    @Autowired
    private GymRepository gymRepository;

    @Override
    public ResponseEntity<?> addGym(GymDto gym) {

            Gym newGymproduct = new Gym();
            newGymproduct.setUserName(gym.getUserName());
            newGymproduct.setPlan(gym.getPlan());
            newGymproduct.setWeight(gym.getWeight());
            newGymproduct.setFeesStatus(gym.getFeesStatus());
            newGymproduct.setStatus(Status.ACTIVE);
            gymRepository.save(newGymproduct);

         return ResponseEntity.ok(newGymproduct);
    }

    @Override
    public List<Gym> getAllProducts() {
        return gymRepository.findAll();
    }

    @Override
    public Gym getGymById(Long gymId) {
        return gymRepository.findById(gymId)
                .orElseThrow(() -> new RuntimeException(" Gym User not found with id: " + gymId));
    }

    @Override
    public ResponseEntity<?> updateProduct(Long id, GymDto gym) {

        try{

            Gym existingUser = gymRepository.findById(id).orElseThrow(
                    () -> new RuntimeException(" Gym Record not found ...")
            );

            existingUser.setUserName(
                    gym.getUserName() != null && !gym.getUserName().isEmpty()
                            ? gym.getUserName()
                            : existingUser.getUserName()
            );
            existingUser.setPlan(
                    gym.getPlan() != null
                            ? gym.getPlan()
                            : existingUser.getPlan()
            );


            existingUser.setWeight(
                    gym.getWeight() != null
                            ? gym.getWeight()
                            : existingUser.getWeight()
            );

            existingUser.setFeesStatus(
                    gym.getFeesStatus() != null
                            ? gym.getFeesStatus()
                            : existingUser.getFeesStatus()
            );

            existingUser.setStatus(
                    gym.getStatus() != null
                            ? gym.getStatus()
                            : existingUser.getStatus()
            );
            return ResponseEntity.ok(gymRepository.save(existingUser));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(" Error Message : " + e.getMessage());
        }
    }

    @Override
    public String deleteProduct(Long gymId) {
        gymRepository.deleteById(gymId);
        return "Gym Record Deleted Successfully";
    }
}
