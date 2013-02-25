package org.terrier.matching.models;

/**
* This class implements a custom WeightingModel to test how easy it is
 * @author tdubourg
*/
public class MyWM extends WeightingModel {
	public static final String NAME = "MyWM";
	/**
	 * Returns the name of the model.
	 * @return java.lang.String
	 */
	public final String getInfo() {
		return NAME;
	}
	/**
	 * This method provides the contract for implementing weighting models.
	 * @param tf The term frequency in the document
	 * @param docLength the document's length
	 * @return the score assigned to a document with the given tf 
	 * and docLength, and other preset parameters
	 */
	public final double score(double tf, double docLength) {
		return 1.0;
	}
	/**
	 * This method provides the contract for implementing weighting models.
	 * @param tf The term frequency in the document
	 * @param docLength the document's length
	 * @param n_t The document frequency of the term
	 * @param F_t the term frequency in the collection
	 * @param _keyFrequency the term frequency in the query
	 * @return the score returned by the implemented weighting model.
	 */
	public final double score(
		double tf,
		double docLength,
		double n_t,
		double F_t,
		double _keyFrequency) {
		return 1.0;
	}
}