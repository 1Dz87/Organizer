package by.itstep.organizaer.service;

import by.itstep.organizaer.exceptions.UserNotFoundException;
import by.itstep.organizaer.model.dto.ContactsDto;
import by.itstep.organizaer.model.dto.FriendDto;
import by.itstep.organizaer.model.entity.Contacts;
import by.itstep.organizaer.model.entity.Friend;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.model.mapping.FriendMapper;
import by.itstep.organizaer.repository.FriendRepository;
import by.itstep.organizaer.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class FriendServiceTest {

    private AutoCloseable autoCloseable;

    @Mock
    private FriendRepository friendRepository;

    private FriendMapper friendMapper;

    @Mock
    private UserRepository userRepository;

    private FriendService friendService;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        this.friendMapper = Mappers.getMapper(FriendMapper.class);
        this.friendService = new FriendService(friendRepository, friendMapper, userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @DisplayName(value = "Должно возникнуть исключение UserNotFoundException")
    void createFriend_exception() {
        UUID uuid = UUID.randomUUID();
        ContactsDto contactsDto = ContactsDto
                .builder()
                .phone("+465487134")
                .build();
        FriendDto dto = FriendDto
                .builder()
                .name("test")
                .uuid(uuid)
                .contacts(contactsDto)
                .build();
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(null, null));
        SecurityContextHolder.setContext(context);
        when(userRepository.findByPhone(dto.getContacts().getPhone()))
                .thenReturn(Optional.of(User.builder().uuid(uuid).build()));

        assertThrows(UserNotFoundException.class, () -> friendService.createFriend(dto));
    }

    @Test
    @DisplayName(value = "UUID объекта заполняется верно")
    void createFriend_setUuid_ok() {
        // Заполнение параметров теста
        UUID uuid = UUID.randomUUID();
        ContactsDto contactsDto = ContactsDto
                .builder()
                .phone("+465487134")
                .build();
        FriendDto dto = FriendDto
                .builder()
                .name("test")
                .contacts(contactsDto)
                .build();
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(User.builder().uuid(uuid).build(), null));
        SecurityContextHolder.setContext(context);

        // Определение поведения моков
        when(userRepository.findByPhone(dto.getContacts().getPhone()))
                .thenReturn(Optional.of(User.builder().uuid(uuid).build()));
        when(friendRepository.save(ArgumentMatchers.argThat(friend ->
                        friend.getUuid() != null && friend.getUuid().equals(uuid)
                        && friend.getName().equals("test")
                        && friend.getContacts().getPhone().equals("+465487134"))))
                .thenReturn(Friend.builder().name("test").uuid(uuid).contacts(Contacts.builder().phone("+465487134").build()).build());

        // Вызов тестируемого метода
        FriendDto result = friendService.createFriend(dto);

        // Проверка результата выполнения метода
        assertNotNull(result.getUuid());
        assertNotNull(result.getContacts());
        assertEquals(uuid, result.getUuid());
        assertEquals("test", result.getName());
        assertEquals("+465487134", result.getContacts().getPhone());
    }

    @Test
    @DisplayName(value = "UUID должен быть null")
    void createFriend_NoUUID(){
        UUID uuid = UUID.randomUUID();
        ContactsDto contactsDto = ContactsDto
                .builder()
                .phone("+465487134")
                .build();
        FriendDto dto = FriendDto
                .builder()
                .name("test")
                .contacts(contactsDto)
                .build();
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(User.builder().uuid(uuid).build(), null));
        SecurityContextHolder.setContext(context);

        // Определение поведения моков
        when(userRepository.findByPhone(dto.getContacts().getPhone()))
                .thenReturn(Optional.empty());
        when(friendRepository.save(ArgumentMatchers.argThat(friend ->
                friend.getUuid() == null
                        && friend.getName().equals("test")
                        && friend.getContacts().getPhone().equals("+465487134"))))
                .thenReturn(Friend.builder().name("test").contacts(Contacts.builder().phone("+465487134").build()).build());

        // Вызов тестируемого метода
        FriendDto result = friendService.createFriend(dto);

        // Проверка результата выполнения метода
        assertNull(result.getUuid());
        assertNotNull(result.getContacts());
        assertEquals("test", result.getName());
        assertEquals("+465487134", result.getContacts().getPhone());
    }
}
