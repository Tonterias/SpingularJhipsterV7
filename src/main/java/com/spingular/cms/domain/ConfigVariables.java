package com.spingular.cms.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ConfigVariables.
 */
@Entity
@Table(name = "config_variables")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConfigVariables implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "config_var_long_1")
    private Long configVarLong1;

    @Column(name = "config_var_long_2")
    private Long configVarLong2;

    @Column(name = "config_var_long_3")
    private Long configVarLong3;

    @Column(name = "config_var_long_4")
    private Long configVarLong4;

    @Column(name = "config_var_long_5")
    private Long configVarLong5;

    @Column(name = "config_var_long_6")
    private Long configVarLong6;

    @Column(name = "config_var_long_7")
    private Long configVarLong7;

    @Column(name = "config_var_long_8")
    private Long configVarLong8;

    @Column(name = "config_var_long_9")
    private Long configVarLong9;

    @Column(name = "config_var_long_10")
    private Long configVarLong10;

    @Column(name = "config_var_long_11")
    private Long configVarLong11;

    @Column(name = "config_var_long_12")
    private Long configVarLong12;

    @Column(name = "config_var_long_13")
    private Long configVarLong13;

    @Column(name = "config_var_long_14")
    private Long configVarLong14;

    @Column(name = "config_var_long_15")
    private Long configVarLong15;

    @Column(name = "config_var_boolean_16")
    private Boolean configVarBoolean16;

    @Column(name = "config_var_boolean_17")
    private Boolean configVarBoolean17;

    @Column(name = "config_var_boolean_18")
    private Boolean configVarBoolean18;

    @Column(name = "config_var_string_19")
    private String configVarString19;

    @Column(name = "config_var_string_20")
    private String configVarString20;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConfigVariables id(Long id) {
        this.id = id;
        return this;
    }

    public Long getConfigVarLong1() {
        return this.configVarLong1;
    }

    public ConfigVariables configVarLong1(Long configVarLong1) {
        this.configVarLong1 = configVarLong1;
        return this;
    }

    public void setConfigVarLong1(Long configVarLong1) {
        this.configVarLong1 = configVarLong1;
    }

    public Long getConfigVarLong2() {
        return this.configVarLong2;
    }

    public ConfigVariables configVarLong2(Long configVarLong2) {
        this.configVarLong2 = configVarLong2;
        return this;
    }

    public void setConfigVarLong2(Long configVarLong2) {
        this.configVarLong2 = configVarLong2;
    }

    public Long getConfigVarLong3() {
        return this.configVarLong3;
    }

    public ConfigVariables configVarLong3(Long configVarLong3) {
        this.configVarLong3 = configVarLong3;
        return this;
    }

    public void setConfigVarLong3(Long configVarLong3) {
        this.configVarLong3 = configVarLong3;
    }

    public Long getConfigVarLong4() {
        return this.configVarLong4;
    }

    public ConfigVariables configVarLong4(Long configVarLong4) {
        this.configVarLong4 = configVarLong4;
        return this;
    }

    public void setConfigVarLong4(Long configVarLong4) {
        this.configVarLong4 = configVarLong4;
    }

    public Long getConfigVarLong5() {
        return this.configVarLong5;
    }

    public ConfigVariables configVarLong5(Long configVarLong5) {
        this.configVarLong5 = configVarLong5;
        return this;
    }

    public void setConfigVarLong5(Long configVarLong5) {
        this.configVarLong5 = configVarLong5;
    }

    public Long getConfigVarLong6() {
        return this.configVarLong6;
    }

    public ConfigVariables configVarLong6(Long configVarLong6) {
        this.configVarLong6 = configVarLong6;
        return this;
    }

    public void setConfigVarLong6(Long configVarLong6) {
        this.configVarLong6 = configVarLong6;
    }

    public Long getConfigVarLong7() {
        return this.configVarLong7;
    }

    public ConfigVariables configVarLong7(Long configVarLong7) {
        this.configVarLong7 = configVarLong7;
        return this;
    }

    public void setConfigVarLong7(Long configVarLong7) {
        this.configVarLong7 = configVarLong7;
    }

    public Long getConfigVarLong8() {
        return this.configVarLong8;
    }

    public ConfigVariables configVarLong8(Long configVarLong8) {
        this.configVarLong8 = configVarLong8;
        return this;
    }

    public void setConfigVarLong8(Long configVarLong8) {
        this.configVarLong8 = configVarLong8;
    }

    public Long getConfigVarLong9() {
        return this.configVarLong9;
    }

    public ConfigVariables configVarLong9(Long configVarLong9) {
        this.configVarLong9 = configVarLong9;
        return this;
    }

    public void setConfigVarLong9(Long configVarLong9) {
        this.configVarLong9 = configVarLong9;
    }

    public Long getConfigVarLong10() {
        return this.configVarLong10;
    }

    public ConfigVariables configVarLong10(Long configVarLong10) {
        this.configVarLong10 = configVarLong10;
        return this;
    }

    public void setConfigVarLong10(Long configVarLong10) {
        this.configVarLong10 = configVarLong10;
    }

    public Long getConfigVarLong11() {
        return this.configVarLong11;
    }

    public ConfigVariables configVarLong11(Long configVarLong11) {
        this.configVarLong11 = configVarLong11;
        return this;
    }

    public void setConfigVarLong11(Long configVarLong11) {
        this.configVarLong11 = configVarLong11;
    }

    public Long getConfigVarLong12() {
        return this.configVarLong12;
    }

    public ConfigVariables configVarLong12(Long configVarLong12) {
        this.configVarLong12 = configVarLong12;
        return this;
    }

    public void setConfigVarLong12(Long configVarLong12) {
        this.configVarLong12 = configVarLong12;
    }

    public Long getConfigVarLong13() {
        return this.configVarLong13;
    }

    public ConfigVariables configVarLong13(Long configVarLong13) {
        this.configVarLong13 = configVarLong13;
        return this;
    }

    public void setConfigVarLong13(Long configVarLong13) {
        this.configVarLong13 = configVarLong13;
    }

    public Long getConfigVarLong14() {
        return this.configVarLong14;
    }

    public ConfigVariables configVarLong14(Long configVarLong14) {
        this.configVarLong14 = configVarLong14;
        return this;
    }

    public void setConfigVarLong14(Long configVarLong14) {
        this.configVarLong14 = configVarLong14;
    }

    public Long getConfigVarLong15() {
        return this.configVarLong15;
    }

    public ConfigVariables configVarLong15(Long configVarLong15) {
        this.configVarLong15 = configVarLong15;
        return this;
    }

    public void setConfigVarLong15(Long configVarLong15) {
        this.configVarLong15 = configVarLong15;
    }

    public Boolean getConfigVarBoolean16() {
        return this.configVarBoolean16;
    }

    public ConfigVariables configVarBoolean16(Boolean configVarBoolean16) {
        this.configVarBoolean16 = configVarBoolean16;
        return this;
    }

    public void setConfigVarBoolean16(Boolean configVarBoolean16) {
        this.configVarBoolean16 = configVarBoolean16;
    }

    public Boolean getConfigVarBoolean17() {
        return this.configVarBoolean17;
    }

    public ConfigVariables configVarBoolean17(Boolean configVarBoolean17) {
        this.configVarBoolean17 = configVarBoolean17;
        return this;
    }

    public void setConfigVarBoolean17(Boolean configVarBoolean17) {
        this.configVarBoolean17 = configVarBoolean17;
    }

    public Boolean getConfigVarBoolean18() {
        return this.configVarBoolean18;
    }

    public ConfigVariables configVarBoolean18(Boolean configVarBoolean18) {
        this.configVarBoolean18 = configVarBoolean18;
        return this;
    }

    public void setConfigVarBoolean18(Boolean configVarBoolean18) {
        this.configVarBoolean18 = configVarBoolean18;
    }

    public String getConfigVarString19() {
        return this.configVarString19;
    }

    public ConfigVariables configVarString19(String configVarString19) {
        this.configVarString19 = configVarString19;
        return this;
    }

    public void setConfigVarString19(String configVarString19) {
        this.configVarString19 = configVarString19;
    }

    public String getConfigVarString20() {
        return this.configVarString20;
    }

    public ConfigVariables configVarString20(String configVarString20) {
        this.configVarString20 = configVarString20;
        return this;
    }

    public void setConfigVarString20(String configVarString20) {
        this.configVarString20 = configVarString20;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigVariables)) {
            return false;
        }
        return id != null && id.equals(((ConfigVariables) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfigVariables{" +
            "id=" + getId() +
            ", configVarLong1=" + getConfigVarLong1() +
            ", configVarLong2=" + getConfigVarLong2() +
            ", configVarLong3=" + getConfigVarLong3() +
            ", configVarLong4=" + getConfigVarLong4() +
            ", configVarLong5=" + getConfigVarLong5() +
            ", configVarLong6=" + getConfigVarLong6() +
            ", configVarLong7=" + getConfigVarLong7() +
            ", configVarLong8=" + getConfigVarLong8() +
            ", configVarLong9=" + getConfigVarLong9() +
            ", configVarLong10=" + getConfigVarLong10() +
            ", configVarLong11=" + getConfigVarLong11() +
            ", configVarLong12=" + getConfigVarLong12() +
            ", configVarLong13=" + getConfigVarLong13() +
            ", configVarLong14=" + getConfigVarLong14() +
            ", configVarLong15=" + getConfigVarLong15() +
            ", configVarBoolean16='" + getConfigVarBoolean16() + "'" +
            ", configVarBoolean17='" + getConfigVarBoolean17() + "'" +
            ", configVarBoolean18='" + getConfigVarBoolean18() + "'" +
            ", configVarString19='" + getConfigVarString19() + "'" +
            ", configVarString20='" + getConfigVarString20() + "'" +
            "}";
    }
}
