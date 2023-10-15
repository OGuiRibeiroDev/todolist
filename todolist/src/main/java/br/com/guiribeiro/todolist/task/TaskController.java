package br.com.guiribeiro.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskmodel, HttpServletRequest request)
    {
    var idUser = request.getAttribute("idUser");
    taskmodel.setIdUser((UUID) idUser);


    var currentDate = LocalDateTime.now();
    if(currentDate.isAfter(taskmodel.getStartAt()) || currentDate.isAfter(taskmodel.getEndAt()))
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A DATA DE INÍCIO / TÉRMINO DEVE SER MAIOR QUE A DATA ATUAL");
    }

    if(taskmodel.getStartAt().isAfter(taskmodel.getEndAt()))
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A DATA DE INÍCIO DEVE SER MENOR QUE A DATA DE TÉRMINO");
    }

    var task = this.taskRepository.save(taskmodel);
    return ResponseEntity.status(HttpStatus.OK).body(task);
    }


    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request)
    {
    var idUser = request.getAttribute("idUser");
    var tasks = this.taskRepository.findByIdUser((UUID) idUser);
    return tasks;
    }

    @PutMapping("/{id}")
    public TaskModel update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id)
    {
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID)idUser);
        taskModel.setId(id);
        return this.taskRepository.save(taskModel);
    }

    
}
