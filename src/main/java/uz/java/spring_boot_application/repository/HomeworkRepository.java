package uz.java.spring_boot_application.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.java.spring_boot_application.entities.Homework;
@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> , JpaSpecificationExecutor<Homework> {
    @Query("""
          select h from Homework h
          where (:title is null or h.title = :title)
          and (:groupId is null or h.groupId = :groupId)""")
    Page<Homework> findByAllHomework(String title, Long groupId, Pageable pageRequest);
}
