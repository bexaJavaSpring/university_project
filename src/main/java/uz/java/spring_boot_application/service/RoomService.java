package uz.java.spring_boot_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.dto.DataDto;
import uz.java.spring_boot_application.dto.room.RoomFilter;
import uz.java.spring_boot_application.dto.room.RoomRequest;
import uz.java.spring_boot_application.dto.room.RoomResponse;
import uz.java.spring_boot_application.entities.Room;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.mapper.RoomMapper;
import uz.java.spring_boot_application.repository.RoomRepository;
import uz.java.spring_boot_application.specification.RoomSpecification;
import uz.java.spring_boot_application.specification.SearchSpecification;
import uz.java.spring_boot_application.util.CachePrefix;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final CacheManagerService cacheManagerService;

    @Transactional(readOnly = true)
    public DataDto<List<RoomResponse>> getAll(RoomFilter filter) {
        Object data = cacheManagerService.get(filter.hashCode() + "", CachePrefix.ROOM);
        if (data !=null){
            return (DataDto<List<RoomResponse>>) data;
        }
        RoomSpecification spec = new RoomSpecification(filter);
        List<Room> list = roomRepository.findAll(spec, SearchSpecification.getPageable(
                filter.getPage(), filter.getLimit(), filter.getSortBy()
        )).toList();

        List<RoomResponse> response = list.stream().map(roomMapper::toResponse).toList();
        cacheManagerService.put(filter.hashCode() + "", CachePrefix.ROOM, new DataDto<>(response));
        return new DataDto<>(response);
    }

    @Transactional(readOnly = true)
    public RoomResponse getOne(Long id) {
        Object data = cacheManagerService.get(id.toString(), CachePrefix.ROOM);
        if (data!=null){
            return (RoomResponse) data;
        }
        Room room = roomRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("room.not.found")
        );

        RoomResponse response = roomMapper.toResponse(room);
        cacheManagerService.put(id.toString(), CachePrefix.ROOM, response);
        return response;
    }
    @Transactional
    public Long create(RoomRequest roomRequest) {
        Room entity = roomMapper.toEntity(roomRequest);
        Room save = roomRepository.save(entity);
        cacheManagerService.delete(CachePrefix.ROOM);
        return save.getId();
    }
    @Transactional
    public Long update(RoomRequest roomRequest, Long id) {
        Room room = roomRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("room.not.found")
        );
        roomMapper.updateFromRequest(roomRequest, room);

        Long id1 = roomRepository.save(room).getId();
        cacheManagerService.delete(CachePrefix.ROOM);
        return id1;
    }
    @Transactional
    public Boolean delete(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(
                () -> new GenericNotFoundException("room.not.found")
        );
        room.markAsDeleted();
        roomRepository.save(room);
        cacheManagerService.delete(CachePrefix.ROOM);
        return true;
    }
}
