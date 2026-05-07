package uz.java.spring_boot_application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.entities.Faculty;
import uz.java.spring_boot_application.entities.Group;
import uz.java.spring_boot_application.entities.Student;
import uz.java.spring_boot_application.repository.FacultyRepository;
import uz.java.spring_boot_application.repository.GroupRepository;
import uz.java.spring_boot_application.repository.StudentRepository;

@Service
public class CascadeTypeTestService {

    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public CascadeTypeTestService(PasswordEncoder passwordEncoder, GroupRepository groupRepository, StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.passwordEncoder = passwordEncoder;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public void createGroup() {   // bu yerda parent entity - Group, child - Student
        Group group = new Group();
        group.setGroupNumber("102A");
        group.setName("something");
        group.setFaculty(facultyRepository.findById(1L).orElse(null));

        Student student = new Student();
        student.setAge(23);
        student.setFirstName("Jack");
        student.setLastName("Henry");
        student.setUsername("henry");
        student.setPassword(passwordEncoder.encode("1234"));
        student.setGroup(group);
//        studentRepository.save(student); bu yerda bu narsa CascdeType.PERSIST bolgai uchun kerakmas
        group.getStudents().add(student);
        groupRepository.save(group);
    }


    public void updateGroup() {
        Group group = groupRepository.findById(4L).orElse(null);
        group.getStudents().get(0).setFirstName("firstname");
        group.getStudents().get(0).setLastName("lastName");
        // bu yerda student ni repository dan update qilmasak ham automatik ozi update boldi
        groupRepository.save(group);
    }

    public void deleteGroup() {
        Group group = groupRepository.findById(5L).orElse(null);
        groupRepository.delete(group);
    }
}
