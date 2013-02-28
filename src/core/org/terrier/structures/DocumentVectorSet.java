package org.terrier.structures;

public class DocumentVectorSet {
	public void reset() {

	}
	public boolean endOfVectorSet() {
		return false;
	}
	public DocumentVector getNextVector() {
		return null;
	}

    public void insert(DocumentVector pushTerm) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

