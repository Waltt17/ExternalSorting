import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class ExternalSorting {

    public static void main(String[] args) throws IOException {
        boolean shouldRename = false; // THIS IS OURWORKING MAIN!!!!
        if (!shouldRename) {
            String scoreFile = args[0];
            DataHandler hand = new DataHandler(scoreFile, "out.bin");
            ArrayList<Long> offsets = replacementSelection(hand);
            Merge merge = new Merge(offsets, hand);
            merge.merge();
            hand.closeFiles();
            shouldRename = hand.getOutFile().equals(scoreFile);
        }
        if (shouldRename) {
            File file = new File("out.bin");
            File newFile = new File(args[0]);
            newFile.delete();
            file.renameTo(newFile);
        }
    }


    /**
     * does the replacement selection
     * 
     * @param hand
     *            data handler
     * @return the arraylist of offsets
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
     * makes a text file of a given binary file
     * 
     * @param binFile
     *            binary file to be read
     * @param textFileName
     *            the textfile name
     * @throws IOException
     */
    public static void makeTextFile(String binFile, String textFileName)
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
