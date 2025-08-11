package com.example.museum.service;

import com.example.museum.entity.MuseumArtifact;
import com.example.museum.repository.MuseumArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MuseumArtifactServiceImpl implements MuseumArtifactService {

    @Autowired
    MuseumArtifactRepository repository;

    @Cacheable(value = "artifactsCache")
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<MuseumArtifact> getAllArtifacts() {
        return repository.findAll();
    }

    @Cacheable(value = "artifactsCache", key = "#id")
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public MuseumArtifact getArtifactById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artifact not found with id " + id));
    }

    @CacheEvict(value = "artifactsCache", allEntries = true)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public MuseumArtifact saveArtifact(MuseumArtifact artifact) {
        if (repository.existsByName(artifact.getName())) {
            throw new RuntimeException("Artifact with name '" + artifact.getName() + "' already exists.");
        }
        return repository.save(artifact);
    }

    @Override
    @CacheEvict(value = "artifactsCache", allEntries = true)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public MuseumArtifact updateArtifact(Long id, MuseumArtifact artifact) {
        MuseumArtifact existingArtifact = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artifact not found with id " + id));

        if (!existingArtifact.getName().equals(artifact.getName())) {
            MuseumArtifact artifactWithName = repository.findByName(artifact.getName());
            if (artifactWithName != null && !artifactWithName.getId().equals(id)) {
                throw new RuntimeException("Another artifact with name '" + artifact.getName() + "' already exists.");
            }
        }

        existingArtifact.setName(artifact.getName());
        existingArtifact.setOrigin(artifact.getOrigin());
        existingArtifact.setPeriod(artifact.getPeriod());
        existingArtifact.setDescription(artifact.getDescription());

        return existingArtifact;
    }

    @CacheEvict(value = "artifactsCache", allEntries = true)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteArtifact(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Artifact not found with id " + id);
        }
        repository.deleteById(id);
    }
}
