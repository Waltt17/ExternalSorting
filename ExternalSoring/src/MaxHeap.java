
/**
 * @author waltt17
 */
public class MaxHeap {
    private int maxSize; // max size of heap + list combined
    private int heapSize; // current size of heap
    private int listSize; // Current size of list of leftovers items
    private int listIndex;
    private KeyValue lastPopped;
    private KeyValue[] heap;


    /**
     * constructor for maxheap
     * 
     * @param values
     *            the keyvalue array to be heaped
     */
    public MaxHeap(KeyValue[] values) {
        heap = new KeyValue[values.length];
        maxSize = values.length;
        heapSize = 0;
        listSize = 0;
        listIndex = heap.length;
        lastPopped = null;
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                add(values[i]);
            }
        }
    }


    /**
     * if the heap is empty
     * 
     * @return if it is empty
     */
    public boolean isEmpty() {
        return heapSize == 0;
    }


    /**
     * the size of the heap
     * 
     * @return the heapsize
     */
    public int heapSize() {
        return heapSize;
    }


    /**
     * the size of the list
     * 
     * @return the listsize
     */
    public int listSize() {
        return listSize;
    }


    /**
     * gets the left child of given index
     * 
     * @param index
     *            the index to be found
     * @return the left child index
     */
    private int getLeftChildIndex(int index) {
        return 2 * index + 1;
    }


    /**
     * gets right child of given index
     * 
     * @param index
     *            the index to be found
     * @return the right child index
     */
    private int getRightChildIndex(int index) {
        return 2 * index + 2;
    }


    /**
     * gets the parent at given index
     * 
     * @param index
     *            index to be searched
     * @return the index of the parent
     */
    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }


    /**
     * if the index is a leaf
     * 
     * @param index
     *            indexx to be searched
     * @return if it is a leaf or not
     */
    public boolean isLeaf(int index) {
        return (index >= (heapSize / 2)) && (index <= heapSize);
    }


    /**
     * returns value at leftchild
     * 
     * @param index
     *            index to be looked at
     * @return the keyvalue of leftchild
     */
    public KeyValue leftchild(int index) {
        return heap[getLeftChildIndex(index)];
    }


    /**
     * returns value of right child
     * 
     * @param index
     *            index to be looked at
     * @return the keyvalue of rightchild
     */
    public KeyValue rightchild(int index) {
        return heap[getRightChildIndex(index)];
    }


    /**
     * returns value of parent
     * 
     * @param index
     *            the index to be looked at
     * @return the keyvalue of parent
     */
    public KeyValue parent(int index) {
        return heap[getParentIndex(index)];
    }


    /**
     * if there is a left child of given index
     * 
     * @param index
     *            to be looked at
     * @return if there is a left child
     */
    public boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < heapSize;
    }


    /**
     * if there is a right child of given index
     * 
     * @param index
     *            to be looked at
     * @return if there is a right child
     */
    public boolean hasRightChild(int index) {
        return getRightChildIndex(index) < heapSize;
    }


    /**
     * if there is a parent of given index
     * 
     * @param index
     *            to be looked at
     * @return if there is a parent
     */
    public boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }


    /**
     * swaps two values
     * 
     * @param one
     *            first index
     * @param two
     *            second index
     */
    private void swap(int one, int two) {
        KeyValue temp = heap[one];
        heap[one] = heap[two];
        heap[two] = temp;
    }


    /**
     * peeks at top
     * 
     * @return the keyvalue at top
     */
    public KeyValue peek() {
        if (heapSize == 0) {
            return null;
        }
        return heap[0];
    }


    /**
     * pops the top
     * 
     * @return the keyvalue at top
     */
    public KeyValue pop() {
        if (heapSize == 0) {
            lastPopped = null;
            return null;
        }
        KeyValue temp = heap[0];
        lastPopped = temp;
        heap[0] = heap[heapSize - 1];
        heap[heapSize - 1] = null;
        heapSize--;
        heapifyDown();
        return temp;
    }


    /**
     * rebuilds the heap
     */
    public void rebuildHeap() {
        for (int i = listIndex; i < maxSize; i++) {
            heap[heapSize] = heap[i];
            heapSize++;
            heapifyUp();
        }
        listSize = 0;
        listIndex = maxSize;
        for (int i = heapSize; i < maxSize; i++) {
            heap[i] = null;
        }
    }


    /**
     * bubbles down after removal
     */
    private void heapifyDown() {
        int index = 0;
        while (hasLeftChild(index)) {
            int smaller = getLeftChildIndex(index);
            if (hasRightChild(index) && rightchild(index)
                .getDouble() > leftchild(index).getDouble()) {
                smaller = getRightChildIndex(index);
            }

            if (heap[index].getDouble() > heap[smaller].getDouble()) {
                break;
            }
            else {
                swap(index, smaller);
            }
            index = smaller;
        }
    }


    /**
     * adds to heap
     * 
     * @param add
     *            keyvalue to be added
     */
    public void add(KeyValue add) {
        if (!(lastPopped == null) && add.getDouble() > lastPopped.getDouble()) {
            listIndex--;
            heap[listIndex] = add;
            listSize++;

        }
        else if (heapSize + 1 <= maxSize) {
            heap[heapSize] = add;
            heapSize++;
            heapifyUp();
        }
    }


    /**
     * bubbles up addeed value to correct position
     */
    private void heapifyUp() {
        int index = heapSize - 1;
        while (hasParent(index) && parent(index).getDouble() < heap[index]
            .getDouble()) {
            swap(getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

}
