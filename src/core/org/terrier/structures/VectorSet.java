package org.terrier.structures;

import java.util.ArrayList;

public class VectorSet {
	private int i_pointer;
	private ArrayList<Vector> vectors;

	public VectorSet(){
		vectors = new ArrayList<>(100);
	}

	public void reset() {
		i_pointer = -1;
	}
	public boolean endOfVectorSet() {
		return i_pointer >= vectors.size();
	}
	public Vector getNextVector() {
		i_pointer++;
		if (!endOfVectorSet()) {
			return vectors.get(i_pointer);
		} else {
			return null;
		}
	}

    public VectorSet insert(Vector vec) {
        vectors.add(vec);
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

