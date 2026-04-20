package uz.java.spring_boot_application.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import uz.java.spring_boot_application.dto.user.UserFilter;
import uz.java.spring_boot_application.entities.User;

import java.util.ArrayList;
import java.util.List;

public record UserSpecification(UserFilter userFilter) implements Specification<User> {
    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (userFilter.firstName() != null) {
         predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                 "%"+ userFilter.firstName().toLowerCase()+"%"));
        }
        if (userFilter.lastName() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
                    "%"+userFilter.lastName().toLowerCase()+"%"));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
