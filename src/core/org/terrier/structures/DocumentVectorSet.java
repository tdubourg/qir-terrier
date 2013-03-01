package org.terrier.structures;

import java.util.ArrayList;

public class DocumentVectorSet {
	private int i_pointer;
	private ArrayList<DocumentVector> vectors;

	public DocumentVectorSet(){
		vectors = new ArrayList<>(100);
	}

	public void reset() {
		i_pointer = -1;
	}
	public boolean endOfVectorSet() {
		return i_pointer >= vectors.size();
	}
	public DocumentVector getNextVector() {
		i_pointer++;
		if (!endOfVectorSet()) {
			return vectors.get(i_pointer);
		} else {
			return null;
		}
	}

    public void insert(DocumentVector pushTerm) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

