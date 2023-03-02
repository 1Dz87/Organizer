package by.itstep.organizaer.service;

import by.itstep.organizaer.exceptions.FriendGroupNotFoundException;
import by.itstep.organizaer.exceptions.UserNotFoundException;
import by.itstep.organizaer.model.entity.FriendGroup;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.repository.FriendGroupRepository;
import by.itstep.organizaer.repository.FriendRepository;
import by.itstep.organizaer.repository.UserRepository;
import by.itstep.organizaer.utils.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class FriendGroupService {

    FriendGroupRepository friendGroupRepository;

    FriendRepository friendRepository;

    UserRepository userRepository;

    @Transactional
    public Long createFriendGroup(String name){
        User user = SecurityUtil.getCurrentUser()
                .orElseThrow(()-> new UserNotFoundException(" Authorization error"));
        FriendGroup group = FriendGroup.builder().name(name).build();
        user.addFriendGroup(group);
        userRepository.save(user);
        return group.getId();
    }

    @Transactional
    public String putFriendsToGroup(List<Long> friendIds, Long groupId) {
        try {
            FriendGroup group = friendGroupRepository.findById(groupId)
                    .orElseThrow(() -> new FriendGroupNotFoundException(groupId));
            friendRepository.saveAll(friendRepository.findAllById(friendIds)
                    .stream()
                    .peek(friend -> friend.setFriendGroup(group))
                    .collect(Collectors.toList()));
            return "Готово";
        } catch (FriendGroupNotFoundException e) {
            return e.getMessage();
        }
    }
}
