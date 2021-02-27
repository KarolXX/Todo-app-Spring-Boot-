package io.github.mat3e.todoapp.logic;

import io.github.mat3e.todoapp.model.TaskGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TempService {
    //FIXME: n + 1 selects
    @Autowired
    List<String> temp(TaskGroupRepository repository) {
        return repository.findAll().stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream()
                        .map(task -> task.getDescription()))
                .collect(Collectors.toList());
    }
}
