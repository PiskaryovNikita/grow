package com.gongsi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

public class DeserializationUtility {

    public static Object deserializeObjectFromString(final String s) throws IOException, ClassNotFoundException {
        final byte[] data = Base64.getDecoder().decode(s);
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        final Object o = ois.readObject();
        ois.close();
        return o;
    }

}