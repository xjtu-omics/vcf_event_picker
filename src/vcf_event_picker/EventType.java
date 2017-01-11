package vcf_event_picker;

/**
 * EventType indicates the type of an event, whether it is a deletion, an insertion, or something else.
 * 
 * @author admin123
 *
 */

public enum EventType {
	DELETION("Deletion"), INSERTION("Insertion"), UNKNOWN("Unknown");
	
	String name;
	
	EventType(String eventName) {
		name = eventName;
	}
	
	public String toString() {
		return name;
	}
}
