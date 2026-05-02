package uz.java.spring_boot_application.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import uz.java.spring_boot_application.dto.lesson.LessonFilter;
import uz.java.spring_boot_application.entities.Lesson;

import java.util.ArrayList;
import java.util.List;

public record LessonSpecification(LessonFilter filter) implements Specification<Lesson> {
    @Override
    public Predicate toPredicate(Root<Lesson> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.getTitle() != null)
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + filter.getTitle().toLowerCase() + "%"));

        if (filter.getType() != null)
            predicates.add(criteriaBuilder.equal(root.get("type"), filter.getType()));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
