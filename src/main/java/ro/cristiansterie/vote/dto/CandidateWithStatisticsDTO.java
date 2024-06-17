package ro.cristiansterie.vote.dto;

public class CandidateWithStatisticsDTO extends CandidateDTO {
    private Long totalVotes;

    public Long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }
}
