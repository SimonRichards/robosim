package simulation.inanimates;

import simulation.entities.Cup;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Simon
 */
public class ThingTest {

    /**
     * Test of isUpright method, of class Inanimate.
     */
    @Test
    public void testIsUpright() {
        System.out.println("isUpright");

        Cup instance  = new Cup(0, 0, true);
        boolean   expResult = true;
        boolean   result    = instance.isUpright();

        assertEquals(expResult, result);
    }

    /**
     * Test of knockOver method, of class Inanimate.
     */
    @Test
    public void testKnockOver() {
        System.out.println("knockOver");

        Cup instance  = new Cup(0, 0, true);
        boolean   expResult = true;
        boolean   result    = instance.knockOver();

        assertEquals(expResult, result);
    }

}



