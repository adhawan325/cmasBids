package com.cmasbids.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Bid.
 */
@Entity
@Table(name = "bid")
@Document(indexName = "bid")
public class Bid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bid_number", nullable = false)
    private String bidNumber;

    @NotNull
    @Column(name = "bid_name", nullable = false)
    private String bidName;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Lob
    @Column(name = "bid_sow")
    private String bidSOW;

    @Lob
    @Column(name = "bid_m_qs")
    private String bidMQs;

    @Lob
    @Column(name = "bid_d_qs")
    private String bidDQs;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_on", nullable = false)
    private LocalDate createdOn;

    @NotNull
    @Column(name = "modified_on", nullable = false)
    private LocalDate modifiedOn;

    @ManyToOne(optional = false)
    @NotNull
    private Department department;

    @ManyToMany
    @JoinTable(name = "bid_vendor",
               joinColumns = @JoinColumn(name="bids_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="vendors_id", referencedColumnName="id"))
    private Set<Vendor> vendors = new HashSet<>();

    @ManyToMany(mappedBy = "bids")
    @JsonIgnore
    private Set<Candidate> candidates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBidNumber() {
        return bidNumber;
    }

    public Bid bidNumber(String bidNumber) {
        this.bidNumber = bidNumber;
        return this;
    }

    public void setBidNumber(String bidNumber) {
        this.bidNumber = bidNumber;
    }

    public String getBidName() {
        return bidName;
    }

    public Bid bidName(String bidName) {
        this.bidName = bidName;
        return this;
    }

    public void setBidName(String bidName) {
        this.bidName = bidName;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Bid endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getBidSOW() {
        return bidSOW;
    }

    public Bid bidSOW(String bidSOW) {
        this.bidSOW = bidSOW;
        return this;
    }

    public void setBidSOW(String bidSOW) {
        this.bidSOW = bidSOW;
    }

    public String getBidMQs() {
        return bidMQs;
    }

    public Bid bidMQs(String bidMQs) {
        this.bidMQs = bidMQs;
        return this;
    }

    public void setBidMQs(String bidMQs) {
        this.bidMQs = bidMQs;
    }

    public String getBidDQs() {
        return bidDQs;
    }

    public Bid bidDQs(String bidDQs) {
        this.bidDQs = bidDQs;
        return this;
    }

    public void setBidDQs(String bidDQs) {
        this.bidDQs = bidDQs;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Bid createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public Bid createdOn(LocalDate createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDate getModifiedOn() {
        return modifiedOn;
    }

    public Bid modifiedOn(LocalDate modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public void setModifiedOn(LocalDate modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Department getDepartment() {
        return department;
    }

    public Bid department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Vendor> getVendors() {
        return vendors;
    }

    public Bid vendors(Set<Vendor> vendors) {
        this.vendors = vendors;
        return this;
    }

    public Bid addVendor(Vendor vendor) {
        this.vendors.add(vendor);
        vendor.getBids().add(this);
        return this;
    }

    public Bid removeVendor(Vendor vendor) {
        this.vendors.remove(vendor);
        vendor.getBids().remove(this);
        return this;
    }

    public void setVendors(Set<Vendor> vendors) {
        this.vendors = vendors;
    }

    public Set<Candidate> getCandidates() {
        return candidates;
    }

    public Bid candidates(Set<Candidate> candidates) {
        this.candidates = candidates;
        return this;
    }

    public Bid addCandidate(Candidate candidate) {
        this.candidates.add(candidate);
        candidate.getBids().add(this);
        return this;
    }

    public Bid removeCandidate(Candidate candidate) {
        this.candidates.remove(candidate);
        candidate.getBids().remove(this);
        return this;
    }

    public void setCandidates(Set<Candidate> candidates) {
        this.candidates = candidates;
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
        Bid bid = (Bid) o;
        if (bid.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bid.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Bid{" +
            "id=" + getId() +
            ", bidNumber='" + getBidNumber() + "'" +
            ", bidName='" + getBidName() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", bidSOW='" + getBidSOW() + "'" +
            ", bidMQs='" + getBidMQs() + "'" +
            ", bidDQs='" + getBidDQs() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", modifiedOn='" + getModifiedOn() + "'" +
            "}";
    }
}
