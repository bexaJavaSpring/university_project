package uz.java.spring_boot_application.dto.user;

public record UserFilter(int page, int size, String sortBy, String firstName, String lastName) {

}
