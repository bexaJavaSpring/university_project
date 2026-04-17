package uz.java.spring_boot_application.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import uz.java.spring_boot_application.dto.room.RoomFilter;
import uz.java.spring_boot_application.entities.Room;

import java.util.ArrayList;
import java.util.List;

public record RoomSpecification(RoomFilter filter) implements Specification<Room> {

    @Override
    public  Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.getName() != null)
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));

        if (filter.getGroupId() != null)
            predicates.add(criteriaBuilder.equal(root.get("group").get("id"), filter.getGroupId()));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
