package vcf_event_picker;

/**
 * EventType indicates the type of an event, whether it is a deletion, an insertion, or something else.
 * 
 * @author Eric-Wubbo Lameijer, Xi'an Jiaotong University, eric_wubbo@hotmail.com
 *
 */

public enum EventType {
	DELETION("Deletion"), INSERTION("Insertion"), UNKNOWN("Unknown");
	
	String name; // the (printable) name of the event
	
	/**
	 * EventType constructor
	 * 
	 * @param eventName 
	 * 		the name of the event.
	 */
	EventType(String eventName) {
		name = eventName;
	}
	
	/**
	 * A method to return a printable representation of the event.
	 */
	public String toString() {
		return name;
	}
}
