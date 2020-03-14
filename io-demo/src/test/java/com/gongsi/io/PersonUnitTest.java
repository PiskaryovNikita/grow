package com.gongsi.io;

import com.gongsi.Address;
import com.gongsi.Employee;
import com.gongsi.Person;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersonUnitTest {
    private static final String OUTPUT_FILE = "output.txt";

    @Test
    public void whenSerializeAndDeserializeThenObjectIsTheSame() throws IOException, ClassNotFoundException {
        final Person person = new Person();
        person.setAge(20);
        person.setName("Joe");

        try (final FileOutputStream fileOutputStream = new FileOutputStream(OUTPUT_FILE);
             final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(person);
            objectOutputStream.flush();
        }

        final Person deserializePerson;
        try (final FileInputStream fileInputStream = new FileInputStream(OUTPUT_FILE);
             final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            deserializePerson = (Person) objectInputStream.readObject();
        }

        assertEquals(deserializePerson.getAge(), person.getAge());
        assertEquals(deserializePerson.getName(), person.getName());
    }

    @Test
    public void whenCustomSerializeAndDeserializeThenObjectIsTheSame() throws IOException, ClassNotFoundException {
        final Person person = new Person();
        person.setAge(20);
        person.setName("Joe");

        final Address address = new Address();
        address.setHouseNumber(1);

        final Employee employee = new Employee();
        employee.setPerson(person);
        employee.setAddress(address);

        try (final FileOutputStream fileOutputStream = new FileOutputStream(OUTPUT_FILE);
             final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(employee);
            objectOutputStream.flush();
        }

        final Employee deserializeEmployee;
        try (final FileInputStream fileInputStream = new FileInputStream(OUTPUT_FILE);
             final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            deserializeEmployee = (Employee) objectInputStream.readObject();
        }

        assertEquals(deserializeEmployee.getPerson().getAge(), employee.getPerson().getAge());
        assertEquals(deserializeEmployee.getAddress().getHouseNumber(), (employee.getAddress().getHouseNumber()));
    }

}
