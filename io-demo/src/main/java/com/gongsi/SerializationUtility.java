package com.gongsi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class SerializationUtility {

    public static String serializeObjectToString(final Serializable o) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

}