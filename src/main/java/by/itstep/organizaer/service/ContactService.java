package by.itstep.organizaer.service;

import by.itstep.organizaer.exceptions.UserNotFoundException;
import by.itstep.organizaer.model.entity.Contacts;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final UserRepository repository;

    public Contacts createUserContact(Long userId, Contacts contacts) {
        return repository.findById(userId)
                .map(user -> {
                    user.setContacts(contacts);
                    return repository.save(user);
                })
                .map(User::getContacts)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public Contacts createFriendContact(Long friendId, Contacts contacts) {
        return null;
    }
}
