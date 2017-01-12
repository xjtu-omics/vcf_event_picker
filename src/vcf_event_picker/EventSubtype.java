package vcf_event_picker;

/**
 * EventSubtype represents a subtype of events, say insertions between 6 and 12 bases long.
 * 
 * @author Eric-Wubbo Lameijer, Xi'an Jiaotong University, eric_wubbo@hotmail.com
 *
 */
public class EventSubtype {
	private EventType eventType; // the type of event
	private EventSize minSize; // the minimum size of the event (in number of bases)
	private EventSize maxSize; // the maximum size of the event (in number of bases)
	
	/**
	 * EventSubtype constructor. Constructs an event of type 'inputEventType', with a size
	 * range of [inputMinSize,inputMaxSize] (inclusive range, so 3,4 will results in all events
	 * of size 3 or 4 to be included)
	 * 
	 * @param inputEventType 
	 * 		the type of the event, say insertion or deletion.
	 * @param inputMinSize 
	 * 		the minimum size of the event (must be greater than zero).
	 * @param inputMaxSize 
	 * 		the maximum size of the event (must be greater than zero, and greater or equal to the minimum size)
	 */
	public EventSubtype(EventType inputEventType, int inputMinSize, int inputMaxSize) {
		Utilities.require(inputMinSize <= inputMaxSize, 
				"EventSubtype constructor error: the specified minimum size is larger than the specified maximum size.");
		eventType = inputEventType;
		minSize = new EventSize(inputMinSize);
		maxSize = new EventSize(inputMaxSize);
	}
	
	/**
	 * Returns whether the given event belongs to this event subtype.
	 * 
	 * @param event 
	 * 		the event to be checked
	 * @return
	 * 		whether the event belongs to this event subtype.
	 */
	public boolean matches(Event event) {
		return ((event.getType() == eventType) && 
				(event.getSize().asInteger() >= minSize.asInteger()) &&
				(event.getSize().asInteger() <= maxSize.asInteger()));
	}
	
	/**
	 * Returns the event type as a string
	 */
	public String toString() {
		return eventType.toString() + " of minimum size " + minSize.asInteger() + " and maximum size " + maxSize.asInteger();
	}
}
