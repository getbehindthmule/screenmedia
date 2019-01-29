package com.greenhills.fuel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

// cannot use lombok here because of an issue with Jackson unable to construct an instance from lombok
public class UkWeeklyPriceDetailsCsv {
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate localDate;
    private BigDecimal unleadedPricePerLitre;
    private BigDecimal dieselPricePerLitre;
    private BigDecimal unleadedDutyPerLitre;
    private BigDecimal dieselDutyPerLitre;
    private BigDecimal unleadedVat;
    private BigDecimal dieselVat;


    public UkWeeklyPriceDetailsCsv() {
    }

    public UkWeeklyPriceDetailsCsv(LocalDate localDate, BigDecimal unleadedPricePerLitre, BigDecimal dieselPricePerLitre, BigDecimal unleadedDutyPerLitre, BigDecimal dieselDutyPerLitre, BigDecimal unleadedVat, BigDecimal dieselVat) {
        this.localDate = localDate;
        this.unleadedPricePerLitre = unleadedPricePerLitre;
        this.dieselPricePerLitre = dieselPricePerLitre;
        this.unleadedDutyPerLitre = unleadedDutyPerLitre;
        this.dieselDutyPerLitre = dieselDutyPerLitre;
        this.unleadedVat = unleadedVat;
        this.dieselVat = dieselVat;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public BigDecimal getUnleadedPricePerLitre() {
        return unleadedPricePerLitre;
    }

    public void setUnleadedPricePerLitre(BigDecimal unleadedPricePerLitre) {
        this.unleadedPricePerLitre = unleadedPricePerLitre;
    }

    public BigDecimal getDieselPricePerLitre() {
        return dieselPricePerLitre;
    }

    public void setDieselPricePerLitre(BigDecimal dieselPricePerLitre) {
        this.dieselPricePerLitre = dieselPricePerLitre;
    }

    public BigDecimal getUnleadedDutyPerLitre() {
        return unleadedDutyPerLitre;
    }

    public void setUnleadedDutyPerLitre(BigDecimal unleadedDutyPerLitre) {
        this.unleadedDutyPerLitre = unleadedDutyPerLitre;
    }

    public BigDecimal getDieselDutyPerLitre() {
        return dieselDutyPerLitre;
    }

    public void setDieselDutyPerLitre(BigDecimal dieselDutyPerLitre) {
        this.dieselDutyPerLitre = dieselDutyPerLitre;
    }

    public BigDecimal getUnleadedVat() {
        return unleadedVat;
    }

    public void setUnleadedVat(BigDecimal unleadedVat) {
        this.unleadedVat = unleadedVat;
    }

    public BigDecimal getDieselVat() {
        return dieselVat;
    }

    public void setDieselVat(BigDecimal dieselVat) {
        this.dieselVat = dieselVat;
    }


    @Override
    public String toString() {
        return "UkWeeklyPriceDetailsCsv{" +
                "localDate=" + localDate +
                ", unleadedPricePerLitre=" + unleadedPricePerLitre +
                ", dieselPricePerLitre=" + dieselPricePerLitre +
                ", unleadedDutyPerLitre=" + unleadedDutyPerLitre +
                ", dieselDutyPerLitre=" + dieselDutyPerLitre +
                ", unleadedVat=" + unleadedVat +
                ", dieselVat=" + dieselVat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UkWeeklyPriceDetailsCsv that = (UkWeeklyPriceDetailsCsv) o;
        return Objects.equals(localDate, that.localDate) &&
                Objects.equals(unleadedPricePerLitre, that.unleadedPricePerLitre) &&
                Objects.equals(dieselPricePerLitre, that.dieselPricePerLitre) &&
                Objects.equals(unleadedDutyPerLitre, that.unleadedDutyPerLitre) &&
                Objects.equals(dieselDutyPerLitre, that.dieselDutyPerLitre) &&
                Objects.equals(unleadedVat, that.unleadedVat) &&
                Objects.equals(dieselVat, that.dieselVat);
    }

    @Override
    public int hashCode() {

        return Objects.hash(localDate, unleadedPricePerLitre, dieselPricePerLitre, unleadedDutyPerLitre, dieselDutyPerLitre, unleadedVat, dieselVat);
    }
}
