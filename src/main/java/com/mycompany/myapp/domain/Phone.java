package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.mycompany.myapp.domain.enumeration.Os;

/**
 * A Phone.
 */
@Entity
@Table(name = "phone")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 0, max = 20)
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "os")
    private Os os;

    @Column(name = "promoting")
    private Boolean promoting;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Phone name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public Phone price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Os getOs() {
        return os;
    }

    public Phone os(Os os) {
        this.os = os;
        return this;
    }

    public void setOs(Os os) {
        this.os = os;
    }

    public Boolean isPromoting() {
        return promoting;
    }

    public Phone promoting(Boolean promoting) {
        this.promoting = promoting;
        return this;
    }

    public void setPromoting(Boolean promoting) {
        this.promoting = promoting;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Phone)) {
            return false;
        }
        return id != null && id.equals(((Phone) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Phone{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", os='" + getOs() + "'" +
            ", promoting='" + isPromoting() + "'" +
            "}";
    }
}
