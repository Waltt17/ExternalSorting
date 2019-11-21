import student.TestCase;

/**
 * @author Waltt17
 *
 */
public class KeyValueTest extends TestCase {
    private KeyValue test;


    /**
     * set up
     */
    public void setUp() {
        test = new KeyValue(1, 1.1);
    }


    /**
     * test get double
     */
    public void testGetDouble() {
        assertEquals(1.1, test.getDouble(), 0);
    }


    /**
     * test get long
     */
    public void testGetLong() {
        assertEquals(1, test.getLong());
    }


    /**
     * test set double
     */
    public void testSetDouble() {
        test.setDouble(2.2);
        assertEquals(2.2, test.getDouble(), 0);
    }


    /**
     * test set long
     */
    public void testSetLong() {
        test.setLong(2);
        assertEquals(2, test.getLong());
    }


    /**
     * test tostring
     */
    public void testToString() {
        assertEquals("1 1.1", test.toString());
    }
}
