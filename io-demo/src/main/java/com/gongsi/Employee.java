package com.gongsi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient Address address;
	private Person person;

	public Address getAddress() {
		return address;
	}

	public void setAddress(final Address address) {
		this.address = address;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(final Person person) {
		this.person = person;
	}

	private void writeObject(final ObjectOutputStream outputStream) throws IOException {
		outputStream.defaultWriteObject();
		outputStream.writeObject(address.getHouseNumber());
	}

	private void readObject(final ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
		inputStream.defaultReadObject();
		final Integer houseNumber = (Integer) inputStream.readObject();
		final Address address = new Address();
		address.setHouseNumber(houseNumber);
		setAddress(address);
	}

}
