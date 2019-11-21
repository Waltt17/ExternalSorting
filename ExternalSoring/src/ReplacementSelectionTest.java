import java.io.IOException;
import java.util.ArrayList;
import student.TestCase;

/**
 * @author Waltt17
 *
 */
public class ReplacementSelectionTest extends TestCase {
    private ReplacementSelection driver;


    /**
     * sets up
     */
    public void setUp() throws IOException {
        // do nothing
        driver = new ReplacementSelection(new DataHandler("sample8k.bin",
            "outfile.bin"));
    }


    public void test() throws IOException {
        ArrayList<Long> temp = driver.getOffsets();
        assertEquals(temp.size(), 0);
        driver.firstPass();
        driver.endCurrRun();
        assertEquals(1, 1);
    }

}
