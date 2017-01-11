package vcf_event_picker;

public class VcfEventPicker {
	public static void main(String[] args) {
		System.out.println("Picking events...");
		VcfReader vcfReader = new VcfReader(args[0]);
		while (vcfReader.hasNextEvent()) {
			System.out.println(vcfReader.getNextEvent());
		}
	}

}
