package vcf_event_picker;

/**
 * Represents a certain event
 * 
 * @author Eric-Wubbo Lameijer, Xi'an Jiaotong University, eric_wubbo@hotmail.com
 *
 */
public class Event {
	private EventSize size;
	private EventType type;
	
	Event(int inputSize, EventType inputType) {
		size = new EventSize(inputSize);
		type = inputType;
	}
	
	public EventSize getSize() {
		return size;
	}
	
	public EventType getType() {
		return type;
	}
	
	public String toString() {
		return type.toString() + " of " + size.asInteger();
	}

}
