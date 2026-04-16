package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.dto.group.GroupFilter;
import uz.java.spring_boot_application.dto.group.GroupRequest;
import uz.java.spring_boot_application.dto.group.GroupResponse;
import uz.java.spring_boot_application.entities.Group;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.GroupMapper;
import uz.java.spring_boot_application.repository.FacultyRepository;
import uz.java.spring_boot_application.repository.GroupRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final FacultyRepository facultyRepository;

    @Transactional(readOnly = true)
    public List<GroupResponse> getAll(GroupFilter filter) {
        int page = filter.page() != null ? filter.page() : 0;
        int limit = filter.limit() != null ? filter.limit() : 10;

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(filter.sortBy() != null ? filter.sortBy() : "id").ascending());
        Page<Group> allCustom = groupRepository.findAllCustom(filter.name(), filter.groupNumber(), filter.facultyId(), pageRequest);
        return allCustom.stream().map(groupMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public GroupResponse getOne(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("Group not found")
        );
        return groupMapper.toResponse(group);
    }

    @Transactional()
    public Long create(GroupRequest request) {
        facultyRepository.findById(request.getFacultyId()).orElseThrow(
                () -> new GenericNotFoundException("Faculty not found")
        );
        Group group = groupMapper.toEntity(request);
        Group save = groupRepository.save(group);
        return save.getId();
    }

    @Transactional
    public Boolean update(Long id, GroupRequest request) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("Group not found")
        );
        groupMapper.updateFromRequest(request, group);
        groupRepository.save(group);
        return true;
    }

    @Transactional
    public Boolean delete(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("Group not found")
        );
        group.markAsDeleted();
        groupRepository.save(group);
        return true;
    }
}
