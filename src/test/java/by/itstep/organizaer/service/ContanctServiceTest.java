package by.itstep.organizaer.service;

import by.itstep.organizaer.constants.StringConstants;
import by.itstep.organizaer.exceptions.UserNotFoundException;
import by.itstep.organizaer.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ContanctServiceTest {

    private AutoCloseable autoCloseable;

    @Mock
    private UserRepository userRepository;

    private ContactService contactService;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        this.contactService = new ContactService(userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void crateContact_exception() {
        Long userId = 12L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> contactService.createUserContact(userId, null));
        assertEquals(String.format(StringConstants.USER_NOT_FOUND_ERR_MSG_TEMPLATE, userId), ex.getMessage());
    }

}
