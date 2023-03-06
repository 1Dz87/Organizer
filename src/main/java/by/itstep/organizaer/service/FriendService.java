package by.itstep.organizaer.service;

import by.itstep.organizaer.exceptions.UserNotFoundException;
import by.itstep.organizaer.model.dto.FriendDto;
import by.itstep.organizaer.model.entity.Friend;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.model.mapping.FriendMapper;
import by.itstep.organizaer.repository.FriendRepository;
import by.itstep.organizaer.repository.UserRepository;
import by.itstep.organizaer.utils.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FriendService {

    FriendRepository friendRepository;

    FriendMapper friendMapper;

    UserRepository userRepository;

    @Transactional
    public FriendDto createFriend(FriendDto friendDto){
        Friend friend = friendMapper.toEntity(friendDto);
        log.debug("Входящий запрос на создания объекта друга: {}", friendDto);
        Optional<User> userOptional = userRepository.findByPhone(friend.getContacts().getPhone());
        if(userOptional.isPresent()){
            friend.setUuid(userOptional.get().getUuid());
        }
        User currentUser = SecurityUtil.getCurrentUser().orElseThrow(() -> new AuthenticationServiceException("Ошибка авторизации"));
        friend.setUser(currentUser);
        friendRepository.save(friend);
        return friendMapper.toDto(friend);
    }

    @Transactional
    public void updateFriendWithUuid(Long userId) {
        userRepository.findById(userId)
                .map(user -> {
                    friendRepository.findByPhone(user.getContacts().getPhone()).forEach((friend) -> {
                        friend.setUuid(user.getUuid());
                        friendRepository.save(friend);
                    });
                    return user;
                })
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

}
