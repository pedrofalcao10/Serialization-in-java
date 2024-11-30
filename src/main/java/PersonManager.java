import java.util.ArrayList;
import java.util.List;

public class PersonManager {
    // Generate a list of Serialization_TCP_P2C_in_java.src.Person objects
    public static List<Person> generatePersons(int numPersons) {
        List<Person> personList = new ArrayList<>();

        for (int i = 0; i < numPersons; i++) {
            personList.add(new Person("Person" + (i + 1), "Location" + (i + 1), 20 + i));
        }

        return personList;
    }
}