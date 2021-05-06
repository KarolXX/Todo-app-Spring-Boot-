package io.github.mat3e.todoapp.comparator;

import io.github.mat3e.todoapp.model.Project;

import java.util.Comparator;

public class ProjectDescriptionComparator implements Comparator<Project> {
    @Override
    public int compare(Project p1, Project p2) {
        return p1.getDescription().compareTo(p2.getDescription());
    }
}
