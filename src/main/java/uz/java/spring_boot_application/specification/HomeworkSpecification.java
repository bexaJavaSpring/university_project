package uz.java.spring_boot_application.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import uz.java.spring_boot_application.dto.homework.HomeworkFilter;
import uz.java.spring_boot_application.entities.Homework;

import java.util.ArrayList;
import java.util.List;

public record HomeworkSpecification(HomeworkFilter homeworkFilter)implements Specification<Homework> {
    @Override
    public Predicate toPredicate(Root<Homework> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (homeworkFilter.title() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.
                    lower(root.get("title")), "%"+homeworkFilter.title().toLowerCase()+"%"));
        }
        if (homeworkFilter.groupId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("groupId"), homeworkFilter.groupId()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
