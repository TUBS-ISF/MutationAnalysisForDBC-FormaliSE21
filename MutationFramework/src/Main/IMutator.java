package Main;
import java.io.File;

public interface IMutator {
	public IMutationOperators ops = null;
	
	public File[] generateMutants(File[] files);
}
