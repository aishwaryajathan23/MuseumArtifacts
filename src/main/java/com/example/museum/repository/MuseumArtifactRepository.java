package com.example.museum.repository;

import com.example.museum.entity.MuseumArtifact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MuseumArtifactRepository extends JpaRepository<MuseumArtifact, Long> {
    boolean existsByName(String name);
    MuseumArtifact findByName(String name);

}
