package com.example.museum.controller;

import com.example.museum.entity.MuseumArtifact;
import com.example.museum.service.MuseumArtifactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MuseumArtifactUIController.class)
public class MuseumArtifactUIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MuseumArtifactService service;

    private MuseumArtifact artifact;

    @BeforeEach
    void setup() {
        artifact = new MuseumArtifact(1L, "Sword", "India", "Ancient", "A royal weapon");
    }

    @Test
    void testListArtifacts() throws Exception {
        Mockito.when(service.getAllArtifacts()).thenReturn(Arrays.asList(artifact));

        mockMvc.perform(get("/artifacts"))
                .andExpect(status().isOk())
                .andExpect(view().name("artifacts"))
                .andExpect(model().attributeExists("artifacts"))
                .andExpect(model().attribute("artifacts", hasSize(1)));
    }

    @Test
    void testShowAddForm() throws Exception {
        mockMvc.perform(get("/artifacts/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-artifact"))
                .andExpect(model().attributeExists("artifact"));
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        mockMvc.perform(post("/artifacts/add")
                        .param("name", "Spear")
                        .param("origin", "India")
                        .param("period", "Medieval")
                        .param("description", "War weapon"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/artifacts"))
                .andExpect(flash().attribute("message", "Artifact added successfully"));

        Mockito.verify(service).saveArtifact(any(MuseumArtifact.class));
    }

    @Test
    void testAddArtifactValidationFailure() throws Exception {
        mockMvc.perform(post("/artifacts/add")
                        .param("name", "") // blank -> invalid
                        .param("origin", "")
                        .param("era", "")
                        .param("description", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("add-artifact"));
    }

    @Test
    void testShowEditForm() throws Exception {
        Mockito.when(service.getArtifactById(1L)).thenReturn(artifact);

        mockMvc.perform(get("/artifacts/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-artifact"))
                .andExpect(model().attributeExists("artifact"))
                .andExpect(model().attribute("artifact", hasProperty("name", is("Sword"))));
    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
        mockMvc.perform(post("/artifacts/edit/1")
                        .param("name", "Updated Sword")
                        .param("origin", "India")
                        .param("period", "Modern")
                        .param("description", "Updated"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/artifacts"))
                .andExpect(flash().attribute("message", "Artifact updated successfully"));

        Mockito.verify(service).updateArtifact(eq(1L), any(MuseumArtifact.class));
    }

    @Test
    void testUpdateArtifactValidationFailure() throws Exception {
        mockMvc.perform(post("/artifacts/edit/1")
                        .param("name", "")
                        .param("origin", "")
                        .param("era", "")
                        .param("description", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-artifact"));
    }

    @Test
    void testDeleteArtifact() throws Exception {
        mockMvc.perform(get("/artifacts/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/artifacts"))
                .andExpect(flash().attribute("message", "Artifact deleted successfully"));

        Mockito.verify(service).deleteArtifact(1L);
    }
}
