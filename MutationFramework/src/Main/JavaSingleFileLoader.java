package Main;
import java.io.File;

public class JavaSingleFileLoader implements IDataLoader{

	@Override
	public File[] loadData(String path) {
		File[] files = new File[1];
		files[0] = new File(path);
		return files;
	}

}
