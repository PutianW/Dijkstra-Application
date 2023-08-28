import java.awt.*;

/**
 * @implNote implement a queue using a circular array with initial capacity 8.
 *
 * Implement BetterQueueInterface and add a constructor
 *
 * You are explicitly forbidden from using java.util.Queue and any subclass
 * (including LinkedList, for example) and any other java.util.* library EXCEPT java.util.Objects.
 * Write your own implementation of a Queue.
 *
 * Another great example of why we are implementing our own queue here is that
 * our queue is actually FASTER than Java's LinkedList (our solution is 2x faster!). This is due
 * to a few reasons, the biggest of which are 1. the overhead associated with standard library
 * classes, 2. the fact that Java's LinkedList doesn't store elements next to each other, which
 * increases memory overhead for the system, and 3. LinkedList stores 2 pointers with each element,
 * which matters when you store classes that aren't massive (because it increases the size of each
 * element, making more work for the system).
 *
 * @param <E> the type of object this queue will be holding
 */
public class BetterQueue<E> implements BetterQueueInterface<E> {

    /**
     * Initial size of queue.  Do not decrease capacity below this value.
     */
    private final int INIT_CAPACITY = 8;


    /**
     * If the array needs to increase in size, it should be increased to
     * old capacity * INCREASE_FACTOR.
     *
     * If it cannot increase by that much (old capacity * INCREASE_FACTOR > max int),
     * it should increase by CONSTANT_INCREMENT.
     *
     * If that can't be done either throw OutOfMemoryError()
     *
     */
    private final int INCREASE_FACTOR = 2;
    private final int CONSTANT_INCREMENT = 1 << 5; // 32



    /**
     * If the number of elements stored is < capacity * DECREASE_FACTOR, it should decrease
     * the capacity of the UDS to max(capacity * DECREASE_FACTOR, initial capacity).
     *
     */
    private final double DECREASE_FACTOR = 0.5;


    /**
     * Array to store elements in (according to the implementation
     * note in the class header comment).
     *
     * Circular arrays work as follows:
     *
     *   1. Removing an element increments the "first" index
     *   2. Adding an element inserts it into the next available slot. Incrementing
     *      the "last" index WRAPS to the front of the array, if there are spots available
     *      there (if we have removed some elements, for example).
     *   3. The only way to know if the array is full is if the "last" index
     *      is right in front of the "first" index.
     *   4. If you need to increase the size of the array, put the elements back into
     *      the array starting with "first" (i.e. "first" is at index 0 in the new array).
     *   5. No other implementation details will be given, but a good piece of advice
     *      is to draw out what should be happening in each operation before you code it.
     *
     *   hint: modulus might be helpful
     */


    private E[] queue;
    private int first;  // index of the first element;
    private int last;   // index of the last element + 1;
    private int size;   // current size of the queue;
    private int currentCapacity;    // current capacity of the queue;


    // increase the capacity of the queue;
    private void increaseSize() throws OutOfMemoryError {
        long factorSize = this.currentCapacity * INCREASE_FACTOR;
        long incrementSize = this.currentCapacity + CONSTANT_INCREMENT;
        E[] newQueue;
        int j = 0;
        if (factorSize > Integer.MAX_VALUE) {
            if (incrementSize > Integer.MAX_VALUE) {
                throw new OutOfMemoryError();
            }
            newQueue = (E[]) new Object[this.currentCapacity + CONSTANT_INCREMENT];
            for (int i = 0; i < this.currentCapacity; i++) {
                j = (this.first + i) % this.currentCapacity;
                newQueue[i] = this.queue[j];
            }
            this.currentCapacity = this.currentCapacity + CONSTANT_INCREMENT;
        } else {
            newQueue = (E[]) new Object[this.currentCapacity * INCREASE_FACTOR];
            for (int i = 0; i < this.currentCapacity; i++) {
                j = (this.first + i) % this.currentCapacity;
                newQueue[i] = this.queue[j];
            }
            this.currentCapacity = this.currentCapacity * INCREASE_FACTOR;
        }
        this.queue = newQueue;
        this.first = 0;
        this.last = this.size;
    }


    // decrease the capacity of the queue;
    private void decreaseSize() {
        int newCapacity = Math.max((int) (this.currentCapacity * this.DECREASE_FACTOR), INIT_CAPACITY);
        E[] newQueue = (E[]) new Object[newCapacity];
        int j = 0;
        for (int i = 0; i < this.size; i++) {
            j = (this.first + i) % this.currentCapacity;
            newQueue[i] = this.queue[j];
        }
        this.currentCapacity = newCapacity;
        this.queue = newQueue;
        this.first = 0;
        this.last = this.size;
    }


    /**
     * Constructs an empty queue
     */
    @SuppressWarnings("unchecked")
    public BetterQueue(){
        //todo
        this.queue = (E[]) new Object[INIT_CAPACITY];
        this.first = 0;
        this.last = 0;
        this.size = 0;
        this.currentCapacity = INIT_CAPACITY;
    }

    /**
     * Add an item to the back of the queue
     *
     * @param item item to push
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void add(E item) {
        //todo
        if (item == null) {
            throw new NullPointerException();
        }

        if (this.size >= this.currentCapacity) {
            increaseSize();
        }
        this.queue[this.last] = item;
        this.last = (this.last + 1) % this.currentCapacity;
        this.size++;
    }

    /**
     * Returns the front of the queue (does not remove it) or <code>null</code> if the queue is empty
     *
     * @return front of the queue or <code>null</code> if the queue is empty
     */
    @Override
    public E peek() {
        //todo
        if (this.size == 0) {
            return null;
        } else {
            return this.queue[this.first];
        }
    }

    /**
     * Returns and removes the front of the queue
     *
     * @return the head of the queue, or <code>null</code> if this queue is empty
     */
    @Override
    public E remove() {
        //todo
        if (this.size == 0) {
            return null;
        } else {
            E temp = this.queue[this.first];
            this.queue[this.first] = null;
            this.first = (this.first + 1) % this.currentCapacity;
            this.size--;
            if (this.size < (int) (this.currentCapacity * this.DECREASE_FACTOR)) {
                decreaseSize();
            }
            return temp;
        }
    }

    /**
     * Returns the number of elements in the queue
     *
     * @return integer representing the number of elements in the queue
     */
    @Override
    public int size() {
        //todo
        return this.size;
    }

    /**
     * Returns whether the queue is empty
     *
     * @return true if the queue is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        //todo
        return (this.size == 0);
    }

    /**
     * DO NOT MODIFY NOR IMPLEMENT THIS FUNCTION
     *
     * @param g graphics object to draw on
     */
    @Override
    public void draw(Graphics g) {
        //DO NOT MODIFY NOR IMPLEMENT THIS FUNCTION
        if(g != null) g.getColor();
        //todo GRAPHICS DEVELOPER:: draw the queue how we discussed
        //251 STUDENTS:: YOU ARE NOT THE GRAPHICS DEVELOPER!
    }
}
