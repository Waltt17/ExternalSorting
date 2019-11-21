import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DataHandler {

    private RandomAccessFile inFile;
    private RandomAccessFile outFile;
    private String inFileName;
    private String outFileName;
    private FileChannel inChannel;
    private FileChannel outChannel;


    /**
     * constructor
     * 
     * @param scoreFile
     *            files for ascores
     * @param outFile
     *            file for output
     * @throws IOException
     */
    public DataHandler(String scoreFile, String outFile) throws IOException {
        this.inFile = new RandomAccessFile(scoreFile, "r");
        this.outFile = new RandomAccessFile(outFile, "rw");
        this.inFileName = scoreFile;
        this.outFileName = outFile;
        inChannel = inFile.getChannel();
        outChannel = this.outFile.getChannel();
    }


    /**
     * reads the next block of data
     * 
     * @return the keyvalue array of the block
     * @throws IOException
     */
    public KeyValue[] readNextBlock() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16384); // Allocates for 1 block
        KeyValue[] records = new KeyValue[1024];
        inChannel.read(buffer);
        buffer.flip();

        int count = 0;
        for (int i = 0; i < buffer.limit(); i += 16) {
            // added count++ cause it was adding to 0 position every time
            records[count] = new KeyValue(buffer.getLong(), buffer.getDouble());
            count++;
        }
        return records;
    }


    /**
     * 
     * @param offset
     *            Offset by number of records
     * @return
     * @throws IOException
     */
    public KeyValue[] readBlockAtOffset(long offset) throws IOException {
        offset *= 16;
        ByteBuffer buffer = ByteBuffer.allocate(16384); // Allocates for 1 block
        KeyValue[] records = new KeyValue[1024];
        inChannel.read(buffer, offset);
        buffer.flip();
        int count = 0;
        for (int i = 0; i < buffer.limit(); i += 16) {
            // added count++ cause it was adding to 0 position every time
            records[count] = new KeyValue(buffer.getLong(), buffer.getDouble());
            count++;
        }
        return records;
    }


    /**
     * writes to a data file the block
     * 
     * @param arr
     *            the keyvalue array to be written
     * @return the long of the block location
     * @throws IOException
     */
    public long writeNextBlock(KeyValue[] arr) throws IOException {
        int writeCount = 0;
        ByteBuffer buffer = ByteBuffer.allocate(16384); // Allocates for 1 block
        for (int i = 0; i < arr.length; i++) {
            KeyValue temp = arr[i];
            if (temp != null) {
                buffer.putLong(temp.getLong());
                buffer.putDouble(temp.getDouble());
                writeCount++;
            }
        }
        buffer.flip();
        outChannel.write(buffer);
        return writeCount;
    }


    /**
     * reads the first eight blocks
     * 
     * @return the keyvalue array of the blocks
     * @throws IOException
     */
    public KeyValue[] readFirstEight() throws IOException {
        // Read data from file
        ByteBuffer buffer = ByteBuffer.allocate(8 * 16384); // Allocates for 8
                                                            // blocks
        inChannel.read(buffer);
        buffer.flip();

        KeyValue[] records = new KeyValue[1024 * 8];
        for (int i = 0; i < records.length; i++) {
            records[i] = new KeyValue(buffer.getLong(), buffer.getDouble());
        }
        return records;
    }


    /**
     * closes the files
     * 
     * @throws IOException
     */
    public void closeFiles() throws IOException {
        inFile.close();
        inChannel.close();
        outFile.close();
        outChannel.close();
        inFile = null;
        inChannel = null;
        outFile = null;
        outChannel = null;
    }


    /**
     * swaps input and output
     * 
     * @throws IOException
     */
    public void swapInOut() throws IOException {
        inFile.close();
        inChannel.close();
        outFile.close();
        outChannel.close();

        this.inFile = new RandomAccessFile(outFileName, "r");
        this.outFile = new RandomAccessFile(inFileName, "rw");
        String temp = this.inFileName;
        this.inFileName = this.outFileName;
        this.outFileName = temp;
        inChannel = inFile.getChannel();
        outChannel = this.outFile.getChannel();
    }


    /**
     * returns the output file
     * 
     * @return output file
     */
    public String getOutFile() {
        return this.outFileName;
    }


    /**
     * makes a text file of a given binary file
     * 
     * @param binFile
     *            binary file to be read
     * @param textFileName
     *            the textfile name
     * @throws IOException
     */
    public void makeTextFile(String binFile, String textFileName)
        throws IOException {
        File file = new File(textFileName);

        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter pw = new PrintWriter(file);
        RandomAccessFile rand = new RandomAccessFile(binFile, "r");
        FileChannel filChan = rand.getChannel();
        ByteBuffer buff = ByteBuffer.allocate((int)filChan.size());

        filChan.read(buff);
        buff.flip();

        for (int i = 0; i < buff.limit(); i += 16) {
            long l = buff.getLong();
            double d = buff.getDouble();
            pw.println(Double.toString(d) + " " + Long.toString(l));
        }
        pw.close();
        rand.close();
        filChan.close();
    }

}
