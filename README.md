# vcf_event_picker

Purpose: from an input VCF file, picks a subset of events for validation. This subset (or subsets) are defined in a text file, with lines like "INS 1 5 10" meaning picking 10 insertions, any of which can be 1,2,3,4 and 5 basepairs long. Use DEL to indicate deletions.

Usage: java -jar vcf_event_picker.jar input_vcf eventtypes_txt selected_events_vcf
Example: java -jar vcf_event_picker.jar pacbio_only_insertions.vcf small_ins.txt pacbio_only_insertions_selection.vcf

Contact data: Eric-Wubbo Lameijer, Xi'an Jiaotong University, eric_wubbo@hotmail.com

