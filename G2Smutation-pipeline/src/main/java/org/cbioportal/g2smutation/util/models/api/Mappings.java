package org.cbioportal.g2smutation.util.models.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * Main Content Model for coordinate API return in JSON format
 * 
 * https://grch37.rest.ensembl.org/map/translation/ENSP00000356671.3/167..167?
 * content-type=application/json
 * 
 * @author Juexin Wang
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Mappings {

    private String assembly_name;
    private String seq_region_name;
    private String start;
    private String end;

    Mappings() {

    }

    public String getAssembly_name() {
        return assembly_name;
    }

    public void setAssembly_name(String assembly_name) {
        this.assembly_name = assembly_name;
    }

    public String getSeq_region_name() {
        return seq_region_name;
    }

    public void setSeq_region_name(String seq_region_name) {
        this.seq_region_name = seq_region_name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Quote [Mappings=" + assembly_name + " " + seq_region_name + " " + start + " " + end + "]";
    }

}
