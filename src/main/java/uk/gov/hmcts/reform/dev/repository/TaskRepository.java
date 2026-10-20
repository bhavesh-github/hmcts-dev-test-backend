package uk.gov.hmcts.reform.dev.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.gov.hmcts.reform.dev.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.deletedFlag = false")
    List<Task> findAllActive();

    @Query("SELECT t FROM Task t WHERE t.id = :id AND t.deletedFlag = false")
    Optional<Task> findActiveById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Task t SET t.deletedFlag = true, t.modifiedAt = :now, t.modifiedBy.id = :modifierId"
        + " WHERE t.id = :id")
    int markAsDeleted(
        @Param("id") Long id,
        @Param("modifierId") Long modifierId,
        @Param("now") LocalDateTime now
    );

    @Modifying
    @Query("UPDATE Task t SET t.deletedFlag = true, t.modifiedAt = :now WHERE t.id = :id")
    int markAsDeletedWithoutModifier(
        @Param("id") Long id,
        @Param("now") LocalDateTime now
    );
}
