package vcf_event_picker;

public class EventSize {

	private int size;
	
	EventSize(int inputSize) {
		Utilities.require(inputSize > 0, 
				"EventSize constructor error: the size of an event must be greater than zero");
		size = inputSize;
	}
	
	public int asInteger() {
		return size;
	}
}
