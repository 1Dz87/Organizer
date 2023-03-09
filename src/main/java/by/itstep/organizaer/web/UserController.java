package by.itstep.organizaer.web;

import by.itstep.organizaer.model.dto.FriendDto;
import by.itstep.organizaer.model.dto.UserDto;
import by.itstep.organizaer.service.FriendGroupService;
import by.itstep.organizaer.service.FriendService;
import by.itstep.organizaer.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Операции пользователя", description = "API операций с пользователем")
@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    UserService userService;
    FriendGroupService friendGroupService;

    FriendService friendService;

    @Operation(description = "Создать объект \"ДРУГ\" для текущего пользователя",
    responses = {
            @ApiResponse(responseCode = "404", description = "Текущий пользователь не авторизован или удален из базы данных"),
            @ApiResponse(responseCode = "200", description = "Объект \"Друг\" успешно добавлен для текущего пользователя")
    })
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
