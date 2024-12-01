import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Person")
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String localization;
    private int age;
    private String numID; // Unique Identifier

    // Default constructor (required for Jackson)
    public Person() {}

    public Person(String name, String localization, int age) {
        this.name = name;
        this.localization = localization;
        this.age = age;
        this.numID = generateUniqueID();
    }

    // Generate a universally unique identifier (UUID)
    private String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public String getLocalization() {
        return localization;
    }

    public int getAge() {
        return age;
    }

    public String  getNumID() {
        return numID;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + getName() + '\'' +
                ", localization='" + getLocalization() + '\'' +
                ", age=" + getAge() +
                ", numID='" + getNumID() + '\'' +
                '}';
    }
}