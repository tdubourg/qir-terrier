/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terrier.structures;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author troll
 */
public class TermsToVectorsIndexBuilderTest {

    public TermsToVectorsIndexBuilderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    /**
     * Test of pushTerm method, of class TermsToVectorsIndexBuilder.
     */
    @Test public void testCircularFixedSizeBufferPush() {
        int capacity = 10;
        TermsToVectorsIndexBuilder.CircularFixedSizeBuffer<Integer> a = new TermsToVectorsIndexBuilder.CircularFixedSizeBuffer<>(capacity);
        Integer[] testData =  {42, 1, 2, 3, 4, 5, 6, 6, 7, 8, 9 };
        for (int i = 0; i < testData.length; i++) {
            a.push(testData[i]);
        }
        int i = testData.length  - capacity;
        for (Integer integer : a) {
            assertEquals(integer, testData[i]);
            i++;
        }
    }

    /**
     * Test of pushTerm method, of class TermsToVectorsIndexBuilder.
     */
    @Test
    public void testPushTerm() {
        System.out.println("pushTerm");
        int termid = 0;
        TermsToVectorsIndexBuilder instance = new TermsToVectorsIndexBuilder();
        Vector expResult = null;
        Vector result = instance.pushTerm(termid);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    
    
}
