package org.cbioportal.G2Smutation.util.blast;

/**
 * Model of Blast database
 * 
 * @author Juexin Wang
 *
 */
public class BlastDataBase {
    public String name;
    public String dbName;
    public String resultfileName;

    public BlastDataBase(String subject) {
        this.name = subject.replaceAll("\\..*","");         // Remove file extension
        this.name = this.name.replaceAll(".fasta","");
        this.dbName = this.name + ".db";
        this.resultfileName = this.name + ".xml";
    }
}
