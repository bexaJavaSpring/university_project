package uz.java.spring_boot_application.dto.homework;

public record HomeworkFilter(int page, int size, String sortBy, String title, Long groupId) {
}
