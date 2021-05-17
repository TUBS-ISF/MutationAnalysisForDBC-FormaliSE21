package Main;

public interface IReport {
	public double mutationScore = 0;
	public int mutationCount = 0;
	public int killedMutants = 0;
	
	public String print();
	
}
