package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.logic.ProjectService;
import io.github.mat3e.todoapp.model.Project;
import io.github.mat3e.todoapp.model.ProjectRepository;
import io.github.mat3e.todoapp.model.ProjectStep;
import io.github.mat3e.todoapp.model.projection.ProjectWriteAndReadModel;
import io.micrometer.core.annotation.Timed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectRepository repository;
    private final ProjectService service;

    public ProjectController(final ProjectService service, final ProjectRepository repository) {
        this.service = service;
        this.repository = repository;
    }

//    @RolesAllowed("ROLE_ADMIN")
    @GetMapping
    String getProjectTemplate(Model model, Authentication auth) {
        //if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("project", new ProjectWriteAndReadModel());
            return "projects";
//        }
//        return "index";
    }

    @PostMapping
    String addProject(
            @ModelAttribute("project") @Valid ProjectWriteAndReadModel current,
            BindingResult bindingResult,
            Model model
    ) {
        if(bindingResult.hasErrors()) {
            return "projects";
        }
        service.save(current);
        model.addAttribute("project", new ProjectWriteAndReadModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("msg", "Project added!!!");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteAndReadModel current) {
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

   // @Timed(value = "project.create.group", histogram = true, percentiles = {0.5, 0.95, 0.99})
    @PostMapping("/{id}")
    String createGroup(
            @ModelAttribute("project") ProjectWriteAndReadModel current,
            Model model,
            @PathVariable int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline
    ) {
        try {
            service.createGroup(deadline, id);
            model.addAttribute("msg", "New group has been created");
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("msg", "Error when creating group from this project");
        }

        return "projects";
    }

    @ModelAttribute("projects")
    List<ProjectWriteAndReadModel> getProjects() {
        return service.readAll();
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteProject(@PathVariable Integer id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
