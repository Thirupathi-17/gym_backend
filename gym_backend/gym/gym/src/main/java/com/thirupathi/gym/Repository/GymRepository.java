package com.thirupathi.gym.Repository;

import com.thirupathi.gym.Entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRepository extends JpaRepository<Gym, Long> {
}
