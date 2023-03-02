package by.itstep.organizaer.exceptions;

public class CityNotFoundException extends RuntimeException{

    public CityNotFoundException(String city) {
        super(String.format("Город %s не найден", city));
    }
}
