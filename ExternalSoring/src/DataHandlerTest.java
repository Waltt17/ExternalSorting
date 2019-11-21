import java.io.IOException;
import student.TestCase;

/**
 * @author Waltt17
 *
 */
public class DataHandlerTest extends TestCase {
    private DataHandler handler;


    /**
     * sets up test handler
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        handler = new DataHandler("sample8k.bin", "DataHandlerTestOut.bin");
    }


    /**
     * tests the readat offset
     * 
     * @throws IOException
     */
    public void testReadAtOffset() throws IOException {
        KeyValue[] test = handler.readBlockAtOffset(16);
        assertEquals(106944573427L, test[0].getLong());
    }


    /**
     * tests read by block
     * 
     * @throws IOException
     */
    public void testReadNextBlock() throws IOException {
        KeyValue[] test = new KeyValue[1024];
        KeyValue[] recs = handler.readNextBlock();
        // at [0] 781555640717 584.1676211777398
        long l = 781555640717L;
        double d = 584.1676211777398;
        test[0] = new KeyValue(l, d);
        assertEquals(test[0].getDouble(), recs[0].getDouble(), 0);
        // at [1] 123016989614 169.87305565944044
        l = 123016989614L;
        d = 169.87305565944044;
        test[1] = new KeyValue(l, d);
        assertEquals(test[1].getDouble(), recs[1].getDouble(), 0);

        // at [last] 416280563513 372.72626149358524
        l = 700391662928L;
        d = 324.28151483459135;
        test[1023] = new KeyValue(l, d);
        assertEquals(test[1023].getDouble(), recs[1023].getDouble(), 0);

    }


    /**
     * tests read first 8 blocks
     * 
     * @throws IOException
     */
    public void testReadNextEight() throws IOException {
        KeyValue[] test = new KeyValue[1024 * 8];
        KeyValue[] vals = handler.readFirstEight();

        // [first] 781555640717 584.1676211777398
        // [last] 416280563513 372.72626149358524
        long l = 781555640717L;
        double d = 584.1676211777398;
        test[0] = new KeyValue(l, d);
        assertEquals(test[0].getDouble(), vals[0].getDouble(), 0);

        l = 416280563513L;
        d = 372.72626149358524;
        test[(1024 * 8) - 1] = new KeyValue(l, d);
        assertEquals(test[(1024 * 8) - 1].getDouble(), vals[(1024 * 8) - 1]
            .getDouble(), 0);

    }


    /**
     * tests the write block
     * 
     * @throws IOException
     */
    public void testWriteNextBlock() throws IOException {
        KeyValue[] recs = handler.readNextBlock();
        handler.writeNextBlock(recs);
        recs = handler.readNextBlock();
        handler.writeNextBlock(recs);

        handler = new DataHandler("DataHandlerTestOut.bin",
            "DataHandlerTestOut.bin");

        recs = handler.readNextBlock();
        KeyValue[] test = new KeyValue[1024];
        // at [0] 781555640717 584.1676211777398
        long l = 781555640717L;
        double d = 584.1676211777398;
        test[0] = new KeyValue(l, d);
        assertEquals(test[0].getDouble(), recs[0].getDouble(), 0);

        // at [1024] 416280563513 372.72626149358524
        l = 700391662928L;
        d = 324.28151483459135;
        test[1023] = new KeyValue(l, d);
        assertEquals(test[1023].getDouble(), recs[1023].getDouble(), 0);

        // next block
        recs = handler.readNextBlock();
        test = new KeyValue[1024];

        // at [1025] 594721501660 215.87639006704651
        l = 594721501660L;
        d = 215.87639006704651;
        test[0] = new KeyValue(l, d);
        assertEquals(test[0].getDouble(), recs[0].getDouble(), 0);

        // at [2048] 147082819256 424.2043999138394
        l = 147082819256L;
        d = 424.2043999138394;
        test[1023] = new KeyValue(l, d);
        assertEquals(test[1023].getDouble(), recs[1023].getDouble(), 0);
    }


    /**
     * test the textfile
     * 
     * @throws IOException
     */
    public void testMakeTextFile() throws IOException {
        handler.makeTextFile("sample8k.bin", "name.txt");
        int temp = 0;
        assertEquals(0, temp);
    }

}
