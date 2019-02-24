package org.cbioportal.G2Smutation.scripts;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.util.CommandProcessUtil;
import org.cbioportal.G2Smutation.util.ReadConfig;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.PDBFileReader;

public class PdbScriptsPipelineStructureAnnotation {
	static final Logger log = Logger.getLogger(PdbScriptsPipelineStarter.class);
	
	// If already have pdb files in workspace
	/**
     * run Naccess on a pdb file
     * 
     * @param input pdbfile ep. 2acf.pdb
     * @return .asa .log .rsa
     */
	public int runNaccessFromLocal(String filename) {
//		CommandProcessUtil cu = new CommandProcessUtil();
//		ArrayList<String> paralist = new ArrayList<String>();
//		paralist = new ArrayList<String>();
//        paralist.add(filename);
//        cu.runCommand("naccessRun", paralist);
		int shellReturnCode = 0;
		String commandName ="runNaccess";
		try {
			ProcessBuilder pb = null;
			log.info("[Naccess] Running naccess");
			pb = new ProcessBuilder(makdeNaccessRunCommand(filename));
			pb.directory(new File(ReadConfig.workspace));
			Process pc = pb.start();
            pc.waitFor();
            shellReturnCode = pc.exitValue();
            outputProcessError(pc, shellReturnCode, commandName);
            log.info("[SHELL] Command " + commandName + " completed");	
		}catch (Exception ex) {
            log.error("[SHELL] Fatal Error: Could not Successfully process command, exit the program now");
            log.error(ex.getMessage());
            ex.printStackTrace();
            System.exit(0);
        }
		return shellReturnCode;
	}
	
	/**
     * find burried residue in .rsa file
     * 
     * @param input pdb.rsa
     * @return pdb.showburied
     */
	public void generateBuriedAtomicFile(String rsafilename) throws IOException {
		String rsafilepwd = new String(ReadConfig.workspace + rsafilename);
		File rsafile = new File(rsafilepwd);
		String resfilepwd = new String(ReadConfig.workspace + rsafilename + ".showburied");
		File resfile = new File(resfilepwd);
		List<String> lines = FileUtils.readLines(rsafile, StandardCharsets.UTF_8.name());
		int linesNum = lines.size();
		//log.info(lines.size());
		HashMap<String, String> absHM = new HashMap<>();
		//float sum1 = 0, sum2 = 0;
		for(int i = linesNum - 1; i >= 0; i--) {
			//log.info(lines.get(i).split("\\s+")[0]);
			if(lines.get(i).split("\\s+")[0].equals("CHAIN")) {
				absHM.put(lines.get(i).split("\\s+")[2], lines.get(i).split("\\s+")[3]);
				//log.info(lines.get(i).split("\\s+")[2] + "  " + lines.get(i).split("\\s+")[3]);
			}
			if(lines.get(i).split("\\s+")[0].equals("RES")) {
				//log.info(Float.parseFloat(lines.get(i).split("\\s+")[4]));
				if(Float.parseFloat(lines.get(i).split("\\s+")[5]) < Float.parseFloat(ReadConfig.relativeRatio)) {
					lines.set(i, lines.get(i) + "      1");
				}
				else {
					lines.set(i, lines.get(i) + "      0");
				}
//				if(lines.get(i).split("\\s+")[2].equals("A")) {
//					sum1 += Float.parseFloat(lines.get(i).split("\\s+")[4]);
//					sum2 += Float.parseFloat(lines.get(i).split("\\s+")[5]);
//				}
			}
			if(lines.get(i).split("\\s+")[0].equals("REM") && lines.get(i).split("\\s+")[1].equals("ABS")){
				lines.set(i, lines.get(i) + "   IFBURIED");
			}
			else
				continue;
		}
//		log.info(sum1 + " " + sum2);
		FileUtils.writeLines(resfile, StandardCharsets.UTF_8.name(), lines);
	}
	
	private List<String> makdeNaccessRunCommand(String inFilename) {
    	List<String> list = new ArrayList<String>();
    	list.add(ReadConfig.workspace + ReadConfig.naccessRunFile);
    	list.add(ReadConfig.workspace + inFilename);
    	return list;
    }
	
    private void outputProcessError(Process process, int shellReturnCode, String commandName) {
        try {
            if (shellReturnCode != 0) {
                log.error("[Process] Process Error in " + commandName + ":" + process.toString());
                String errorInfo = "";
                InputStream error = process.getErrorStream();
                boolean done = false;
                while (!done) {
                    int buf = error.read();
                    if (buf == -1)
                        break;
                    errorInfo = errorInfo + (char) buf;
                }
                log.error("[Process] Error: " + errorInfo);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void getHETFromPdbFile(String filename) {
		try {
			PDBFileReader reader = new PDBFileReader();
			Structure struc = reader.getStructure(ReadConfig.workspace + filename);
			//struc.getPDBHeader();
			log.info(struc.getHetGroups());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
}
