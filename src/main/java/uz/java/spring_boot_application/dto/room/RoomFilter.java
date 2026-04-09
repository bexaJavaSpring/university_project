package uz.java.spring_boot_application.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.java.spring_boot_application.dto.BaseFilter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomFilter extends BaseFilter {
    private String name;
    private String roomNumber;
    private Long groupId;

    public RoomFilter(Integer page, Integer limit, String sortBy, String name, String roomNumber, Long groupId){
        super(page, limit, sortBy);
        this.groupId = groupId;
        this.name = name;
        this.roomNumber = roomNumber;
    }
}
