package com.thirupathi.gym.Controller;

import com.thirupathi.gym.Dto.GymDto;
import com.thirupathi.gym.Entity.Gym;
import com.thirupathi.gym.Service.GymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gym")
public class GymController {

    @Autowired
    private GymService gymService;

   @PostMapping("/add")
    public ResponseEntity<?> createGym(@RequestBody GymDto gym) {
        return gymService.addGym(gym);
    }

    @GetMapping
    public List<Gym> getAllProducts() {
        return gymService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Gym  getProductById(@PathVariable Long id) {
        return gymService.getGymById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?>  updateProduct(@PathVariable Long id, @RequestBody GymDto gym) {
        return gymService.updateProduct(id, gym);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        gymService.deleteProduct(id);
        return "Record deleted successfully";
    }

}
