package com.jedin.Repositories;

import com.jedin.models.Task;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Buscar tarefas por status de conclusão
    List<Task> findByCompleted(Boolean completed);

    // Buscar tarefas por status de conclusão com paginação
    Page<Task> findByCompleted(Boolean completed, Pageable pageable);

    // Buscar tarefas com data de vencimento específica
    List<Task> findByDueDate(LocalDate dueDate);

    // Buscar tarefas vencidas (não concluídas e com data passada)
    @Query("SELECT t FROM Task t WHERE t.dueDate < :today AND t.completed = false")
    List<Task> findOverdueTasks(@Param("today") LocalDate today);

    // Buscar tarefas que vencem hoje
    @Query("SELECT t FROM Task t WHERE t.dueDate = :today")
    List<Task> findTasksDueToday(@Param("today") LocalDate today);

    // Buscar tarefas por faixa de data de vencimento
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :startDate AND :endDate")
    List<Task> findByDueDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Buscar tarefas sem data de vencimento
    List<Task> findByDueDateIsNull();

    // Buscar tarefas por descrição (case-insensitive)
    @Query("SELECT t FROM Task t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    List<Task> findByDescriptionContainingIgnoreCase(@Param("description") String description);

    // Buscar tarefas criadas em uma data específica
    @Query("SELECT t FROM Task t WHERE DATE(t.creationDate) = :creationDate")
    List<Task> findByCreationDate(@Param("creationDate") LocalDate creationDate);

    // Buscar tarefas criadas em um período
    @Query("SELECT t FROM Task t WHERE t.creationDate BETWEEN :startDateTime AND :endDateTime")
    List<Task> findByCreationDateRange(@Param("startDateTime") java.time.LocalDateTime startDateTime,
                                      @Param("endDateTime") java.time.LocalDateTime endDateTime);

    // Contar tarefas por status
    Long countByCompleted(Boolean completed);

    // Contar tarefas vencidas
    @Query("SELECT COUNT(t) FROM Task t WHERE t.dueDate < :today AND t.completed = false")
    Long countOverdueTasks(@Param("today") LocalDate today);

    // Buscar tarefas ordenadas por data de criação (mais recentes primeiro)
    List<Task> findAllByOrderByCreationDateDesc();

    // Buscar tarefas ordenadas por data de vencimento
    List<Task> findAllByOrderByDueDateAsc();

    // Buscar tarefas próximas do vencimento (próximos N dias)
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :today AND :futureDate AND t.completed = false")
    List<Task> findTasksDueSoon(@Param("today") LocalDate today, @Param("futureDate") LocalDate futureDate);

    // Verificar se existe tarefa com descrição específica
    boolean existsByDescriptionIgnoreCase(String description);

    // Buscar tarefas com paginação ordenadas por data de criação
    Page<Task> findAllByOrderByCreationDateDesc(Pageable pageable);

    // Buscar tarefas não concluídas com paginação
    Page<Task> findByCompletedOrderByCreationDateDesc(Boolean completed, Pageable pageable);
}
