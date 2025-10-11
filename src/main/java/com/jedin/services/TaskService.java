package com.jedin.services;

import com.jedin.Repositories.TaskRepository;
import com.jedin.models.Task;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@BrowserCallable
@AnonymousAllowed
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Listar todas as tarefas com paginação
    public List<Task> list(Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "creationDate"));
        }
        Page<Task> page = taskRepository.findAllByOrderByCreationDateDesc(pageable);
        return page.getContent();
    }

    // Listar todas as tarefas sem paginação
    public List<Task> listAll() {
        return taskRepository.findAllByOrderByCreationDateDesc();
    }

    // Criar nova tarefa
    public Task createTask(@NotBlank String description, String dueDate) {
        Task task = new Task();
        task.setDescription(description.trim());

        if (dueDate != null && !dueDate.trim().isEmpty()) {
            try {
                task.setDueDate(LocalDate.parse(dueDate));
            } catch (Exception e) {
                throw new IllegalArgumentException("Formato de data inválido. Use YYYY-MM-DD");
            }
        }

        task.setCreationDate(LocalDateTime.now());
        task.setCompleted(false);

        return taskRepository.save(task);
    }

    // Buscar tarefa por ID
    public Optional<Task> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        return taskRepository.findById(id);
    }

    // Atualizar tarefa
    public Task updateTask(Long id, @Valid Task updatedTask) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada com ID: " + id));

        existingTask.setDescription(updatedTask.getDescription().trim());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setCompleted(updatedTask.getCompleted());

        return taskRepository.save(existingTask);
    }

    // Marcar tarefa como concluída
    public Task markAsCompleted(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada com ID: " + id));

        task.setCompleted(true);
        return taskRepository.save(task);
    }

    // Marcar tarefa como não concluída
    public Task markAsIncomplete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada com ID: " + id));

        task.setCompleted(false);
        return taskRepository.save(task);
    }

    // Deletar tarefa
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Tarefa não encontrada com ID: " + id);
        }
        taskRepository.deleteById(id);
    }

    // Buscar tarefas por status
    public List<Task> findByCompleted(boolean completed) {
        return taskRepository.findByCompleted(completed);
    }

    // Buscar tarefas pendentes
    public List<Task> findPendingTasks() {
        return taskRepository.findByCompleted(false);
    }

    // Buscar tarefas concluídas
    public List<Task> findCompletedTasks() {
        return taskRepository.findByCompleted(true);
    }

    // Buscar tarefas vencidas
    public List<Task> findOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDate.now());
    }

    // Buscar tarefas que vencem hoje
    public List<Task> findTasksDueToday() {
        return taskRepository.findTasksDueToday(LocalDate.now());
    }

    // Buscar tarefas que vencem em breve (próximos 7 dias)
    public List<Task> findTasksDueSoon() {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(7);
        return taskRepository.findTasksDueSoon(today, futureDate);
    }

    // Buscar tarefas por descrição
    public List<Task> searchByDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return List.of();
        }
        return taskRepository.findByDescriptionContainingIgnoreCase(description.trim());
    }

    // Buscar tarefas por data de vencimento
    public List<Task> findByDueDate(String dueDateStr) {
        try {
            LocalDate dueDate = LocalDate.parse(dueDateStr);
            return taskRepository.findByDueDate(dueDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de data inválido. Use YYYY-MM-DD");
        }
    }

    // Buscar tarefas sem data de vencimento
    public List<Task> findTasksWithoutDueDate() {
        return taskRepository.findByDueDateIsNull();
    }

    // Contar tarefas por status
    public Long countByCompleted(boolean completed) {
        return taskRepository.countByCompleted(completed);
    }

    // Contar tarefas vencidas
    public Long countOverdueTasks() {
        return taskRepository.countOverdueTasks(LocalDate.now());
    }

    // Obter estatísticas das tarefas
    public TaskStatistics getStatistics() {
        long totalTasks = taskRepository.count();
        long completedTasks = taskRepository.countByCompleted(true);
        long pendingTasks = taskRepository.countByCompleted(false);
        long overdueTasks = taskRepository.countOverdueTasks(LocalDate.now());

        return new TaskStatistics(totalTasks, completedTasks, pendingTasks, overdueTasks);
    }

    // Verificar se existe tarefa com descrição específica
    public boolean existsByDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return false;
        }
        return taskRepository.existsByDescriptionIgnoreCase(description.trim());
    }

    // Classe auxiliar para estatísticas
    public static class TaskStatistics {
        private final long totalTasks;
        private final long completedTasks;
        private final long pendingTasks;
        private final long overdueTasks;

        public TaskStatistics(long totalTasks, long completedTasks, long pendingTasks, long overdueTasks) {
            this.totalTasks = totalTasks;
            this.completedTasks = completedTasks;
            this.pendingTasks = pendingTasks;
            this.overdueTasks = overdueTasks;
        }

        public long getTotalTasks() { return totalTasks; }
        public long getCompletedTasks() { return completedTasks; }
        public long getPendingTasks() { return pendingTasks; }
        public long getOverdueTasks() { return overdueTasks; }

        public double getCompletionRate() {
            return totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0;
        }
    }
}
