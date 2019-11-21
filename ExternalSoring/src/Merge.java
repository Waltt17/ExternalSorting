import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author walt tatera
 */
public class Merge {

    private ArrayList<Long> totalRecordsInRun;
    private DataHandler handle;
    private KeyValue[] buffer;
    private int[] recordsRead;
    private KeyValue[] outputBuffer;
    private int[] indexTracker;
    private int outCurr;
    private ArrayList<Long> offsets;
    private long currRunCount;


    /**
     * constructor for the merge class
     * 
     * @param lis
     *            the listy of total records
     * @param hand
     *            the data handler
     */
    public Merge(ArrayList<Long> lis, DataHandler hand) {
        this.totalRecordsInRun = lis;
        handle = hand;
        buffer = new KeyValue[1024 * 8];
        recordsRead = new int[8];
        indexTracker = new int[8];
        outputBuffer = new KeyValue[1024];
        outCurr = 0;
        offsets = new ArrayList<Long>();
        currRunCount = 0;
    }


    /**
     * does the merging
     * 
     * @throws IOException
     */
    public void merge() throws IOException {
        while (totalRecordsInRun.size() > 8) {
            mergePass();
        }
        mergePass();
    }


    /**
     * first pass for merge
     * 
     * @throws IOException
     */
    public void mergePass() throws IOException {
        for (int runNum = 0; runNum < totalRecordsInRun.size(); runNum += 8) {

            fillBuffer(runNum);
            indexTracker = new int[8];
            KeyValue[] nextValues = new KeyValue[8];
            for (int j = runNum; j < runNum + 8; j++) { // Initial fill heap
                nextValues[j % 8] = getNextValue(j);
            }
            MaxHeap heap = new MaxHeap(nextValues);
            KeyValue popped;
            while ((popped = heap.pop()) != null) {

                for (int i = runNum; i < runNum + 8; i++) {
                    if (nextValues[i % 8] == popped) {
                        nextValues[i % 8] = getNextValue(i);
                        if (nextValues[i % 8] != null) {
                            heap.add(nextValues[i % 8]);
                        }
                        break;
                    }
                }
                addToOutput(popped);
            }
            writeOutput(); // here we need to end the current run
            indexTracker = new int[8];
            recordsRead = new int[8];
            buffer = new KeyValue[1024 * 8];
            offsets.add(currRunCount);
        }
        // End of current pass. Set up for next pass:
        this.totalRecordsInRun = offsets;
        handle.swapInOut();
        buffer = new KeyValue[1024 * 8];
        recordsRead = new int[8];
        indexTracker = new int[8];
        outputBuffer = new KeyValue[1024];
        outCurr = 0;
        offsets = new ArrayList<Long>();
        currRunCount = 0;
    }


    /**
     * adds keyvalue to the output
     * 
     * @param toAdd
     *            the keyvalye to be output
     * @throws IOException
     */
    private void addToOutput(KeyValue toAdd) throws IOException {
        if (outCurr == outputBuffer.length) {
            writeOutput();
            outCurr = 0;
        }
        outputBuffer[outCurr] = toAdd;
        outCurr++;
    }


    /**
     * Writes the output file to disk and clears it
     * 
     * @throws IOException
     */
    private void writeOutput() throws IOException {
        currRunCount += handle.writeNextBlock(outputBuffer);
        outputBuffer = new KeyValue[1024];
    }


    /**
     * Gets the next value from the buffer. Reads the next block if necessary
     * 
     * @param index
     * @return
     * @throws IOException
     */
    private KeyValue getNextValue(int index) throws IOException {
        if (indexTracker[index % 8] == 1024) {
            getNextBlock(index);
            indexTracker[index % 8] = 0;
        }
        int bufferIndex = ((index % 8) * 1024) + indexTracker[index % 8];
        if (bufferIndex >= buffer.length) {
            return null;
        }
        KeyValue ans = buffer[bufferIndex];
        indexTracker[index % 8]++;
        return ans;
    }


    /**
     * fills the buffer
     * 
     * @param startIndex
     *            index to be started at
     * @throws IOException
     */
    public void fillBuffer(int startIndex) throws IOException {
        int numRuns = totalRecordsInRun.size();
        for (int i = startIndex; i < Math.min(startIndex + 8, numRuns); i++) {
            getNextBlock(i);
        }
    }


    /**
     * Gets the next block for the index of the run
     * Fills with null when done reading everything from that run
     * The index must correspond to the run. (run 0 .... run 19)
     * 
     * @param index
     *            index of run to get next block of
     * @throws IOException
     */
    public void getNextBlock(int index) throws IOException {
        long fileOffset;
        if (index == 0) {
            fileOffset = recordsRead[index];
        }
        else {
            fileOffset = recordsRead[index % 8] + totalRecordsInRun.get(index
                - 1);
        }
        KeyValue[] temp = handle.readBlockAtOffset(fileOffset);

        int bufferIndex = ((index % 8) * 1024);
        int bufferIndexMax = bufferIndex + 1024;
        int count = recordsRead[index % 8];
        for (int j = 0; j < temp.length; j++) {
            if (count < totalRecordsInRun.get(index)) {
                if (bufferIndex < bufferIndexMax) {
                    buffer[bufferIndex] = temp[j];
                    bufferIndex++;
                    count++;
                }
            }
            else {
                if (bufferIndex < bufferIndexMax) {
                    buffer[bufferIndex] = null;
                    bufferIndex++;
                }
            }
        }
        recordsRead[index % 8] = count;
    }

}
