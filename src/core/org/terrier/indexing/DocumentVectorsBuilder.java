/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terrier.indexing;

import org.terrier.structures.DocumentVector;
import org.terrier.utility.ApplicationSetup;

public class DocumentVectorsBuilder {
    //* This class is not multi-thread-proof

    protected class CircularFixedSizeBuffer<E> {

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
    }
    private CircularFixedSizeBuffer<Integer> buffer;

    public DocumentVectorsBuilder() {
        buffer = new CircularFixedSizeBuffer<>(ApplicationSetup.WINDOW_SIZE);
    }

    public DocumentVector pushTerm(int termid) {
        buffer.push(termid);
        return null;
    }
}