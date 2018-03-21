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
 * A Vendor.
 */
@Entity
@Table(name = "vendor")
@Document(indexName = "vendor")
public class Vendor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "vendor_name", nullable = false)
    private String vendorName;

    @NotNull
    @Column(name = "vendor_contact", nullable = false)
    private String vendorContact;

    @NotNull
    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Lob
    @Column(name = "vendor_notes")
    private String vendorNotes;

    @NotNull
    @Column(name = "vendor_end_date", nullable = false)
    private LocalDate vendorEndDate;

    @ManyToMany(mappedBy = "vendors")
    @JsonIgnore
    private Set<Bid> bids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendorName() {
        return vendorName;
    }

    public Vendor vendorName(String vendorName) {
        this.vendorName = vendorName;
        return this;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorContact() {
        return vendorContact;
    }

    public Vendor vendorContact(String vendorContact) {
        this.vendorContact = vendorContact;
        return this;
    }

    public void setVendorContact(String vendorContact) {
        this.vendorContact = vendorContact;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public Vendor contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public Vendor contactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
        return this;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getVendorNotes() {
        return vendorNotes;
    }

    public Vendor vendorNotes(String vendorNotes) {
        this.vendorNotes = vendorNotes;
        return this;
    }

    public void setVendorNotes(String vendorNotes) {
        this.vendorNotes = vendorNotes;
    }

    public LocalDate getVendorEndDate() {
        return vendorEndDate;
    }

    public Vendor vendorEndDate(LocalDate vendorEndDate) {
        this.vendorEndDate = vendorEndDate;
        return this;
    }

    public void setVendorEndDate(LocalDate vendorEndDate) {
        this.vendorEndDate = vendorEndDate;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public Vendor bids(Set<Bid> bids) {
        this.bids = bids;
        return this;
    }

    public Vendor addBid(Bid bid) {
        this.bids.add(bid);
        bid.getVendors().add(this);
        return this;
    }

    public Vendor removeBid(Bid bid) {
        this.bids.remove(bid);
        bid.getVendors().remove(this);
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
        Vendor vendor = (Vendor) o;
        if (vendor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vendor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vendor{" +
            "id=" + getId() +
            ", vendorName='" + getVendorName() + "'" +
            ", vendorContact='" + getVendorContact() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", contactPhone='" + getContactPhone() + "'" +
            ", vendorNotes='" + getVendorNotes() + "'" +
            ", vendorEndDate='" + getVendorEndDate() + "'" +
            "}";
    }
}
