package com.example.museum.service;

import com.example.museum.entity.MuseumArtifact;
import com.example.museum.repository.MuseumArtifactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MuseumArtifactServiceImplTest {

    @Mock
    private MuseumArtifactRepository repository;

    @InjectMocks
    private MuseumArtifactServiceImpl service;

    private MuseumArtifact artifact1;
    private MuseumArtifact artifact2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        artifact1 = new MuseumArtifact(1L, "Mona Lisa", "France", "Renaissance", "Famous painting");
        artifact2 = new MuseumArtifact(2L, "Terracotta Army", "China", "Qin Dynasty", "Clay soldiers");
    }

    @Test
    public void testGetAllArtifacts() {
        when(repository.findAll()).thenReturn(Arrays.asList(artifact1, artifact2));
        List<MuseumArtifact> result = service.getAllArtifacts();
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    public void testGetArtifactById_Found() {
        when(repository.findById(1L)).thenReturn(Optional.of(artifact1));
        MuseumArtifact result = service.getArtifactById(1L);
        assertEquals("Mona Lisa", result.getName());
    }

    @Test
    public void testGetArtifactById_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getArtifactById(99L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    public void testSaveArtifact_Success() {
        when(repository.existsByName(artifact1.getName())).thenReturn(false);
        when(repository.save(artifact1)).thenReturn(artifact1);

        MuseumArtifact saved = service.saveArtifact(artifact1);
        assertNotNull(saved);
        assertEquals("Mona Lisa", saved.getName());
    }

    @Test
    public void testSaveArtifact_DuplicateName() {
        when(repository.existsByName(artifact1.getName())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.saveArtifact(artifact1));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    public void testUpdateArtifact_Success() {
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.findByName(artifact1.getName())).thenReturn(artifact1); // same artifact with name
        when(repository.save(artifact1)).thenReturn(artifact1);

        MuseumArtifact updated = service.updateArtifact(1L, artifact1);
        assertEquals("Mona Lisa", updated.getName());
        verify(repository).save(artifact1);
    }

    @Test
    public void testUpdateArtifact_NotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateArtifact(99L, artifact1));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    public void testUpdateArtifact_DuplicateNameConflict() {
        MuseumArtifact anotherArtifact = new MuseumArtifact(2L, "Mona Lisa", "France", "Renaissance", "Desc");

        when(repository.existsById(1L)).thenReturn(true);
        when(repository.findByName(artifact1.getName())).thenReturn(anotherArtifact);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateArtifact(1L, artifact1));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    public void testDeleteArtifact_Success() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> service.deleteArtifact(1L));
        verify(repository).deleteById(1L);
    }

    @Test
    public void testDeleteArtifact_NotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.deleteArtifact(99L));
        assertTrue(ex.getMessage().contains("not found"));
    }
}
