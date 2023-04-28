package github.incodelearning.interview;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * You will optimize and refactor an N-way set-associative cache.
 * <p>
 * Background
 * <p>
 * There are 3 types of fixed-size cache: direct mapped, fully associative, and set associative.
 * <p>
 * In a direct mapped cache, every key is mapped to a single location, e.g., if cache has 100 spots, the key "key1"
 * might be at location 42. Another key "key2" might be at location 89, .etc. In a fully associative cache, there is
 * no mapping between keys and locations -- a key may be stored in any location in the cache.
 * <p>
 * A set associative cache is a balance between the two types above. It is structured as a collection of S number of
 * N-sized sets (hence "N-way"). The total capacity is S*N. Every key is mapped (typically via a hash function) to a
 * specific set, and then may be stored in any location within that set. Conflict misses can occur when a new key is
 * added to a full set. In this case, a replacement algorithm decides which existing key to evict from the cache to
 * make room for hte new key.
 * <p>
 * You can find more about cache structures at https://en.wikipedia.org/wiki/Cache_placement_policies
 * <p>
 * Note: while set associative caching is a concept often applied in hardware caches, it has similar advantages
 * (and disadvantages) in software caches as well.
 * <p>
 * Requirements
 * <p>
 * You ar provided with an initial implementation of an N-Way set associative cache. Your objective is to modify the
 * existing implementation in the following ways.
 * <p>
 * 1. Correct it. The initial implementation is correct for some use cases, but it has significant flaws. You should
 * correct the implementation to be a fully functional N-Way set associative cache per the description above. Use the
 * provided test cases as a guide.
 * <p>
 * 2. Optimize it. The initial implementation is quite naive and has significant opportunity for optimization.
 * <p>
 * 3. Refactor it with an extensible interface that allows for custome replacement algorithms. The initial
 * implementation includes a tightly-coupled LRU algorithm. Note this will likely require you to modify the
 * {@code SetAssociativeCacheFactory} helper class at the bottom to map the initialization and/or method invocation
 * from test case input to the interface you design.
 * <p>
 * 4. Implement an alternative Most Recently Used (MRU) replacement algorithm, using the interface and framework you
 * design in #2. Note: see note in 3.
 * <p>
 * 5. Make it appropriate for use in a multithreaded environment.
 * <p>
 * Testing and Debugging
 * <p>
 * Several test cases are provided with the initial implementation. Note: The tests are guides only. The solution will
 * be evaluated based on how well it meets the requirements, not all of which are easily evaluated by automated tests.
 * It is possible to pass all tests but still not sufficiently satisfy the requirements.
 */

//public class Solution {
//    public static void main(String[] args) throws IOException {
//        SetAssociativeCacheRunner.parseInput(System.in);
//    }
//
//    /**
//     * Parses Test Case input to instantiate and invoke a SetAssociativeCache
//     * <p>
//     * NOTE: You can typically ignore anything in here. Feel free to collapse...
//     * Format:
//     * {@code {MethodName}, [, {param1}[,{param2}][, ...]}
//     * Example input and output:
//     * <p>
//     * <ul>
//     * <ui>Set, key1, val1 will store key1 -> val1 in the cache
//     * <ui>ContainsKey, key1 will check for existence of key1 in the cache and prints true
//     * <ui> Get, key1 will retrieve the value of key1 from the cache and prints val1
//     */
//    public class SetAssociativeCacheRunner {
//        public static void parseInput(InputStream inputStream) throws IOException {
//            InputStreamReader inputReader = new InputStreamReader(inputStream);
//            BufferedReader reader = new BufferedReader(inputReader);
//            String line;
//            int lineCount = 0;
//            SetAssociativeCache<String, String> cache = null;
//            while (!isNullOrEmpty(line = reader.readLine())) {
//                lineCount++;
//                OutParam<String> replacementAlgoName = new OutParam<>();
//                if (lineCount == 1) {
//                    cache = createCache(line, replacementAlgoName);
//                } else {
//                    // All remaining lines invoke instance methods on the SetAssociativeCache
//                    Object retValue = SetAssociativeCacheFactory.InvokeCacheMethod(line, cache);
//                    // Write the method's return value (if any) to stdout
//                    if (retValue != null) {
//                        System.out.println(retValue);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * Create cache.
//     * Input: {SetCount}, {SetSize}, {ReplacementAlgoName}, e.g., 4, 5, LRUReplacementAlgo
//     * Instantiate a 5-way set associative cache with a total capacity of 20 items using the LRU replacement algo.
//     */
//    private static SetAssociativeCache<String, String> createCache(String inputLine,
//                                                                   OutParam<String> replacementAlgoName) {
//        String[] cacheParams = Arrays.stream(inputLine.split(",")).map(s -> s.trim()).toArray(n -> new String[n]);
//        int setCount = Integer.parseInt(cacheParams[0]);
//        int setSize = Integer.parseInt(cacheParams[1]);
//        replacementAlgoName.value = cacheParams[2];
//        return SetAssociativeCacheFactory.CreateStringCache(setCount, setSize, replacementAlgoName.value);
//    }
//    // ############################ BEGIN Solution Classes ############################
//
//    /**
//     * NOTE: You are free to modify anything below, except for class names and generic interface.
//     * Other public interface changes may require updating one or more of the helper classes above
//     * for test cases to run and pass.
//     * <p>
//     * A Set-Associative Cache data structure with fixed capacity.
//     * <p>
//     * - Data is structured into setCount # of setSize-sized sets.
//     * - Every possible key is associated with exactly one set via a hashing algorithm
//     * - If more items are added to a set than it has capacity for (i.e. > setSize items),
//     * a replacement victim is chosen from that set using an LRU algorithm.
//     * <p>
//     * NOTE: Part of the exercise is to allow for different kinds of replacement algorithms...
//     */
//    public static class SetAssociativeCache<TKey, TValue> {
//        int Capacity;
//        int SetSize;
//        int SetCount;
//        CacheSet<TKey, TValue>[] Sets;
//
//        public SetAssociativeCache(int setCount, int setSize) {
//            this.SetCount = setCount;
//            this.SetSize = setSize;
//            this.Capacity = this.SetCount * this.SetSize;
//            // Initialize the sets
//            this.Sets = new CacheSet[this.SetCount];
//            for (int i = 0; i < this.SetCount; i++) {
//                Sets[i] = new CacheSet<>(setSize);
//            }
//        }
//
//        /**
//         * Gets the value associated with `key`. Throws if key not found.
//         */
//        public TValue get(TKey key) {
//            int setIndex = this.getSetIndex(key);
//            CacheSet<TKey, TValue> set = this.Sets[setIndex];
//            return set.get(key);
//        }
//
//        /**
//         * Adds the `key` to the cache with the associated value, or overwrites the existing key.
//         * If adding would exceed capacity, an existing key is chosen to replace using an LRU algorithm
//         * (NOTE: It is part of this exercise to allow for more replacement algos)
//         */
//        public void set(TKey key, TValue value) {
//            int setIndex = this.getSetIndex(key);
//            CacheSet<TKey, TValue> set = this.Sets[setIndex];
//            set.set(key, value);
//        }
//
//        /**
//         * Returns the count of items in the cache
//         */
//        public int getCount() {
//            int count = 0;
//            for (int i = 0; i < this.Sets.length; i++) {
//                count += this.Sets[i].Count;
//            }
//            return count;
//        }
//
//        /**
//         * Returns `true` if the given `key` is present in the set; otherwise, `false`.
//         */
//        public boolean containsKey(TKey key) {
//            int setIndex = this.getSetIndex(key);
//            CacheSet<TKey, TValue> set = this.Sets[setIndex];
//            return set.containsKey(key);
//        }
//
//        /**
//         * Maps a key to a set
//         */
//        private int getSetIndex(TKey key) {
//            int c = Integer.MAX_VALUE;
//            int s = -1;
//            for (int i = 0; i < this.Sets.length; i++) {
//                if (this.Sets[i].containsKey(key)) {
//                    return i;
//                }
//                if (this.Sets[i].Count < c) {
//                    c = this.Sets[i].Count;
//                    s = i;
//                }
//            }
//            return s;
//        }
//    }
//
//    /**
//     * An internal data structure representing one set in a N-Way Set-Associative Cache
//     */
//    static class CacheSet<TKey, TValue> {
//        int Capacity;
//        CacheItem<TKey, TValue>[] Store;
//        LinkedList<TKey> UsageTracker;
//        public int Count;
//
//        public CacheSet(int capacity) {
//            this.Capacity = capacity;
//            this.UsageTracker = new LinkedList<>();
//            this.Store = new CacheItem[capacity];
//        }
//
//        /**
//         * Gets the value associated with `key`. Throws if key not found.
//         */
//        public TValue get(TKey key) {
//            // If the key is present, update the usage tracker
//            if (this.containsKey(key)) {
//                this.recordUsage(key);
//            } else {
//                throw new RuntimeException(String.format("The key '%s' was not found", key));
//            }
//            return this.Store[this.findIndexOfKey(key)].value;
//        }
//
//        /**
//         * Adds the `key` to the cache with the associated value, or overwrites the existing key.
//         * If adding would exceed capacity, an existing key is chosen to replace using an LRU algorithm
//         * (NOTE: It is part of this exercise to allow for more replacement algos)
//         */
//        public void set(TKey key, TValue value) {
//            int indexOfKey = this.findIndexOfKey(key);
//            if (indexOfKey >= 0) {
//                this.Store[indexOfKey].value = value;
//            } else {
//                int indexToSet;
//                // If the set is at it's capacity
//                if (this.Count == this.Capacity) {
//                    // Choose the Least-Recently-Used (LRU) item to replace, which will be at the tail of the usage tracker
//                    // TODO: Factor this logic out to allow for custom replacement algos
//                    TKey keyToReplace = this.UsageTracker.getLast();
//                    indexToSet = this.findIndexOfKey(keyToReplace);
//                    // Remove the existing key
//                    this.removeKey(keyToReplace);
//                } else {
//                    indexToSet = this.Count;
//                }
//                this.Store[indexToSet] = new CacheItem<>(key, value);
//                this.Count++;
//            }
//            this.recordUsage(key);
//        }
//
//        /**
//         * Returns `true` if the given `key` is present in the set; otherwise, `false`.
//         */
//        public boolean containsKey(TKey key) {
//            return this.findIndexOfKey(key) >= 0;
//        }
//
//        private void removeKey(TKey key) {
//            int indexOfKey = this.findIndexOfKey(key);
//            if (indexOfKey >= 0) {
//                this.UsageTracker.remove(key);
//                this.Store[indexOfKey] = null;
//                this.Count--;
//            }
//        }
//
//        private int findIndexOfKey(TKey key) {
//            for (int i = 0; i < this.Count; i++) {
//                if (this.Store[i] != null && this.Store[i].key.equals(key)) return i;
//            }
//            return -1;
//        }
//
//        private void recordUsage(TKey key) {
//            this.UsageTracker.remove(key);
//            this.UsageTracker.addFirst(key);
//        }
//    }
//
//    /**
//     * An internal data structure representing a single item in an N-Way Set-Associative Cache
//     */
//    static class CacheItem<TKey, TValue> {
//        public TKey key;
//        public TValue value;
//
//        public CacheItem(TKey key, TValue value) {
//            this.key = key;
//            this.value = value;
//        }
//    }
//
//    public final static String LruAlgorithm = "LRUReplacementAlgo";
//    public final static String MruAlgorithm = "MRUReplacementAlgo";
//
//    /**
//     * A common interface for replacement algos, which decide which item in a CacheSet to evict
//     */
//    interface IReplacementAlgo {
//        // TODO: Define the interface for replacement algos...
//    }
//
//    class LRUReplacementAlgo implements IReplacementAlgo {
//        // TODO: Implement the interface defined above
//    }
//
//    class MRUReplacementAlgo implements IReplacementAlgo {
//        // TODO: Implement the interface defined above
//    }
//
//    // ############################ BEGIN Helper Classes ############################
//    // NOTE: Your code in the classes below will not be evaluated as part of the exercise.
//    // They are just used by the stub code in the header to help run HackerRank test cases.
//    // You may need to make small modifications to these classes, depending on your interface design,
//    // for tests to run and pass, but it is not a core part of the exercise
//    //
//    static class OutParam<T> {
//        public T value;
//    }
//
//    private static boolean isNullOrEmpty(String s) {
//        return s == null || s.isEmpty();
//    }
//
//    public static class SetAssociativeCacheFactory {
//        /// NOTE: replacementAlgoName is provided in case you need it here. Whether you do will depend
//        // on your interface design.
//        public static SetAssociativeCache<String, String> CreateStringCache(
//                int setCount, int setSize, String replacementAlgoName) {
//            return new SetAssociativeCache<>(setCount, setSize);
//        }
//
//        /// NOTE: Modify only if you change the main interface of SetAssociativeCache
//        public static Object InvokeCacheMethod(String inputLine, SetAssociativeCache<String, String> cacheInstance) {
//            String[] callArgs = Arrays.stream(inputLine.split(",", -1))
//                    .map(a -> a.trim()).toArray(n -> new String[n]);
//            String methodName = callArgs[0].toLowerCase();
//            //String[] callParams = Arrays.copyOfRange(callArgs, 1, callArgs.length - 1); // TODO: This is unused
//            switch (methodName) {
//                case "get":
//                    return cacheInstance.get(callArgs[1]);
//                case "set":
//                    cacheInstance.set(callArgs[1], callArgs[2]);
//                    return null;
//                case "containskey":
//                    return cacheInstance.containsKey(callArgs[1]);
//                case "getcount":
//                    return cacheInstance.getCount();
//                // TODO: If you want to add and test other public methods to SetAssociativeCache,
//                //add them to the switch statement here... (this is not common)
//                default:
//                    throw new RuntimeException(String.format("Unknown method name '{%s}'", methodName));
//            }
//        }
//    }
//
//    // TODO: Consider making use of this in the `SetAssociativeCacheFactory` above to map replacement algo name
//    // to a IReplacementAlgo instance for the interface you design
//    public class ReplacementAlgoFactory {
//        IReplacementAlgo createReplacementAlgo(String replacementAlgoName) {
//            switch (replacementAlgoName) {
//                case LruAlgorithm:
//                    return new LRUReplacementAlgo();
//                case MruAlgorithm:
//                    return new MRUReplacementAlgo();
//                default:
//                    // TODO: If you want to test other replacement algos, add them to the switch statement here...
//                    throw new RuntimeException(String.format("Unknown replacement algo '%s'", replacementAlgoName));
//            }
//        }
//    }
//    // ^^ ######################### END Helper Classes ######################### ^^
//}


public class TTD {

    public final static String LruAlgorithm = "LRUReplacementAlgo";
    public final static String MruAlgorithm = "MRUReplacementAlgo";

    public static void main(String[] args) throws IOException {
        String s = "1, 2, LRUReplacementAlgo\n" +
                "set, key1, val1\n" +
                "get, key1\n" + // val1
                "containsKey, key1\n" + // true
                "set, key2, val2\n" +
                "getcount\n" + // 2
                "get, key2\n" + // val2
                "set, key2, val2.1\n" +
                "containsKey, key2\n" + // true
                "get, key2\n" + // val2.1
                "set, key3, val3\n" +
                "getcount\n" + // 2
                "containsKey, key3\n" + // true
                "containsKey, key2\n" + // true
                "containsKey, key1"; // false

        SetAssociativeCacheRunner.parseInput(new ByteArrayInputStream(s.getBytes()));
        //SetAssociativeCacheRunner.parseInput(System.in);
    }
    // ############################ BEGIN Solution Classes ############################

    /**
     * Create cache.
     * Input: {SetCount}, {SetSize}, {ReplacementAlgoName}, e.g., 4, 5, LRUReplacementAlgo
     * Instantiate a 5-way set associative cache with a total capacity of 20 items using the LRU replacement algo.
     */
    private static SetAssociativeCache<String, String> createCache(String inputLine,
                                                                   OutParam<String> replacementAlgoName) {
        String[] cacheParams = Arrays.stream(inputLine.split(",")).map(s -> s.trim()).toArray(n -> new String[n]);
        int setCount = Integer.parseInt(cacheParams[0]);
        int setSize = Integer.parseInt(cacheParams[1]);
        replacementAlgoName.value = cacheParams[2];
        return SetAssociativeCacheFactory.CreateStringCache(setCount, setSize, replacementAlgoName.value);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * A common interface for replacement algos, which decide which item in a CacheSet to evict
     */
    interface IReplacementAlgo {
        CacheItem whichItemToEvict(CacheSet cacheSet);
        // TODO: Define the interface for replacement algos...
    }

    /**
     * NOTE: You are free to modify anything below, except for class names and generic interface.
     * Other public interface changes may require updating one or more of the helper classes above
     * for test cases to run and pass.
     * <p>
     * A Set-Associative Cache data structure with fixed capacity.
     * <p>
     * - Data is structured into setCount # of setSize-sized sets.
     * - Every possible key is associated with exactly one set via a hashing algorithm
     * - If more items are added to a set than it has capacity for (i.e. > setSize items),
     * a replacement victim is chosen from that set using an LRU algorithm.
     * <p>
     * NOTE: Part of the exercise is to allow for different kinds of replacement algorithms...
     */
    public static class SetAssociativeCache<TKey, TValue> {
        int Capacity;
        int SetSize;
        int SetCount;
        CacheSet<TKey, TValue>[] Sets;
        IReplacementAlgo algo;

        public SetAssociativeCache(int setCount, int setSize, IReplacementAlgo algo) {
            this.SetCount = setCount;
            this.SetSize = setSize;
            this.Capacity = this.SetCount * this.SetSize;
            // Initialize the sets
            this.Sets = new CacheSet[this.SetCount];
            this.algo = algo;
            for (int i = 0; i < this.SetCount; i++) {
                Sets[i] = new CacheSet<>(setSize);
            }
        }

        /**
         * Gets the value associated with `key`. Throws if key not found.
         */
        public TValue get(TKey key) {
            int setIndex = this.getSetIndex(key);
            CacheSet<TKey, TValue> set = this.Sets[setIndex];
            return set.get(key);
        }

        /**
         * Adds the `key` to the cache with the associated value, or overwrites the existing key.
         * If adding would exceed capacity, an existing key is chosen to replace using an LRU algorithm
         * (NOTE: It is part of this exercise to allow for more replacement algos)
         */
        public void set(TKey key, TValue value) {
            if (key == null) throw new NullPointerException("do not support null key");
            int setIndex = this.getSetIndex(key);
            CacheSet<TKey, TValue> set = this.Sets[setIndex];
            set.set(key, value, algo);
        }

        /**
         * Returns the count of items in the cache
         */
        public int getCount() {
            int count = 0;
            for (int i = 0; i < this.Sets.length; i++) {
                count += this.Sets[i].Count;
            }
            return count;
        }

        /**
         * Returns `true` if the given `key` is present in the set; otherwise, `false`.
         */
        public boolean containsKey(TKey key) {
            int setIndex = this.getSetIndex(key);
            CacheSet<TKey, TValue> set = this.Sets[setIndex];
            return set.containsKey(key);
        }

        /**
         * Maps a key to a set
         */
        private int getSetIndex(TKey key) {
            return Math.abs(key.hashCode()) % SetCount;
        }
    }

    /**
     * An internal data structure representing one set in a N-Way Set-Associative Cache
     */
    static class CacheSet<TKey, TValue> {

        public int Count;
        int Capacity;
        CacheItem dummyHead, dummyTail;

        Map<TKey, CacheItem> keyToItemMap;
        ReadWriteLock lock;
        Lock readLock, writeLock;

        public CacheSet(int capacity) {
            this.Capacity = capacity;
            this.Count = 0;
            keyToItemMap = new HashMap<>();
            dummyHead = new CacheItem(null, null);
            dummyTail = new CacheItem(null, null);
            dummyHead.next = dummyTail;
            dummyTail.pre = dummyHead;
            lock = new ReentrantReadWriteLock();
            readLock = lock.readLock();
            writeLock = lock.writeLock();
        }

        private void moveToHead(CacheItem item) {
            removeItem(item);
            addItem(item);
        }

        private void removeItem(CacheItem item) {
            CacheItem pre = item.pre;
            CacheItem next = item.next;
            pre.next = next;
            next.pre = pre;
        }

        private void addItem(CacheItem item) {
            item.pre = dummyHead;
            item.next = dummyHead.next;
            dummyHead.next.pre = item;
            dummyHead.next = item;
        }

        /**
         * Gets the value associated with `key`. Throws if key not found.
         */
        public TValue get(TKey key) {
            if (!this.containsKey(key)) throw new RuntimeException(String.format("The key '%s' was not found", key));
            try {
                readLock.lock();
                CacheItem<TKey, TValue> item = keyToItemMap.get(key);
                moveToHead(item);
                return item.value;
            } finally {
                readLock.unlock();
            }
        }

        /**
         * Adds the `key` to the cache with the associated value, or overwrites the existing key.
         * If adding would exceed capacity, an existing key is chosen to replace using an LRU algorithm
         * (NOTE: It is part of this exercise to allow for more replacement algos)
         */
        public void set(TKey key, TValue value, IReplacementAlgo algo) {
            try {
                writeLock.lock();
                put(key, value, algo);
            } finally {
                writeLock.unlock();
            }
        }

        private void put(TKey key, TValue value, IReplacementAlgo algo) {
            if (containsKey(key)) {
                CacheItem<TKey, TValue> item = keyToItemMap.get(key);
                item.value = value;
                moveToHead(item);
            } else {
                CacheItem<TKey, TValue> newItem = new CacheItem<>(key, value);
                keyToItemMap.put(key, newItem);
                addItem(newItem);
                ++Count;
                if (Count > Capacity) {
                    CacheItem<TKey, TValue> toDelete = algo.whichItemToEvict(this);
                    keyToItemMap.remove(toDelete.key);
                    removeItem(toDelete);
                    --Count;
                }
            }
        }

        /**
         * Returns `true` if the given `key` is present in the set; otherwise, `false`.
         */
        public boolean containsKey(TKey key) {
            return keyToItemMap.containsKey(key);
        }

        private void removeKey(TKey key) {
            CacheItem<TKey, TValue> item = keyToItemMap.get(key);
            keyToItemMap.remove(key);
            removeItem(item);
        }

    }

    /**
     * An internal data structure representing a single item in an N-Way Set-Associative Cache
     */
    static class CacheItem<TKey, TValue> {
        public TKey key;
        public TValue value;
        private CacheItem<TKey, TValue> pre, next;

        public CacheItem(TKey key, TValue value) {
            this.key = key;
            this.value = value;
        }
    }

    // ############################ BEGIN Helper Classes ############################
    // NOTE: Your code in the classes below will not be evaluated as part of the exercise.
    // They are just used by the stub code in the header to help run HackerRank test cases.
    // You may need to make small modifications to these classes, depending on your interface design,
    // for tests to run and pass, but it is not a core part of the exercise
    //
    static class OutParam<T> {
        public T value;
    }

    static class LRUReplacementAlgo implements IReplacementAlgo {
        @Override
        public CacheItem whichItemToEvict(CacheSet cacheSet) {
            return cacheSet.dummyTail.pre;
        }
    }

    static class MRUReplacementAlgo implements IReplacementAlgo {
        @Override
        public CacheItem whichItemToEvict(CacheSet cacheSet) {
            return cacheSet.dummyHead.next;
        }
    }

    public class SetAssociativeCacheFactory {
        /// NOTE: replacementAlgoName is provided in case you need it here. Whether you do will depend
        // on your interface design.
        public static SetAssociativeCache<String, String> CreateStringCache(
                int setCount, int setSize, String replacementAlgoName) {
            return new SetAssociativeCache<>(setCount, setSize,
                    ReplacementAlgoFactory.createReplacementAlgo(replacementAlgoName));
        }

        /// NOTE: Modify only if you change the main interface of SetAssociativeCache
        public static Object InvokeCacheMethod(String inputLine, SetAssociativeCache<String, String> cacheInstance) {
            String[] callArgs = Arrays.stream(inputLine.split(",", -1))
                    .map(a -> a.trim()).toArray(n -> new String[n]);
            String methodName = callArgs[0].toLowerCase();
            //String[] callParams = Arrays.copyOfRange(callArgs, 1, callArgs.length - 1); // TODO: This is unused
            switch (methodName) {
                case "get":
                    return cacheInstance.get(callArgs[1]);
                case "set":
                    cacheInstance.set(callArgs[1], callArgs[2]);
                    return null;
                case "containskey":
                    return cacheInstance.containsKey(callArgs[1]);
                case "getcount":
                    return cacheInstance.getCount();
                // TODO: If you want to add and test other public methods to SetAssociativeCache,
                //add them to the switch statement here... (this is not common)
                default:
                    throw new RuntimeException(String.format("Unknown method name '{%s}'", methodName));
            }
        }
    }

    /**
     * Parses Test Case input to instantiate and invoke a SetAssociativeCache
     * <p>
     * NOTE: You can typically ignore anything in here. Feel free to collapse...
     * Format:
     * {@code {MethodName}, [, {param1}[,{param2}][, ...]}
     * Example input and output:
     * <p>
     * <ul>
     * <ui>Set, key1, val1 will store key1 -> val1 in the cache
     * <ui>ContainsKey, key1 will check for existence of key1 in the cache and prints true
     * <ui>Get, key1 will retrieve the value of key1 from the cache and prints val1
     */
    public class SetAssociativeCacheRunner {
        public static void parseInput(InputStream inputStream) throws IOException {
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputReader);
            String line;
            int lineCount = 0;
            SetAssociativeCache<String, String> cache = null;
            while (!isNullOrEmpty(line = reader.readLine())) {
                lineCount++;
                OutParam<String> replacementAlgoName = new OutParam<>();
                if (lineCount == 1) {
                    cache = createCache(line, replacementAlgoName);
                } else {
                    // All remaining lines invoke instance methods on the SetAssociativeCache
                    Object retValue = SetAssociativeCacheFactory.InvokeCacheMethod(line, cache);
                    // Write the method's return value (if any) to stdout
                    if (retValue != null) {
                        System.out.println(retValue);
                    }
                }
            }
        }
    }

    // TODO: Consider making use of this in the `SetAssociativeCacheFactory` above to map replacement algo name
    // to a IReplacementAlgo instance for the interface you design
    public class ReplacementAlgoFactory {
        static IReplacementAlgo createReplacementAlgo(String replacementAlgoName) {
            switch (replacementAlgoName) {
                case LruAlgorithm:
                    return new LRUReplacementAlgo();
                case MruAlgorithm:
                    return new MRUReplacementAlgo();
                default:
                    // TODO: If you want to test other replacement algos, add them to the switch statement here...
                    throw new RuntimeException(String.format("Unknown replacement algo '%s'", replacementAlgoName));
            }
        }
    }
    // ^^ ######################### END Helper Classes ######################### ^^
}

/**
 * Solution 1.
 */
//public class Solution1 {
//
//    public final static String LruAlgorithm = "LRUReplacementAlgo";
//    public final static String MruAlgorithm = "MRUReplacementAlgo";
//
//    public static void main(String[] args) throws IOException {
//        SetAssociativeCacheRunner.parseInput(System.in);
//    }
//    // ############################ BEGIN Solution Classes ############################
//
//    private static SetAssociativeCache<String, String> createCache(
//            String inputLine, OutParam<String> replacementAlgoName) {
//        String[] cacheParams = Arrays.stream(inputLine.split(",")).map(s -> s.trim()).toArray(n -> new String[n]);
//        int setCount = Integer.parseInt(cacheParams[0]);
//        int setSize = Integer.parseInt(cacheParams[1]);
//        replacementAlgoName.value = cacheParams[2];
//        return SetAssociativeCacheFactory.CreateStringCache(setCount, setSize, replacementAlgoName.value);
//    }
//
//    private static boolean isNullOrEmpty(String s) {
//        return s == null || s.isEmpty();
//    }
//
//    /**
//     * A common interface for replacement algos, which decide which item in a CacheSet to evict
//     */
//    interface IReplacementAlgo<TKey> {
//        public void updateTracker(LinkedList<TKey> tracker, TKey key);
//    }
//
//    /**
//     * Parses Test Case input to instantiate and invoke a SetAssociativeCache
//     * <p>
//     * NOTE: You can typically ignore anything in here. Feel free to collapse...
//     */
//    static class SetAssociativeCacheRunner {
//        public static void parseInput(InputStream inputStream) throws IOException {
//            InputStreamReader inputReader = new InputStreamReader(inputStream);
//            BufferedReader reader = new BufferedReader(inputReader);
//            String line;
//            int lineCount = 0;
//            SetAssociativeCache<String, String> cache = null;
//            while (!isNullOrEmpty(line = reader.readLine())) {
//                lineCount++;
//                OutParam<String> replacementAlgoName = new OutParam<>();
//                if (lineCount == 1) {
//                    cache = createCache(line, replacementAlgoName);
//                } else {
//                    // All remaining lines invoke instance methods on the SetAssociativeCache
//                    Object retValue = SetAssociativeCacheFactory.InvokeCacheMethod(line, cache);
//                    // Write the method's return value (if any) to stdout
//                    if (retValue != null) {
//                        System.out.println(retValue);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * NOTE: You are free to modify anything below, except for class names and generic interface.
//     * Other public interface changes may require updating one or more of the helper classes above
//     * for test cases to run and pass.
//     * <p>
//     * A Set-Associative Cache data structure with fixed capacity.
//     * <p>
//     * - Data is structured into setCount # of setSize-sized sets.
//     * - Every possible key is associated with exactly one set via a hashing algorithm
//     * - If more items are added to a set than it has capacity for (i.e. > setSize items),
//     * a replacement victim is chosen from that set using an LRU algorithm.
//     * <p>
//     * NOTE: Part of the exercise is to allow for different kinds of replacement algorithms...
//     */
//    public static class SetAssociativeCache<TKey, TValue> {
//        int Capacity;
//        int SetSize;
//        int SetCount;
//        CacheSet<TKey, TValue>[] Sets;
//
//        public SetAssociativeCache(int setCount, int setSize, IReplacementAlgo<TKey> replacementAlgo) {
//            this.SetCount = setCount;
//            this.SetSize = setSize;
//            this.Capacity = this.SetCount * this.SetSize;
//            // Initialize the sets
//            this.Sets = new CacheSet[this.SetCount];
//            for (int i = 0; i < this.SetCount; i++) {
//                Sets[i] = new CacheSet<>(setSize, replacementAlgo);
//            }
//        }
//
//        /**
//         * Gets the value associated with `key`. Throws if key not found.
//         */
//        public TValue get(TKey key) {
//            int setIndex = this.getSetIndex(key);
//            CacheSet<TKey, TValue> set = this.Sets[setIndex];
//            return set.get(key);
//        }
//
//        /**
//         * Adds the `key` to the cache with the associated value, or overwrites the existing key.
//         * If adding would exceed capacity, an existing key is chosen to replace using an LRU algorithm
//         * (NOTE: It is part of this exercise to allow for more replacement algos)
//         */
//        public void set(TKey key, TValue value) {
//            int setIndex = this.getSetIndex(key);
//            CacheSet<TKey, TValue> set = this.Sets[setIndex];
//            set.set(key, value);
//        }
//
//        /**
//         * Returns the count of items in the cache
//         */
//        public int getCount() {
//            int count = 0;
//            for (int i = 0; i < this.Sets.length; i++) {
//                count += this.Sets[i].Count;
//            }
//            return count;
//        }
//
//        /**
//         * Returns `true` if the given `key` is present in the set; otherwise, `false`.
//         */
//        public boolean containsKey(TKey key) {
//            int setIndex = this.getSetIndex(key);
//            CacheSet<TKey, TValue> set = this.Sets[setIndex];
//            return set.containsKey(key);
//        }
//
//        /**
//         * Maps a key to a set
//         */
//        private int getSetIndex(TKey key) {
//            int startIndex = Math.abs(key.hashCode()) % SetCount;
//            return startIndex;
//        }
//    }
//
//    /**
//     * An internal data structure representing one set in a N-Way Set-Associative Cache
//     */
//    static class CacheSet<TKey, TValue> {
//        public int Count;
//        int Capacity;
//        HashMap<TKey, TValue> map;
//        LinkedList<TKey> UsageTracker;
//        IReplacementAlgo<TKey> replacementAlgo;
//
//        public CacheSet(int capacity, IReplacementAlgo<TKey> replacementAlgo) {
//            this.Capacity = capacity;
//            this.Count = 0;
//            this.UsageTracker = new LinkedList<>();
//            this.map = new HashMap<>(capacity);
//            this.replacementAlgo = replacementAlgo;
//        }
//
//        //remove the tail
//        public void prune() {
//            TKey key = UsageTracker.removeLast();
//            map.remove(key);
//            Count--;
//        }
//
//        /**
//         * Gets the value associated with `key`. Throws if key not found.
//         */
//        public TValue get(TKey key) {
//            boolean res = UsageTracker.remove(key);
//            if (res) {
//                replacementAlgo.updateTracker(UsageTracker, key);
//                return this.map.get(key);
//            } else {
//                throw new RuntimeException(String.format("The key '%s' was not found", key));
//            }
//        }
//
//        /**
//         * Adds the `key` to the cache with the associated value, or overwrites the existing key.
//         * If adding would exceed capacity, an existing key is chosen to replace using an LRU algorithm
//         * (NOTE: It is part of this exercise to allow for more replacement algos)
//         */
//        public void set(TKey key, TValue value) {
//            // check if pruning is needed
//            if (UsageTracker.size() == this.Capacity) {
//                this.prune();
//            }
//            replacementAlgo.updateTracker(UsageTracker, key);
//            map.put(key, value);
//            Count++;
//        }
//
//        /**
//         * Returns `true` if the given `key` is present in the set; otherwise, `false`.
//         */
//        public boolean containsKey(TKey key) {
//            return this.map.containsKey(key);
//        }
//    }
//
//    static class LRUReplacementAlgo<TKey> implements IReplacementAlgo<TKey> {
//        public void updateTracker(LinkedList<TKey> tracker, TKey key) {
//            tracker.addFirst(key);
//        }
//    }
//    // ############################ BEGIN Helper Classes ############################
//    // NOTE: Your code in the classes below will not be evaluated as part of the exericse.
//    // They are just used by the stub code in the header to help run HackerRank test cases.
//    // You may need to make small modifications to these classes, depending on your interface design,
//    // for tests to run and pass, but it is not a core part of the exercise
//
//    static class MRUReplacementAlgo<TKey> implements IReplacementAlgo<TKey> {
//        public void updateTracker(LinkedList<TKey> tracker, TKey key) {
//            tracker.addLast(key);
//        }
//    }
//
//    //
//    static class OutParam<T> {
//        public T value;
//    }
//
//    public static class SetAssociativeCacheFactory {
//        /// NOTE: replacementAlgoName is provided in case you need it here. Whether you do will depend on your interface design.
//        public static SetAssociativeCache<String, String> CreateStringCache(int setCount, int setSize, String replacementAlgoName) {
//            IReplacementAlgo<String> replacementAlgo =
//                    replacementAlgoName.equals(LruAlgorithm) ? new LRUReplacementAlgo<String>() : new MRUReplacementAlgo<String>();
//            return new SetAssociativeCache<>(setCount, setSize, replacementAlgo);
//        }
//
//        /// NOTE: Modify only if you change the main interface of SetAssociativeCache
//        public static Object InvokeCacheMethod(String inputLine, SetAssociativeCache<String, String> cacheInstance) {
//            String[] callArgs = Arrays.stream(inputLine.split(",", -1))
//                    .map(a -> a.trim()).toArray(n -> new String[n]);
//            String methodName = callArgs[0].toLowerCase();
//            //String[] callParams = Arrays.copyOfRange(callArgs, 1, callArgs.length - 1); // TODO: This is unused
//            switch (methodName) {
//                case "get":
//                    return cacheInstance.get(callArgs[1]);
//                case "set":
//                    cacheInstance.set(callArgs[1], callArgs[2]);
//                    return null;
//                case "containskey":
//                    return cacheInstance.containsKey(callArgs[1]);
//                case "getcount":
//                    return cacheInstance.getCount();
//                // TODO: If you want to add and test other public methods to SetAssociativeCache,
//                //add them to the switch statement here... (this is not common)
//                default:
//                    throw new RuntimeException(String.format("Unknown method name '{%s}'", methodName));
//            }
//        }
//    }
//
//    // TODO: Consider making use of this in the `SetAssociativeCacheFactory` above to map replacement algo name
//    // to a IReplacementAlgo instance for the interface you design
//    public class ReplacementAlgoFactory {
//        IReplacementAlgo createReplacementAlgo(String replacementAlgoName) {
//            switch (replacementAlgoName) {
//                case LruAlgorithm:
//                    return new LRUReplacementAlgo();
//                case MruAlgorithm:
//                    return new MRUReplacementAlgo();
//                default:
//                    // TODO: If you want to test other replacement algos, add them to the switch statement here...
//                    throw new RuntimeException(String.format("Unknown replacement algo '%s'", replacementAlgoName));
//            }
//        }
//    }
//    // ^^ ######################### END Helper Classes ######################### ^^
//}
