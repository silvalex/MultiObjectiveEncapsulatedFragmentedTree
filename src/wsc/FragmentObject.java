package wsc;

public class FragmentObject {
	public int frequency;
	public double totalAvailability;
	public double totalReliability;
	public double totalTime;
	public double totalCost;

	public FragmentObject(int frequency, double totalAvailability, double totalReliability, double totalTime, double totalCost) {
		this.frequency = frequency;
		this.totalAvailability = totalAvailability;
		this.totalReliability = totalReliability;
		this.totalTime = totalTime;
		this.totalCost = totalCost;
	}
}
