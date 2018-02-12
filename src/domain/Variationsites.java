package domain;

public class Variationsites {
	private int VarID;
	private int SampleID;
	private byte Chr;
	private int Length;
	private int SampleValue;
	private float Conf;
	private String vComment;
	private int CNVIndex;
	private String Cytobands;
	private int MarkersNo;
	private String Genes;
	
	public int getVarID() {
		return VarID;
	}
	public void setVarID(int varID) {
		VarID = varID;
	}
	public int getSampleID() {
		return SampleID;
	}
	public void setSampleID(int sampleID) {
		SampleID = sampleID;
	}
	public byte getChr() {
		return Chr;
	}
	public void setChr(byte chr) {
		Chr = chr;
	}
	public int getLength() {
		return Length;
	}
	public void setLength(int length) {
		Length = length;
	}
	public int getSampleValue() {
		return SampleValue;
	}
	public void setSampleValue(int sampleValue) {
		SampleValue = sampleValue;
	}
	public float getConf() {
		return Conf;
	}
	public void setConf(float conf) {
		Conf = conf;
	}
	public String getvComment() {
		return vComment;
	}
	public void setvComment(String vComment) {
		this.vComment = vComment;
	}
	public int getCNVIndex() {
		return CNVIndex;
	}
	public void setCNVIndex(int cNVIndex) {
		CNVIndex = cNVIndex;
	}
	public String getCytobands() {
		return Cytobands;
	}
	public void setCytobands(String cytobands) {
		Cytobands = cytobands;
	}
	public int getMarkersNo() {
		return MarkersNo;
	}
	public void setMarkersNo(int markersNo) {
		MarkersNo = markersNo;
	}
	public String getGenes() {
		return Genes;
	}
	public void setGenes(String genes) {
		Genes = genes;
	}	
}
