package com.example.museum.service;

import com.example.museum.entity.MuseumArtifact;

import java.util.List;

public interface MuseumArtifactService {
    List<MuseumArtifact> getAllArtifacts();
    MuseumArtifact getArtifactById(Long id);
    MuseumArtifact saveArtifact(MuseumArtifact artifact);
    MuseumArtifact updateArtifact(Long id, MuseumArtifact artifact);
    void deleteArtifact(Long id);
}
