package ro.cristiansterie.vote.faceid.dto;

public class FaceVerificationResult {

    private boolean match;
    private Double distance;
    private Double threshold;
    private String model;
    private String similarity_metric;
    private String referenceBase64;

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSimilarity_metric() {
        return similarity_metric;
    }

    public void setSimilarity_metric(String similarity_metric) {
        this.similarity_metric = similarity_metric;
    }

    public String getReferenceBase64() {
        return referenceBase64;
    }

    public void setReferenceBase64(String referenceBase64) {
        this.referenceBase64 = referenceBase64;
    }

    @Override
    public String toString() {
        return "FaceVerificationResult [match=" + match + ", distance=" + distance + ", threshold=" + threshold
                + ", model=" + model + ", similarity_metric=" + similarity_metric + ", referenceBase64="
                + referenceBase64.substring(0, 13) + "]";
    }
}
