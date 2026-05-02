package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.dto.lesson.LessonFilter;
import uz.java.spring_boot_application.dto.lesson.LessonRequest;
import uz.java.spring_boot_application.dto.lesson.LessonResponse;
import uz.java.spring_boot_application.entities.Lesson;
import uz.java.spring_boot_application.entities.Subjects;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.LessonMapper;
import uz.java.spring_boot_application.repository.LessonRepository;
import uz.java.spring_boot_application.repository.SubjectRepository;
import uz.java.spring_boot_application.specification.LessonSpecification;
import uz.java.spring_boot_application.specification.SearchSpecification;

import java.util.List;


@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository repository;
    private final LessonMapper mapper;
    private final SubjectRepository subjectRepository;


    public List<LessonResponse> getAll(LessonFilter filter) {
        LessonSpecification spec = new LessonSpecification(filter);
        List<Lesson> all = repository.findAll(spec, SearchSpecification.getPageable(
                filter.getPage(), filter.getLimit(), filter.getSortBy()
        )).toList();
        return all.stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public Long create(LessonRequest request) {
        subjectRepository.findById(request.getSubjects().getId()).orElseThrow(
                () -> new GenericNotFoundException("subject.not.found")
        );
        Lesson lesson = mapper.toEntity(request);
        repository.save(lesson);
        return lesson.getId();
    }

    @Transactional
    public Long update(Long id, LessonRequest request) {
        var lesson = repository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("lesson.not.found")
        );
        mapper.updateFromRequest(request, lesson);
        if (request.getSubjects().getId() != null) {
            Subjects subjects = subjectRepository.findById(request.getSubjects().getId()).orElseThrow(
                    () -> new GenericNotFoundException("subject.not.found")
            );
            lesson.setSubjects(subjects);
        }
        repository.save(lesson);
        return lesson.getId();
    }

    @Transactional
    public Boolean delete(Long id) {
        var lesson = repository.findById(id).orElse(null);
        if (lesson == null)
            throw new GenericNotFoundException("lesson.not.found");
        lesson.markAsDeleted();
        repository.save(lesson);
        return true;
    }
}
