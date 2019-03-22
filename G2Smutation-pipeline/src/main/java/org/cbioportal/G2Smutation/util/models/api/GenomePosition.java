package org.cbioportal.G2Smutation.util.models.api;

public class GenomePosition {
    private String chromosome;
    private int start;
    private int end;
    private String referenceAllele;
    private String variantAllele;
    public String getChromosome() {
        return chromosome;
    }
    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getEnd() {
        return end;
    }
    public void setEnd(int end) {
        this.end = end;
    }
    public String getReferenceAllele() {
        return referenceAllele;
    }
    public void setReferenceAllele(String referenceAllele) {
        this.referenceAllele = referenceAllele;
    }
    public String getVariantAllele() {
        return variantAllele;
    }
    public void setVariantAllele(String variantAllele) {
        this.variantAllele = variantAllele;
    }
    
    

}
