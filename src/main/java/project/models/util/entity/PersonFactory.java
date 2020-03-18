package project.models.util.entity;

import project.models.Person;
import project.models.enums.MessagesPermission;

import java.text.SimpleDateFormat;
import java.util.Date;

//Класс создания пользователя для тестирования.
public class PersonFactory {

    private Person person;

    public PersonFactory() throws Exception {
        String birth ="31/12/1998";
        Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(birth);
        person = new Person();
        person.setId(1);
        person.setFirstName("Сергей");
        person.setLastName("Рухлядко");
        person.setRegDate(new Date());
        person.setBirthDate(birthDate);
        person.setEmail("cer@gmail.com");
        person.setPhone("89651995568");
        person.setPhoto("https://cdn.pixabay.com/photo/2018/02/12/10/45/heart-3147976_960_720.jpg");
        person.setAbout("Все норм");
        person.setCity("Moscow");
        person.setCountry("Russia");
        person.setMessagesPermission(MessagesPermission.ALL);
        person.setLastOnlineTime(new Date());
        person.setBlocked(false);
    }

    public Person getPerson() {
        return person;
    }
}
