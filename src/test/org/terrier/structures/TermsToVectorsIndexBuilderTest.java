/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terrier.structures;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.terrier.utility.ApplicationSetup;

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
        System.out.println("CircularFixedSizedBuffer:Push()");
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
     * Test scenario:
     * the document is like "blorg blah bleh" with those having termid 1, 2 and 3 respectively
     * we have a window of size 2
     * The result should be like:
     * {
     *  1: [{1:1, 2:1}],
     *  2: [{1:1, 2:1}, {2:1, 3:1}],
     *  3: [{2:1, 3:1}]
     * }
     */
    @Test
    public void testPushTerm() {
        System.out.println("pushTerm");
        TermsToVectorsIndexBuilder instance = new TermsToVectorsIndexBuilder();
        ApplicationSetup.WINDOW_SIZE = 2;// easier... but we will need to write another test for other values as well
        instance.pushTerm(1);
        instance.pushTerm(2);
        instance.pushTerm(3);
        TermsToVectorsIndex ttvi = instance.getBuiltIndex();
        Map<Integer, VectorSet> expectedMap = new ConcurrentHashMap<>();
        expectedMap.put(1, new VectorSet().insert(new Vector().pushNewTerm(2).pushNewTerm(1))); // vector [2:1, 1:1] for term of id 1
        expectedMap.put(2, new VectorSet().insert(new Vector().pushNewTerm(2).pushNewTerm(3))); // vector [2:1, 3:1] for term of id 2
        expectedMap.put(2, new VectorSet().insert(new Vector().pushNewTerm(2).pushNewTerm(3))); // vector [2:1, 3:1] for term of id 2
//        Object[] expectedEntries = {new Map.Entry<Integer, VectorSet>() {};//  I hate Java Generics Crap, gimme true templates, for fuck sake!
//        int i = 0;
//        for (Map.Entry<Integer, VectorSet> entry : ttvi) {
//            assertEquals((Map.Entry<Integer, VectorSet>)expectedEntries[i], entry);
//            i++;
//        }
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    
    
}
