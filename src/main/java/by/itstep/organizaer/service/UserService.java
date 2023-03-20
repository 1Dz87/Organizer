package by.itstep.organizaer.service;

import by.itstep.organizaer.exceptions.UserAlreadyExistsException;
import by.itstep.organizaer.model.dto.RegistrationRequest;
import by.itstep.organizaer.model.dto.UserDto;
import by.itstep.organizaer.model.entity.Authority;
import by.itstep.organizaer.model.entity.Contacts;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.model.mapping.UserMapper;
import by.itstep.organizaer.repository.UserRepository;
import by.itstep.organizaer.service.handler.UserCreationHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    UserRepository userRepository;

    UserMapper userMapper;

    UserCreationHandler userCreationHandler;

    FriendService friendService;

    public UserDto createUser(RegistrationRequest request) {
        User userToSave = userMapper.registrationToEntity(request);
        userToSave.setAuthorities(request
                .getRoles()
                .stream()
                .map(role -> Authority
                        .builder()
                        .authority(role)
                        .orgUser(userToSave)
                        .build())
                .collect(Collectors.toList()));
        userToSave.setContacts(Contacts.builder()
                .phone(request.getPhone())
                .email(Optional.ofNullable(request.getEmail())
                        .map(List::of)
                        .orElse(List.of()))
                .build());
        final UserDto result = userMapper.toDto(userCreationHandler.doCreate(userToSave));
        friendService.updateFriendWithUuid(result.getId());
        return result;
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Не верное имя пользователя или пароль"));
    }

    public Optional<User> getById(final Long id) {
        return userRepository.findById(id);
    }
}
