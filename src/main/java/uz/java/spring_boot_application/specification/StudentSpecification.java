package uz.java.spring_boot_application.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import uz.java.spring_boot_application.dto.student.StudentFilter;
import uz.java.spring_boot_application.entities.Student;

import java.util.ArrayList;
import java.util.List;

public record StudentSpecification(StudentFilter filter) implements Specification<Student> {

    @Override
    public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.getAge() != null)
            predicates.add(criteriaBuilder.equal(root.get("age"), filter.getAge()));
        predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
