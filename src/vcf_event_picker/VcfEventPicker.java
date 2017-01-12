package vcf_event_picker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Randomly selects events from a VCF file.
 * 
 * @author Eric-Wubbo Lameijer, Xi'an Jiaotong University, eric_wubbo@hotmail.com
 *
 */
public class VcfEventPicker {
	private static Map<EventSubtype,List<Integer>> eventCatalogue;
	
	/**
	 * Returns a list of event subtypes which have to be picked up from the VCF file.
	 * 
	 * @return a list of desired event subtypes
	 */
	private static List<EventSubtype> createEventSubtypeList() {
		final int MAX_EVENT_SIZE = 50;
		List<EventSubtype> output = new ArrayList<EventSubtype>();
		for (int eventSize = 1; eventSize <= MAX_EVENT_SIZE; ++eventSize) {
			output.add(new EventSubtype(EventType.INSERTION,eventSize,eventSize));
			output.add(new EventSubtype(EventType.DELETION,eventSize,eventSize));
		}
		return output;
	}
	
	
	public static void main(String[] args) {
		System.out.println("Picking events...");
		VcfReader vcfReader = new VcfReader(args[0]);
		List<EventSubtype> eventSubtypeList = createEventSubtypeList();
		eventCatalogue = new HashMap<EventSubtype,List<Integer>>();
		for (int subtypeIndex = 0; subtypeIndex < eventSubtypeList.size(); ++subtypeIndex) {
			eventCatalogue.put(eventSubtypeList.get(subtypeIndex), new ArrayList<Integer>());
		}
		
		int eventIndex = 0;
		while (vcfReader.hasNextEvent()) {
			Event event = vcfReader.getNextEvent();
			if (hasSoughtSubtype(event, eventSubtypeList)) {
				EventSubtype eventSubtype = getProperSubtype(event, eventSubtypeList);
				List<Integer> eventList = eventCatalogue.get(eventSubtype);
				//System.out.println(event.toString() + " added to list of size " + eventList.size());
				eventList.add(eventIndex);
			}
			++eventIndex;
		}
		Random randomGenerator = new Random();
	    final int NUMBER_OF_EVENTS_TO_PICK_PER_SUBTYPE = 1;
	    List<Integer> selectedEventIndices = new ArrayList<Integer>();  
	    
		for (int subtypeIndex = 0; subtypeIndex < eventSubtypeList.size(); ++subtypeIndex) {
			EventSubtype eventSubtype = eventSubtypeList.get(subtypeIndex);
			List<Integer> eventIndexList = eventCatalogue.get(eventSubtype);
			int numberOfevents = eventIndexList.size();
			
			System.out.println(eventSubtype.toString() + ": " + numberOfevents);
			Utilities.require(numberOfevents >= NUMBER_OF_EVENTS_TO_PICK_PER_SUBTYPE, 
					"VcfEventPicker.main error: warning: too few events of type " + eventSubtype);
			int firstListIndex = randomGenerator.nextInt(numberOfevents);
			/*int secondListIndex = randomGenerator.nextInt(numberOfevents - 1);
			if (secondListIndex >= firstListIndex) {
				++secondListIndex;
			}*/
			selectedEventIndices.add(eventIndexList.get(firstListIndex));
			//selectedEventIndices.add(eventIndexList.get(secondListIndex));
		}
		
		vcfReader.reopen();
		List<String> headerLines = vcfReader.getHeader();
		        BufferedWriter writer = null;
		        try {
		            File outputFile = new File(args[1]);

		            writer = new BufferedWriter(new FileWriter(outputFile));
		            for (String line: headerLines) {
		    			writer.write(line);
		    			writer.newLine();
		    		}
		            eventIndex = 0;
		            while (vcfReader.hasNextEvent()) {
		            	String line = vcfReader.getNextEventLine();
		            	if (selectedEventIndices.contains(eventIndex)) {
		            		writer.write(line);
		            		writer.newLine();
		            	}
		            	++eventIndex;
		            }
		            
		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		            try {
		                // Close the writer regardless of what happens...
		                writer.close();
		            } catch (Exception e) {
		            } // catch
		        } // finally
		
		
		
		
	}

/**
 * Returns the subtype of the event (the first matching subtype that occurs in the subtype list).
 * Breaks with an error if the event does not match any of the subtypes in the list.
 * 
 * @param event
 * 		the event to be matched
 * @param eventSubtypeList
 * 		the list of subtypes to be sought
 * @return
 * 		the (first) EventSubtype that is appropriate to classify the event.
 */
private static EventSubtype getProperSubtype(Event event, List<EventSubtype> eventSubtypeList) {
	for (int subtypeIndex = 0; subtypeIndex < eventSubtypeList.size(); ++subtypeIndex) {
		EventSubtype currentSubtype = eventSubtypeList.get(subtypeIndex);
		if (currentSubtype.matches(event)) {
			return currentSubtype;
		}
	}
	Utilities.require(false, 
			"VcfEventPicker.getProperSubtype error: event does not seem to have one of the desired subtypes.");
		return null;
	}


/**
 * Does the event have a subtype that occurs in the list of sough subtypes?
 * 
 * @param event 
 * 		the event to be checked
 * @param eventSubtypeList
 * 		the list of sought subtypes
 * @return
 * 		whether the event is member of the list of sought subtypes.
 */
	private static boolean hasSoughtSubtype(Event event, List<EventSubtype> eventSubtypeList) {
		for (int subtypeIndex = 0; subtypeIndex < eventSubtypeList.size(); ++subtypeIndex) {
			if (eventSubtypeList.get(subtypeIndex).matches(event)) {
				return true;
			}
		}
		return false;
	}

}
