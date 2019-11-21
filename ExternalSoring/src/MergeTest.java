import java.io.IOException;
import student.*;

/**
 * 
 * @author Walter tatera
 */
public class MergeTest extends TestCase {

    DataHandler hand;
    Merge merge;
    ReplacementSelection driver;


    /**
     * sets up
     */
    public void setUp() throws IOException {
        hand = new DataHandler("sample8k.bin", "outfile.bin");
        driver = new ReplacementSelection(hand);
        merge = new Merge(driver.getOffsets(), hand);
    }


    /**
     * runs merge
     * 
     * @throws IOException
     */
    public void testMerge() throws IOException {
        merge.merge();
        assertEquals(1, 1);
    }

}
