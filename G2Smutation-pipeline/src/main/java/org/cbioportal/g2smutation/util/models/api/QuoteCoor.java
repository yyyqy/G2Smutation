package org.cbioportal.g2smutation.util.models.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Main Model Model for coordinate API return in JSON format
 * 
 * e.g.
 * https://grch37.rest.ensembl.org/map/translation/ENSP00000356671.3/167..167?
 * content-type=application/json
 * 
 * @author Juexin Wang
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteCoor {

    private List<Mappings> mappings;

    public QuoteCoor() {
    }

    public List<Mappings> getMappings() {
        return mappings;
    }

    public void setMappings(List<Mappings> mappings) {
        this.mappings = mappings;
    }

    @Override
    public String toString() {
        return "QuoteCoor [mappings=" + mappings + "]";
    }

}
