package vcf_event_picker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Randomly selects events from a VCF file.
 * 
 * @author Eric-Wubbo Lameijer, Xi'an Jiaotong University,
 *         eric_wubbo@hotmail.com
 *
 */
public class VcfEventPicker {
  private static Map<EventSubtype, List<Integer>> eventCatalogue;

  /**
   * Returns a list of event subtypes which have to be picked up from the VCF
   * file.
   * 
   * @return a list of desired event subtypes
   */
  private static List<SelectionData> createSelectionDataList(String eventFilename) {
    List<SelectionData> output = new ArrayList<SelectionData>();
    try (BufferedReader reader = new BufferedReader(new FileReader(eventFilename))) {
      String line;
      while ((line = reader.readLine()) != null) {
        // ignore comment lines, only handle lines that do not start with "#"
        if (!line.startsWith("#") && !line.equals("")) {
          String[] lineParts = line.split("\\s+");
          if (hasCorrectFormat(lineParts)) {
            SelectionData selection = parseEvent(lineParts);
            if (overlaps(selection.getEventSubtype(), output)) {
              System.out.println("The event described as '" + line + "' overlaps with another event. "
                  + "Please correct the input file. Exiting.");
              System.exit(-1);
            }
            output.add(selection);
          } else {
            System.out.println("Line '" + line + "' is of incorrect format. Lines should either be "
                + "comment lines starting with #, or be like 'INS 1 10' (or 'DEL 5 5'). Exiting.");
            System.exit(-1);
          }
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.out.println("File " + eventFilename + " not found. Exiting.");
      System.exit(-1);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error reading file " + eventFilename + ". Exiting.");
      System.exit(-1);
    }

    return output;
  }

  private static boolean overlaps(EventSubtype eventSubtype, List<SelectionData> output) {
    for (SelectionData selectionData : output) {
      if (eventSubtype.overlapsWith(selectionData.getEventSubtype())) {
        return true;
      }
    }
    return false;
  }

  private static SelectionData parseEvent(String[] lineParts) {
    Utilities.require(hasCorrectFormat(lineParts), "VcfEventPicker.parseEvent error: the input string does not have "
        + "the correct format. Ensure that 'hasCorrectFormat' has been used before .");
    String svTypeAsString = lineParts[0];
    EventType eventType;
    if (svTypeAsString.equals("INS")) {
      eventType = EventType.INSERTION;
    } else {
      eventType = EventType.DELETION;
    }
    int startPos = Integer.parseInt(lineParts[1]);
    int endPos = Integer.parseInt(lineParts[2]);
    int selectionSize = Integer.parseInt(lineParts[3]);
    System.out.println(startPos + " " + endPos + " " + selectionSize);

    return new SelectionData(new EventSubtype(eventType, startPos, endPos), selectionSize);
  }

  /**
   * Is this string of the right format for an event line, like INS 2 6
   * 
   * @param lineParts
   * @return
   */
  private static boolean hasCorrectFormat(String[] lineParts) {
    if (lineParts.length != 4) {
      return false;
    }
    String svType = lineParts[0];
    if (!svType.equals("INS") && !svType.equals("DEL")) {
      return false;
    }
    int startPos = Integer.parseInt(lineParts[1]);
    int endPos = Integer.parseInt(lineParts[2]);
    int numberOfEvents = Integer.parseInt(lineParts[3]);
    if (startPos <= 0 || endPos <= 0 || endPos < startPos || numberOfEvents <= 0) {
      return false;
    }
    return true;
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println(
          "vcf_event_picker\n" + "\n" + "Purpose: from an input VCF file, picks a subset of events for validation. "
              + "This subset (or subsets) are defined in a text file, with lines like \"INS 1 5 10\" meaning "
              + "picking 10 insertions, any of which can be 1,2,3,4 and 5 basepairs long. Use DEL to indicate deletions."
              + "\n\n" + "Usage: java -jar vcf_event_picker.jar input_vcf eventtypes_txt selected_events_vcf\n"
              + "Example: java -jar vcf_event_picker.jar pacbio_only_insertions.vcf small_ins.txt pacbio_only_insertions_selection.vcf\n"
              + "\n" + "Contact data: Eric-Wubbo Lameijer, Xi'an Jiaotong University, eric_wubbo@hotmail.com\n");
      System.exit(-1);

    }

    System.out.println("Picking events...");

    List<SelectionData> selectionDataList = createSelectionDataList(args[1]);
    eventCatalogue = new HashMap<EventSubtype, List<Integer>>();
    for (int subtypeIndex = 0; subtypeIndex < selectionDataList.size(); ++subtypeIndex) {
      eventCatalogue.put(selectionDataList.get(subtypeIndex).getEventSubtype(), new ArrayList<Integer>());
    }

    System.out.println("A1...");
    int eventIndex = 0;
    VcfReader vcfReader = new VcfReader(args[0]);
    while (vcfReader.hasNextEvent()) {
      Event event = vcfReader.getNextEvent();
      if (hasSoughtSubtype(event, selectionDataList)) {
        EventSubtype eventSubtype = getProperSubtype(event, selectionDataList);
        List<Integer> eventList = eventCatalogue.get(eventSubtype);
        System.out.println(event.toString() + " added to list of size " + eventList.size());
        eventList.add(eventIndex);
      }
      ++eventIndex;
    }
    System.out.println("A2...");

    Random randomGenerator = new Random();
    List<Integer> selectedEventIndices = new ArrayList<Integer>();

    for (int subtypeIndex = 0; subtypeIndex < selectionDataList.size(); ++subtypeIndex) {
      EventSubtype eventSubtype = selectionDataList.get(subtypeIndex).getEventSubtype();
      List<Integer> eventIndexList = eventCatalogue.get(eventSubtype);
      int numberOfEvents = eventIndexList.size();
      int numberOfEventsToPick = selectionDataList.get(subtypeIndex).getNumberOfEvents();

      System.out.println(eventSubtype.toString() + ": " + numberOfEvents);
      Utilities.require(numberOfEvents >= numberOfEventsToPick,
          "VcfEventPicker.main error: warning: too few events of type " + eventSubtype);

      for (int pick = 0; pick < numberOfEventsToPick; ++pick) {
        int indexOfPick = randomGenerator.nextInt(numberOfEvents);
        selectedEventIndices.add(eventIndexList.get(indexOfPick));
        eventIndexList.remove(indexOfPick);
        --numberOfEvents;
      }
      /*
       * int secondListIndex = randomGenerator.nextInt(numberOfevents - 1); if
       * (secondListIndex >= firstListIndex) { ++secondListIndex; }
       */

      // selectedEventIndices.add(eventIndexList.get(secondListIndex));
    }

    System.out.println("A3...");
    vcfReader.reopen();
    List<String> headerLines = vcfReader.getHeader();
    BufferedWriter writer = null;
    try {
      File outputFile = new File(args[2]);

      writer = new BufferedWriter(new FileWriter(outputFile));
      for (String line : headerLines) {
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
   * Returns the subtype of the event (the first matching subtype that occurs in
   * the subtype list). Breaks with an error if the event does not match any of
   * the subtypes in the list.
   * 
   * @param event
   *          the event to be matched
   * @param selectionDataList
   *          the list of subtypes to be sought
   * @return the (first) EventSubtype that is appropriate to classify the event.
   */
  private static EventSubtype getProperSubtype(Event event, List<SelectionData> selectionDataList) {
    for (int subtypeIndex = 0; subtypeIndex < selectionDataList.size(); ++subtypeIndex) {
      EventSubtype currentSubtype = selectionDataList.get(subtypeIndex).getEventSubtype();
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
   *          the event to be checked
   * @param selectionDataList
   *          the list of sought subtypes
   * @return whether the event is member of the list of sought subtypes.
   */
  private static boolean hasSoughtSubtype(Event event, List<SelectionData> selectionDataList) {
    for (int subtypeIndex = 0; subtypeIndex < selectionDataList.size(); ++subtypeIndex) {
      if (selectionDataList.get(subtypeIndex).getEventSubtype().matches(event)) {
        return true;
      }
    }
    return false;
  }

}
