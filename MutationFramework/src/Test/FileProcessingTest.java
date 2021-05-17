package Test;

import java.io.File;

import Main.FileProcessing;

public class FileProcessingTest {

	public static void main(String[] args) {
//		String testFileLoc = "E:\\java-2020-03\\Workspace\\MutationFramework\\src\\Test\\ATM.java";
		String testFileLoc = "E:\\java-2020-03\\Workspace\\MutationFramework\\src\\Test\\Account.java";
//		String testFileLoc = "E:\\java-2020-03\\Workspace\\MutationFramework\\src\\Test\\ATMPost.java";
//		String testFileLoc = "E:\\java-2020-03\\Workspace\\MutationFramework\\src\\Test\\MinimalTest.java";
//		String testFileLoc = "E:\\java-2020-03\\Workspace\\MutationFramework\\src\\Test\\Debug3.java";

		File testFile = new File(testFileLoc);
		
		FileProcessing.preProcess(testFile);
		FileProcessing.postProcess(testFile);
	}

}
