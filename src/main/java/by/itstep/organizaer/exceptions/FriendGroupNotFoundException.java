package by.itstep.organizaer.exceptions;

public class FriendGroupNotFoundException extends RuntimeException{

    public FriendGroupNotFoundException(Long id) {
        super(String.format("Группа %d не найдена", id));
    }
}
