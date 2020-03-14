package com.gongsi.io;

import com.gongsi.AppleProduct;
import com.gongsi.DeserializationUtility;
import com.gongsi.SerializationUtility;
import java.io.IOException;
import java.io.InvalidClassException;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DeserializationUnitTest {
    private static long userDefinedSerialVersionUID = 1234567L;
    private static final String serializedObj =
            "rO0ABXNyABdjb20uZ29uZ3NpLkFwcGxlUHJvZHVjdAAAAAAHW80VAgADTAANaGVhZHBob25lUG9ydHQAEkxqYXZhL2xhbmcvU3RyaW5nO0wADWxpZ2h0bmluZ1BvcnRxAH4AAUwAD3RodW5kZXJib2x0UG9ydHEAfgABeHBwcHA=";

    /**
     * Tests the deserialization of the original "AppleProduct" (no exceptions are thrown)
     */
    @Test
    public void shouldDeserializeWhenCompatibleSerializedObjectSupplied() throws IOException, ClassNotFoundException {

        assertEquals(userDefinedSerialVersionUID, AppleProduct.getSerialVersionUID());

        final AppleProduct macBook = new AppleProduct();
        macBook.headphonePort = "headphonePort2020";
        macBook.thunderboltPort = "thunderboltPort2020";
        macBook.lightningPort = "lightningPort2020";

        final String serializedProduct = SerializationUtility.serializeObjectToString(macBook);

        final AppleProduct deserializeProduct =
                (AppleProduct) DeserializationUtility.deserializeObjectFromString(serializedProduct);

        assertTrue(deserializeProduct.headphonePort.equalsIgnoreCase(macBook.headphonePort));
        assertTrue(deserializeProduct.thunderboltPort.equalsIgnoreCase(macBook.thunderboltPort));
        assertTrue(deserializeProduct.lightningPort.equalsIgnoreCase(macBook.lightningPort));

    }

    /**
     * Tests the deserialization of the modified (non-compatible) "AppleProduct".
     * The test should result in an InvalidClassException being thrown.
     * <p>
     * Note: to run this test:
     * 1. Modify the value of the serialVersionUID identifier in AppleProduct.java.
     * So it's not eq to version of AppleProduct when it was serialized.
     * 2. Remove the @Ignore annotation
     * 3. Run the test individually (do not run the entire set of tests)
     * 4. Revert the changes made in 1 & 2 (so that you're able to re-run the tests successfully)
     */
    @Ignore
    @Test(expected = InvalidClassException.class)
    public void shouldThrowExceptionWhenDeserializeNonCompatibleSerializedObject() throws ClassNotFoundException,
            IOException {

        assertNotEquals(userDefinedSerialVersionUID, AppleProduct.getSerialVersionUID());
        DeserializationUtility.deserializeObjectFromString(serializedObj);
    }

}
