package vcf_event_picker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VcfReader {
	private List<String> header;
	private BufferedReader reader;
	private String currentEventLine;
	
public VcfReader(String filename) {
	System.out.println("Opening " + filename);
	header = new ArrayList<String>();
	  try
	  {
	    reader = new BufferedReader(new FileReader(filename));
	    String line;
	    
	    
	    while ((line = reader.readLine()) != null)
	    {
System.out.println(line);
	    	if (line.startsWith("#")) {
	    		header.add(line);
	    	} else {
	    		// apparently, we've reached the first event
	    		currentEventLine = line;
	    		break;
	    	}
	    }
	    
	  }
	  catch (Exception e)
	  {
	    System.err.format("Exception occurred trying to read '%s'.", filename);
	    e.printStackTrace();
	  }
}

/**
 * returns the header of the VCF file
 *
 * @return the header of the VCF file
 */
public List<String> getHeader() {
	return header;
}

/**
 * Returns whether there is a next event in the VCF file.
 * 
 * @return whether there is a next event in the VCF file.
 */
public boolean hasNextEvent() {
	return (currentEventLine != null);
}

/**
 * Returns the next event
 * 
 * @return the next event in the VCF file.
 */
public Event getNextEvent() {
	String[] eventDescriptors = currentEventLine.split("\\t");
	String referenceAllele = eventDescriptors[3];
	String altAllele = eventDescriptors[4];
	int refSize = referenceAllele.length();
	int altSize = altAllele.length();
	EventType eventType;
	int eventSizeAsInteger;
	if (refSize == 1 && altSize > 1) {
		eventType = EventType.INSERTION;
		eventSizeAsInteger = altSize - refSize;
	} else if (refSize > 1 && altSize == 1) {
		eventType = EventType.DELETION;
		eventSizeAsInteger = refSize - altSize;
	} else {
		eventType = EventType.UNKNOWN;
		eventSizeAsInteger = 1;
	}
	
	try {
		currentEventLine = reader.readLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return new Event(eventSizeAsInteger, eventType);
}

public void close() {
	try {
		reader.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	
}
