package Main;
import java.io.File;

/**
 * 
 * @author Leon
 *
 *This class implements the noop approach.
 *noop means no operation, therefore this does not affect the program flow.
 *
 */
public class Noop implements IMutationReductionTechnique {

	/**
	 * Do nothing
	 */
	@Override
	public File[] reduceMutants(File[] files) {
		return files;
	}

}
