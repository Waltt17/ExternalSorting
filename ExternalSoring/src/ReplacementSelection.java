import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Waltt17
 */
public class ReplacementSelection {
    private KeyValue[] inputBuffer;
    private KeyValue[] outputBuffer;
    private DataHandler dataHandler;
    private ArrayList<Long> offsets;
    private long currRunCount;
    private MaxHeap heap;

    private int outCurr;
    private int inCurr;


    /**
     * constructor
     * 
     * @param hand
     *            the datahandler
     * @throws IOException
     */
    public ReplacementSelection(DataHandler hand) throws IOException {
        dataHandler = hand;
        heap = new MaxHeap(dataHandler.readFirstEight());
        inputBuffer = dataHandler.readNextBlock();
        outputBuffer = new KeyValue[1024];
        offsets = new ArrayList<Long>();
        currRunCount = 0;
        outCurr = 0;
        inCurr = 0;
    }


    /**
     * gets the offsets
     * 
     * @return the arraylist of offsets
     */
    public ArrayList<Long> getOffsets() {
        return offsets;
    }


    /**
     * the first pass of replacement selection
     * 
     * @throws IOException
     */
    public void firstPass() throws IOException {
        while (heap.heapSize() != 0 || heap.listSize() != 0) {
            while (!heap.isEmpty()) {
                if (outCurr == outputBuffer.length) {
                    writeOutput();
                    outCurr = 0;
                }
                if (inCurr == inputBuffer.length
                    || inputBuffer[inCurr] == null) {
                    inputBuffer = dataHandler.readNextBlock();
                    inCurr = 0;
                }
                if (inputBuffer[inCurr] == null) {
                    outputBuffer[outCurr] = heap.pop();
                    outCurr++;
                }
                else {
                    outputBuffer[outCurr] = heap.pop();
                    outCurr++;
                    heap.add(inputBuffer[inCurr]);
                    inCurr++;
                }
            }
            this.endCurrRun();
        }
    }


    /**
     * Writes the output file to disk and clears it
     * 
     * @throws IOException
     */
    public void writeOutput() throws IOException {
        currRunCount += dataHandler.writeNextBlock(outputBuffer);
        outputBuffer = new KeyValue[1024];
    }


    /**
     * Ends the current run, clears buffer, and records the offset
     * 
     * @throws IOException
     */
    public void endCurrRun() throws IOException {
        this.writeOutput();
        offsets.add(currRunCount);
        heap.rebuildHeap();
    }

}
