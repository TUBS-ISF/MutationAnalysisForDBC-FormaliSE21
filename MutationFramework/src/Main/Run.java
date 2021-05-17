package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import src.mujava.makeMuJavaStructure;

public class Run {
	public static void main(String args[]) {
		Framework fw = new Framework();
		fw.run(new JavaSingleFileLoader(), new KeYMethodEvaluator(), new Noop(), null, new JavaMethodCodeMutator(), new KeYReport(), "F:\\Homeoffice\\Abschlussarbeiten\\masterarbeit-leon-schaer\\MutationFramework\\src\\MutationFrameworkConfig");
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		PrintWriter writer;
		try {
			String projectpath = Framework.getTargetConfigString("ProjectPath");
			String projectName = new File(projectpath).getName();
			String source = Framework.getTargetConfigString("Source");
			String sourceName = new File(source).getName();
			sourceName = sourceName.split("\\.")[0];
			String method = Framework.getTargetConfigString("Method");
			File newLog = new File(".\\src\\Logs" + File.separator + projectName + File.separator + sourceName + File.separator + method.replace("[\\(\\)]", "_") + "_" + timeStamp.replaceAll("\\.", "_"));
			newLog.getParentFile().mkdirs();
			writer = new PrintWriter(newLog);
			writer.print(fw.report.print());
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
