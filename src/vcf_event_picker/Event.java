package vcf_event_picker;

/**
 * Represents a certain event
 * 
 * @author Eric-Wubbo Lameijer, Xi'an Jiaotong University, eric_wubbo@hotmail.com
 *
 */
public class Event {
	private EventSize size; // the size of the event
	private EventType type; // the type of the event (insertion, deletion or whatever)
	
	/**
	 * Event constructor
	 * 
	 * @param inputSize
	 * 		the size of the event
	 * @param inputType
	 * 		the type of the event
	 */
	Event(int inputSize, EventType inputType) {
		size = new EventSize(inputSize);
		type = inputType;
	}
	
	/**
	 * Returns the size of the event
	 * 
	 * @return the size of the event
	 */
	public EventSize getSize() {
		return size;
	}
	
	/**
	 * Returns the type of the event
	 * 
	 * @return the type of the event (insertion, deletion, etc.)
	 */
	public EventType getType() {
		return type;
	}
	
	/**
	 * Returns a string representation of an event.
	 */
	public String toString() {
		return type.toString() + " of " + size.asInteger();
	}

}
