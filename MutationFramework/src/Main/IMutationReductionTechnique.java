package Main;
import java.io.File;

public interface IMutationReductionTechnique {
	public long RandomSeed = 42;
	
	public File[] reduceMutants(File[] files);
}
