package vcf_event_picker;

public class EventSubclass {
	private EventType eventType;
	private EventSize minSize;
	private EventSize maxSize;
	
	public EventSubclass(EventType inputEventType, int inputMinSize, int inputMaxSize) {
		eventType = inputEventType;
		minSize = new EventSize(inputMinSize);
		maxSize = new EventSize(inputMaxSize);
	}
	
	public boolean matches(Event event) {
		return ((event.getType() == eventType) && 
				(event.getSize().asInteger() >= minSize.asInteger()) &&
				(event.getSize().asInteger() <= maxSize.asInteger()));
	}
}
