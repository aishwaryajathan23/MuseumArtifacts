package com.example.museum.controller;

import com.example.museum.entity.MuseumArtifact;
import com.example.museum.service.MuseumArtifactService;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artifacts")
public class MuseumArtifactController {

    @Autowired
    private MuseumArtifactService service;

    @GetMapping
    public List<MuseumArtifact> listArtifacts() {
        return service.getAllArtifacts();
    }

    @PostMapping
    public ResponseEntity<?> addArtifact(@Valid @RequestBody MuseumArtifact artifact) {
        try {
            MuseumArtifact saved = service.saveArtifact(artifact);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateArtifact(@PathVariable Long id, @Valid @RequestBody MuseumArtifact artifact) {
        try {
            MuseumArtifact updated = service.updateArtifact(id, artifact);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtifact(@PathVariable Long id) {
        try {
            service.deleteArtifact(id);
            return ResponseEntity.ok("Artifact deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArtifactById(@PathVariable Long id) {
        try {
            MuseumArtifact artifact = service.getArtifactById(id);
            return ResponseEntity.ok(artifact);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
