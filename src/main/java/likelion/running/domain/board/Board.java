package likelion.running.domain.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import likelion.running.domain.guest.Guest;
import likelion.running.web.dto.boardDto.EditBoardDto;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hostId;

    @Column(length = 100)
    private String title;

    @Column(length = 500)
    private String content;
    private FlagType flag;

    private String place;

    private LocalDate time;
    private String runTime;
    private String walkTime;
    private int playTime;
    private int repeat;
    private int joinMember;
    private int totalMember;

    private BoardStatus status;

    @JsonManagedReference
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    private List<Guest> guests;

    public void joinGuest(Guest guest){
        if(guests.isEmpty()){
            guests = new ArrayList<>();
        }
        guests.add(guest);
    }
    public EditBoardDto.EditBoardDtoBuilder toEditor(){
        return EditBoardDto.builder()
                .memberId(hostId)
                .title(title)
                .content(content)
                .place(place)
                .runTime(runTime)
                .walkTime(walkTime)
                .time(time)
                .repeat(repeat)
                .totalMember(totalMember)
                .status(status)
                .flag(flag)
                .guests(guests)
                .playTime(playTime);
    }

    public void edit(EditBoardDto editBoardDto){
        title = editBoardDto.getTitle();
        content = editBoardDto.getContent();
        flag = editBoardDto.getFlag();
        place = editBoardDto.getPlace();
        runTime = editBoardDto.getRunTime();
        walkTime = editBoardDto.getWalkTime();
        time = editBoardDto.getTime();
        repeat = editBoardDto.getRepeat();
        totalMember = editBoardDto.getTotalMember();
        playTime = editBoardDto.getPlayTime();
        status = editBoardDto.getStatus();
        guests = editBoardDto.getGuests();
    }

    public void increase(){
        joinMember++;
    }
}
