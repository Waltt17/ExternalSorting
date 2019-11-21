
/**
 * @author Walter
 */
public class KeyValue {
    private double key;
    private long value;


    /**
     * constuctor for keyvalue
     * 
     * @param l
     *            the key
     * @param dub
     *            the value
     */
    public KeyValue(long l, double dub) {
        key = dub;
        value = l;
    }


    /**
     * returns the key
     * 
     * @return the key
     */
    public double getDouble() {
        return key;
    }


    /**
     * returns the value
     * 
     * @return the value
     */
    public long getLong() {
        return value;
    }


    /**
     * sets the key
     * 
     * @param dub
     *            key to be set
     */
    public void setDouble(double dub) {
        key = dub;
    }


    /**
     * sets the value
     * 
     * @param l
     *            the value to be set
     */
    public void setLong(long l) {
        value = l;
    }


    /**
     * to string of the keyvalue class
     * 
     * @return the tostring
     */
    public String toString() {
        return value + " " + key;
    }
}
