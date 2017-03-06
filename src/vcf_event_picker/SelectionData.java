package vcf_event_picker;

/**
 * Basically gives the data of a selection: which events should be in the
 * selection, and how many.
 * 
 * @author Eric-Wubbo Lameijer, Xi'an Jiaotong University,
 *         eric_wubbo@hotmail.com
 *
 */
public class SelectionData {
  private EventSubtype eventSubtype;
  private int numberOfEvents;

  public SelectionData(EventSubtype inputEventSubtype, int inputNumberOfEvents) {
    Utilities.require(inputNumberOfEvents > 0,
        "SelectionData constructor error: the number of events given needs to be greater than 0.");
    eventSubtype = inputEventSubtype;
    numberOfEvents = inputNumberOfEvents;
  }

  public EventSubtype getEventSubtype() {
    return eventSubtype;
  }

  public int getNumberOfEvents() {
    return numberOfEvents;
  }
}
