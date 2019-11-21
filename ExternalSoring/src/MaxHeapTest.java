import student.TestCase;

/**
 * @author Waltt17
 *
 */
public class MaxHeapTest extends TestCase {
    private MaxHeap heap;


    /**
     * set up
     */
    public void setUp() {
        KeyValue[] values = new KeyValue[6];
        values[0] = new KeyValue(0, 0);
        values[1] = new KeyValue(1, 1);
        values[2] = new KeyValue(2, 2);
        values[3] = new KeyValue(3, 3);
        values[4] = new KeyValue(4, 4);
        values[5] = new KeyValue(5, 5);
        heap = new MaxHeap(values);
    }


    /**
     * test pop
     */
    public void testPop() {
        assertEquals(5, heap.pop().getLong());
        assertEquals(4, heap.pop().getLong());
        assertEquals(3, heap.pop().getLong());
        assertEquals(2, heap.pop().getLong());
        assertEquals(1, heap.pop().getLong());
        assertEquals(0, heap.pop().getLong());
    }


    /**
     * test pop more
     */
    public void testPop2() {
        KeyValue[] values = new KeyValue[6];
        values[5] = new KeyValue(0, 0);
        values[4] = new KeyValue(1, 1);
        values[3] = new KeyValue(2, 2);
        values[2] = new KeyValue(3, 3);
        values[1] = new KeyValue(4, 4);
        values[0] = new KeyValue(5, 5);
        heap = new MaxHeap(values);
        assertEquals(5, heap.pop().getLong());
        assertEquals(4, heap.pop().getLong());
        assertEquals(3, heap.pop().getLong());
        assertEquals(2, heap.pop().getLong());
        assertEquals(1, heap.pop().getLong());
        assertEquals(0, heap.pop().getLong());
    }


    /**
     * test pop even more
     */
    public void testPop3() {
        KeyValue[] values = new KeyValue[6];
        values[3] = new KeyValue(0, 0);
        values[2] = new KeyValue(1, 1);
        values[5] = new KeyValue(2, 2);
        values[0] = new KeyValue(3, 3);
        values[1] = new KeyValue(4, 4);
        values[4] = new KeyValue(5, 5);
        heap = new MaxHeap(values);
        assertEquals(5, heap.pop().getLong());
        assertEquals(4, heap.pop().getLong());
        assertEquals(3, heap.pop().getLong());
        assertEquals(2, heap.pop().getLong());
        assertEquals(1, heap.pop().getLong());
        assertEquals(0, heap.pop().getLong());
    }


    /**
     * test add
     */
    public void testPopAdd() {
        KeyValue[] values = new KeyValue[6];
        values[0] = new KeyValue(0, 0);
        values[1] = new KeyValue(1, 1);
        values[2] = new KeyValue(2, 2);
        values[3] = new KeyValue(3, 3);
        values[4] = new KeyValue(4, 4);
        values[5] = new KeyValue(5, 5);
        heap = new MaxHeap(values);
        assertEquals(6, heap.heapSize());
        assertEquals(0, heap.listSize());
        heap.pop();
        assertEquals(5, heap.heapSize());
        assertEquals(0, heap.listSize());
        heap.add(new KeyValue(6, 6));
        assertEquals(5, heap.heapSize());
        assertEquals(1, heap.listSize());
        assertEquals(4, heap.pop().getLong());

        // Empty heap
        heap.add(new KeyValue(7, 7));
        heap.pop(); // 3
        heap.add(new KeyValue(8, 8));
        heap.pop(); // 2
        heap.add(new KeyValue(9, 9));
        heap.pop(); // 1
        heap.add(new KeyValue(10, 10));
        heap.pop();

    }
}
