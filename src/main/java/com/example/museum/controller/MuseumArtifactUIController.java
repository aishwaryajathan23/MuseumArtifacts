package com.example.museum.controller;

import com.example.museum.entity.MuseumArtifact;
import com.example.museum.service.MuseumArtifactService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/artifacts")
public class MuseumArtifactUIController {

    @Autowired
    private MuseumArtifactService service;

    @GetMapping
    public String listArtifacts(Model model) {
        List<MuseumArtifact> artifacts = service.getAllArtifacts();
        model.addAttribute("artifacts", artifacts);
        return "artifacts";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("artifact", new MuseumArtifact());
        return "add-artifact";
    }

    @PostMapping("/add")
    public String addArtifact(@Valid @ModelAttribute("artifact") MuseumArtifact artifact,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add-artifact";
        }
        try {
            service.saveArtifact(artifact);
            redirectAttributes.addFlashAttribute("message", "Artifact added successfully");
        } catch (RuntimeException e) {
            result.rejectValue("name", "duplicate", e.getMessage());
            return "add-artifact";
        }
        return "redirect:/artifacts";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MuseumArtifact artifact = service.getArtifactById(id);
        model.addAttribute("artifact", artifact);
        return "edit-artifact";
    }

    @PostMapping("/edit/{id}")
    public String updateArtifact(@PathVariable Long id,
                                 @Valid @ModelAttribute("artifact") MuseumArtifact artifact,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "edit-artifact";
        }

        try {
            service.updateArtifact(id, artifact);
            redirectAttributes.addFlashAttribute("message", "Artifact updated successfully");
        } catch (RuntimeException e) {
            result.rejectValue("name", "duplicate", e.getMessage());
            return "edit-artifact";
        }

        return "redirect:/artifacts";
    }


    @GetMapping("/delete/{id}")
    public String deleteArtifact(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        service.deleteArtifact(id);
        redirectAttributes.addFlashAttribute("message", "Artifact deleted successfully");
        return "redirect:/artifacts";
    }
}
