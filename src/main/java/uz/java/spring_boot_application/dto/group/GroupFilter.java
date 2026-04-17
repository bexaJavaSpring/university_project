package uz.java.spring_boot_application.dto.group;

public record GroupFilter (Integer page,
                          Integer limit,
                          String sortBy,
                          String name,
                          String groupNumber,
                          Long facultyId) {
}
