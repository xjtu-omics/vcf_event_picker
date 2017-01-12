package vcf_event_picker;

/**
 * The size of an event (must be greater than zero)
 * 
 * @author Eric-Wubbo Lameijer, Xi'an Jiaotong University, eric_wubbo@hotmail.com
 *
 */
public class EventSize {

	private int size; // the size of an event.
	
	/**
	 * EventSize constructor
	 * 
	 * @param inputSize
	 * 		the size of the event (1..infinite)
	 */
	EventSize(int inputSize) {
		Utilities.require(inputSize > 0, 
				"EventSize constructor error: the size of an event must be greater than zero");
		size = inputSize;
	}
	
	/**
	 * Returns the size of the event as an integer. Useful for calculations and comparisons.
	 * 
	 * @return the size of the event as an integer.
	 */
	public int asInteger() {
		return size;
	}
}
