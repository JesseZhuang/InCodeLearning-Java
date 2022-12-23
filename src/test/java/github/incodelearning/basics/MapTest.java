package github.incodelearning.basics;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MapTest {

    @Test
    public void mutableKeyResultInDataLoss() {
        Map<String, Integer> immutableMap = Map.of("seattle", 0);
        Map<String, Integer> mutableMap = new HashMap<>(immutableMap);

        Map<Map<String, Integer>, Integer> map2 = new HashMap<>();
        map2.put(mutableMap, 3);
        assertEquals(immutableMap.hashCode(), mutableMap.hashCode()); // hashed to the same bucket
        assertTrue(immutableMap.equals(mutableMap)); // hashcode agrees with equals
        assertTrue(immutableMap != mutableMap); // reference is not same
        // check in bucket, can find key that equals()
        assertEquals(Integer.valueOf(3), map2.get(immutableMap));

        mutableMap.put("seattle", -3);
        assertNotEquals(mutableMap.hashCode(), immutableMap.hashCode()); // hashcode no longer the same

        assertNull(map2.get(immutableMap)); // hashed to the same bucket but cannot find key equals()
        assertNull(map2.get(mutableMap)); // hashed to a different bucket
        assertEquals(1, map2.size()); // not empty but data could not ge retrieved
    }
}
