/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terrier.indexing;

import java.util.Iterator;
import org.terrier.structures.Vector;
import org.terrier.utility.ApplicationSetup;

public class TermsToVectorsIndexBuilder {
    private Vector createVectorFromBuffer() {
        Vector dv = new Vector();
        
        for (Integer termid : buffer) {
            
        }
        return dv;
    }
    //* This class is not multi-thread-proof

    protected class CircularFixedSizeBuffer<E> implements Iterable<E> {

        private java.util.concurrent.LinkedBlockingDeque deq;

        public CircularFixedSizeBuffer(int capacity) {
            deq = new java.util.concurrent.LinkedBlockingDeque<E>(capacity);
        }

        public void push(E element) {
            try {
                deq.add(element);
            } catch (IllegalStateException e) {
                deq.pop();
                deq.add(element);
            }
        }

        @Override
        public Iterator<E> iterator() {
            return deq.iterator();
        }
        
    }
    private CircularFixedSizeBuffer<Integer> buffer;

    public TermsToVectorsIndexBuilder() {
        buffer = new CircularFixedSizeBuffer<>(ApplicationSetup.WINDOW_SIZE);
    }

    public Vector pushTerm(int termid) {
        buffer.push(termid);
        Vector dv = createVectorFromBuffer();
        return dv;
    }
}