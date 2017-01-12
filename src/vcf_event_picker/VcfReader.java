package vcf_event_picker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Object that reads a VCF file and can return both the header and the events contained within the file.
 * 
 * @author Eric-Wubbo Lameijer, Xi'an Jiaotong University, eric_wubbo@hotmail.com
 *
 */
public class VcfReader {
	private List<String> header; // the comment lines that form the header.
	private BufferedReader reader; // the object that is used to read the events
	private String nextEventLine; // the line containing the next event (starts with containing the first event)
	private String filename; // the name of the VCF file.
	
	/**
	 * Constructor
	 * 
	 * @param filename
	 * 	The name of the VCF file.
	 */
public VcfReader(String inputFilename) {
	System.out.println("Opening " + inputFilename);
	filename = inputFilename;
	init();
}

/**
 * Initializes the data
 */
private void init() {
	header = new ArrayList<String>();
	  try
	  {
	    reader = new BufferedReader(new FileReader(filename));
	    String line;
	    
	    
	    while ((line = reader.readLine()) != null)
	    {
//System.out.println(line);
	    	if (line.startsWith("#")) {
	    		header.add(line);
	    	} else {
	    		// apparently, we've reached the first event
	    		nextEventLine = line;
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
	return (nextEventLine != null);
}

/**
 * Returns the next event
 * 
 * @return the next event in the VCF file.
 */
public Event getNextEvent() {
	String[] eventDescriptors = nextEventLine.split("\\t");
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
	readNextLine();

	return new Event(eventSizeAsInteger, eventType);
}

/** 
 * Reads the next line
 */
private void readNextLine() {
	try {
		nextEventLine = reader.readLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

/**
 * Returns the next event as a line of text
 * 
 * @return 
 * 		the next event as a line of text (in VCF format)
 */
public String getNextEventLine() {
	String line = nextEventLine;
	readNextLine();
	return line;	
}

/** Closes the file. **/
public void close() {
	try {
		reader.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

/** Reopens the file, so that it can be run through for a second time. **/
public void reopen() {
	close();
	init();
}

	
}
