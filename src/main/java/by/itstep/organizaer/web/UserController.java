package by.itstep.organizaer.web;

import by.itstep.organizaer.model.dto.FriendDto;
import by.itstep.organizaer.model.dto.UserDto;
import by.itstep.organizaer.service.FriendGroupService;
import by.itstep.organizaer.service.FriendService;
import by.itstep.organizaer.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    UserService userService;
    FriendGroupService friendGroupService;

    FriendService friendService;

    @PutMapping("/putFriend")
    public ResponseEntity<FriendDto> createFriend(@RequestBody @Valid FriendDto friendDto){
        return ResponseEntity.ok(friendService.createFriend(friendDto));

    }
    @PostMapping("/createGroup")
    public ResponseEntity<Long> createFriendGroup(@RequestParam String name){
        return ResponseEntity.ok(friendGroupService.createFriendGroup(name));
    }
    @PutMapping("/putFriendsToGroup")
    public ResponseEntity<String> putFriendsToGroup(@RequestBody List<Long> friendIds, @RequestParam Long groupId){
        return ResponseEntity.ok(friendGroupService.putFriendsToGroup(friendIds,groupId));
    }

}
