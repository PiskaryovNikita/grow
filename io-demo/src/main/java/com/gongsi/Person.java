package com.gongsi;

import java.io.Serializable;

public class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	private int age;
	private String name;

	public int getAge() {
		return age;
	}

	public void setAge(final int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
