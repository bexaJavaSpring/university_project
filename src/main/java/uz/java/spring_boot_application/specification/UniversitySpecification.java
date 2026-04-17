package uz.java.spring_boot_application.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import uz.java.spring_boot_application.dto.university.UniversityFilter;
import uz.java.spring_boot_application.entities.University;

import java.util.ArrayList;
import java.util.List;

public record UniversitySpecification(UniversityFilter filter) implements Specification<University> {

    @Override
    public Predicate toPredicate(Root<University> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.name() != null)
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filter.name().toLowerCase() + "%"));
        if (filter.phone() != null)
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + filter.phone().toLowerCase() + "%"));

        predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
