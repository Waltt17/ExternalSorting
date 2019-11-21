import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import student.TestCase;

/**
 * @author Waltt17
 *
 */

public class ExternalSortingTest extends TestCase {
    /**
     * sets up
     */
    public void setUp() {
        // does nothing
    }


    /**
     * test merge of 512
     * 
     * @throws IOException
     */
    public void testMerge512() throws IOException {
        createRandom(512);
        boolean shouldRename = false;
        if (!shouldRename) {
            String scoreFile = "random512Blocks.bin";
            DataHandler hand = new DataHandler(scoreFile, "out.bin");
            ArrayList<Long> offsets = replacementSelection(hand);
            Merge merge = new Merge(offsets, hand);
            merge.merge();
            hand.closeFiles();
            shouldRename = hand.getOutFile().equals(scoreFile);
        }
        if (shouldRename) {
            File file = new File("out.bin");
            File newFile = new File("random512Blocks.bin");
            newFile.delete();
            file.renameTo(newFile);
        }

        makeTextFile("random512Blocks.bin", "MainOut512.txt");
    }


    /**
     * test merge of 1000
     * 
     * @throws IOException
     */
    public void testMerge1000() throws IOException {
        createRandom(1000);
        createRandom(999);
        boolean shouldRename = false;
        if (!shouldRename) {
            String scoreFile = "random1000Blocks.bin";
            DataHandler hand = new DataHandler(scoreFile, "out.bin");
            ArrayList<Long> offsets = replacementSelection(hand);
            Merge merge = new Merge(offsets, hand);
            merge.merge();
            hand.closeFiles();
            shouldRename = hand.getOutFile().equals(scoreFile);
        }
        if (shouldRename) {
            File file = new File("out.bin");
            File newFile = new File("random1000Blocks.bin");
            newFile.delete();
            file.renameTo(newFile);
        }

        makeTextFile("random1000Blocks.bin", "MainOut1000.txt"); // original
                                                                 // file
    }


    /**
     * test the merge of reversed 512
     * 
     * @throws IOException
     */
    public void testReversedMerge512() throws IOException {
        createReverse(512);
        boolean shouldRename = false;
        if (!shouldRename) {
            String scoreFile = "reverse512Blocks.bin";
            DataHandler hand = new DataHandler(scoreFile, "out.bin");
            ArrayList<Long> offsets = replacementSelection(hand);
            Merge merge = new Merge(offsets, hand);
            merge.merge();
            hand.closeFiles();
            shouldRename = hand.getOutFile().equals(scoreFile);
        }
        if (shouldRename) {
            File file = new File("out.bin");
            File newFile = new File("reverse512Blocks.bin");
            newFile.delete();
            file.renameTo(newFile);
        }

        makeTextFile("reverse512Blocks.bin", "MainOutReverse.txt"); // original
                                                                    // file
    }


    /**
     * does the replacement selection
     * 
     * @param hand
     *            data handler
     * @return the offsets
     * @throws IOException
     */
    public static ArrayList<Long> replacementSelection(DataHandler hand)
        throws IOException {
        ReplacementSelection driver = new ReplacementSelection(hand);
        driver.firstPass();
        hand.swapInOut();
        return driver.getOffsets();
    }


    /**
     * reates a reversed file
     * 
     * @param numBlocks
     *            number of reversed blocks
     * @throws IOException
     */
    public void createReverse(int numBlocks) throws IOException {
        RandomAccessFile outFile = new RandomAccessFile("reverse" + numBlocks
            + "Blocks.bin", "rw");
        FileChannel outChannel = outFile.getChannel();
        // Allocates for numBlocks block
        ByteBuffer buffer = ByteBuffer.allocate(16384 * numBlocks);
        for (int j = 16384 * numBlocks; j > 0; j -= 16) {
            buffer.putLong(ThreadLocalRandom.current().nextLong(100000000000L,
                999999999999L));
            buffer.putDouble(j);
        }
        buffer.flip();
        outChannel.write(buffer);
        outFile.close();
        makeTextFile("reverse" + numBlocks + "Blocks.bin", "reverse" + numBlocks
            + "Blocks.txt");
    }


    /**
     * creates a random file
     * 
     * @param numBlocks
     *            number of blocks
     * @throws IOException
     */
    public void createRandom(int numBlocks) throws IOException {
        RandomAccessFile outFile = new RandomAccessFile("random" + numBlocks
            + "Blocks.bin", "rw");
        FileChannel outChannel = outFile.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(16384 * numBlocks); // Allocates
                                                                    // for 1
                                                                    // block
        for (int j = 0; j < 16383 * numBlocks; j += 16) {
            buffer.putLong(ThreadLocalRandom.current().nextLong(100000000000L,
                999999999999L));
            buffer.putDouble(ThreadLocalRandom.current().nextDouble(0, 999));
        }
        buffer.flip();
        outChannel.write(buffer);
        outFile.close();
        makeTextFile("random" + numBlocks + "Blocks.bin", "random" + numBlocks
            + "Blocks.txt");
    }


    /**
     * makes a textfile of a given binary
     * 
     * @param binFile
     *            binary file
     * @param textFileName
     *            name of the textfile to be made
     * @throws IOException
     */
    private void makeTextFile(String binFile, String textFileName)
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
            pw.println(Long.toString(l) + " " + Double.toString(d));
        }
        pw.close();
        rand.close();
        filChan.close();
    }

}
