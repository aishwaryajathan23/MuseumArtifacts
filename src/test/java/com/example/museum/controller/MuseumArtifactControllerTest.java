package com.example.museum.controller;

import com.example.museum.entity.MuseumArtifact;
import com.example.museum.service.MuseumArtifactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MuseumArtifactControllerTest {

    @Mock
    private MuseumArtifactService service;

    @InjectMocks
    private MuseumArtifactController controller;

    private MuseumArtifact artifact1;
    private MuseumArtifact artifact2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        artifact1 = new MuseumArtifact(1L, "Mona Lisa", "France", "Renaissance", "Famous painting");
        artifact2 = new MuseumArtifact(2L, "Terracotta Army", "China", "Qin Dynasty", "Clay soldiers");
    }

    @Test
    public void testListArtifacts() {
        when(service.getAllArtifacts()).thenReturn(Arrays.asList(artifact1, artifact2));
        List<MuseumArtifact> artifacts = controller.listArtifacts();
        assertEquals(2, artifacts.size());
        verify(service).getAllArtifacts();
    }

    @Test
    public void testAddArtifact_Success() {
        when(service.saveArtifact(artifact1)).thenReturn(artifact1);
        ResponseEntity<?> response = controller.addArtifact(artifact1);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(artifact1, response.getBody());
    }

    @Test
    public void testAddArtifact_Exception() {
        when(service.saveArtifact(artifact1)).thenThrow(new RuntimeException("Duplicate name"));
        ResponseEntity<?> response = controller.addArtifact(artifact1);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Duplicate name", response.getBody());
    }

    @Test
    public void testUpdateArtifact_Success() {
        when(service.updateArtifact(1L, artifact1)).thenReturn(artifact1);
        ResponseEntity<?> response = controller.updateArtifact(1L, artifact1);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(artifact1, response.getBody());
    }

    @Test
    public void testUpdateArtifact_Exception() {
        when(service.updateArtifact(1L, artifact1)).thenThrow(new RuntimeException("Artifact not found"));
        ResponseEntity<?> response = controller.updateArtifact(1L, artifact1);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Artifact not found", response.getBody());
    }

    @Test
    public void testDeleteArtifact_Success() {
        doNothing().when(service).deleteArtifact(1L);
        ResponseEntity<?> response = controller.deleteArtifact(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Artifact deleted successfully", response.getBody());
    }

    @Test
    public void testDeleteArtifact_Exception() {
        doThrow(new RuntimeException("Artifact not found")).when(service).deleteArtifact(99L);
        ResponseEntity<?> response = controller.deleteArtifact(99L);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Artifact not found", response.getBody());
    }

    @Test
    public void testGetArtifactById_Success() {
        when(service.getArtifactById(1L)).thenReturn(artifact1);
        ResponseEntity<?> response = controller.getArtifactById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(artifact1, response.getBody());
    }

    @Test
    public void testGetArtifactById_Exception() {
        when(service.getArtifactById(99L)).thenThrow(new RuntimeException("Artifact not found"));
        ResponseEntity<?> response = controller.getArtifactById(99L);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Artifact not found", response.getBody());
    }
}
