package com.cmasbids.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Candidate.
 */
@Entity
@Table(name = "candidate")
@Document(indexName = "candidate")
public class Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "candidate_name", nullable = false)
    private String candidateName;

    @Column(name = "meets_m_qs")
    private Boolean meetsMQs;

    @Column(name = "meets_d_qs")
    private Boolean meetsDQs;

    @NotNull
    @Lob
    @Column(name = "resume", nullable = false)
    private byte[] resume;

    @Column(name = "resume_content_type", nullable = false)
    private String resumeContentType;

    @NotNull
    @Column(name = "rate_per_hour", nullable = false)
    private Double ratePerHour;

    @ManyToMany
    @JoinTable(name = "candidate_bid",
               joinColumns = @JoinColumn(name="candidates_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="bids_id", referencedColumnName="id"))
    private Set<Bid> bids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public Candidate candidateName(String candidateName) {
        this.candidateName = candidateName;
        return this;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public Boolean isMeetsMQs() {
        return meetsMQs;
    }

    public Candidate meetsMQs(Boolean meetsMQs) {
        this.meetsMQs = meetsMQs;
        return this;
    }

    public void setMeetsMQs(Boolean meetsMQs) {
        this.meetsMQs = meetsMQs;
    }

    public Boolean isMeetsDQs() {
        return meetsDQs;
    }

    public Candidate meetsDQs(Boolean meetsDQs) {
        this.meetsDQs = meetsDQs;
        return this;
    }

    public void setMeetsDQs(Boolean meetsDQs) {
        this.meetsDQs = meetsDQs;
    }

    public byte[] getResume() {
        return resume;
    }

    public Candidate resume(byte[] resume) {
        this.resume = resume;
        return this;
    }

    public void setResume(byte[] resume) {
        this.resume = resume;
    }

    public String getResumeContentType() {
        return resumeContentType;
    }

    public Candidate resumeContentType(String resumeContentType) {
        this.resumeContentType = resumeContentType;
        return this;
    }

    public void setResumeContentType(String resumeContentType) {
        this.resumeContentType = resumeContentType;
    }

    public Double getRatePerHour() {
        return ratePerHour;
    }

    public Candidate ratePerHour(Double ratePerHour) {
        this.ratePerHour = ratePerHour;
        return this;
    }

    public void setRatePerHour(Double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public Candidate bids(Set<Bid> bids) {
        this.bids = bids;
        return this;
    }

    public Candidate addBid(Bid bid) {
        this.bids.add(bid);
        bid.getCandidates().add(this);
        return this;
    }

    public Candidate removeBid(Bid bid) {
        this.bids.remove(bid);
        bid.getCandidates().remove(this);
        return this;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        if (candidate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), candidate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Candidate{" +
            "id=" + getId() +
            ", candidateName='" + getCandidateName() + "'" +
            ", meetsMQs='" + isMeetsMQs() + "'" +
            ", meetsDQs='" + isMeetsDQs() + "'" +
            ", resume='" + getResume() + "'" +
            ", resumeContentType='" + getResumeContentType() + "'" +
            ", ratePerHour=" + getRatePerHour() +
            "}";
    }
}
