package net.andresbustamante.yafoot.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author andresbustamante
 */
@Entity
@Table(name = "t_match")
@NamedQueries({
        @NamedQuery(name = "Match.findIdByCode", query = "SELECT mat.id FROM Match mat WHERE mat.code = :codeMatch")})
@SequenceGenerator(name = "s_match", sequenceName = "s_match", allocationSize = 1)
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "mat_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_match")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mat_date")
    @Temporal(TemporalType.DATE)
    private Date dateMatch;
    @Basic(optional = false)
    @NotNull
    @Size(max = 12)
    @Column(name = "mat_code")
    private String code;
    @Column(name = "mat_description")
    private String description;
    @Column(name = "mat_num_joueurs_min")
    private Integer numJoueursMin;
    @Column(name = "mat_num_joueurs_max")
    private Integer numJoueursMax;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "match")
    private List<JoueurMatch> joueursMatch;
    @JoinColumn(name = "mat_site_fk", referencedColumnName = "sit_id")
    @ManyToOne(optional = false)
    private Site site;

    public Match() {
    }

    public Match(Integer id) {
        this.id = id;
    }

    public Match(Integer id, Date dateMatch) {
        this.id = id;
        this.dateMatch = dateMatch;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer matId) {
        this.id = matId;
    }

    public Date getDateMatch() {
        return dateMatch;
    }

    public void setDateMatch(Date matDate) {
        this.dateMatch = matDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String matDescription) {
        this.description = matDescription;
    }

    public Integer getNumJoueursMin() {
        return numJoueursMin;
    }

    public void setNumJoueursMin(Integer matNumJoueursMin) {
        this.numJoueursMin = matNumJoueursMin;
    }

    public Integer getNumJoueursMax() {
        return numJoueursMax;
    }

    public void setNumJoueursMax(Integer matNumJoueursMax) {
        this.numJoueursMax = matNumJoueursMax;
    }

    public List<JoueurMatch> getJoueursMatch() {
        return joueursMatch;
    }

    public void setJoueursMatch(List<JoueurMatch> joueurMatchList) {
        this.joueursMatch = joueurMatchList;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Match)) {
            return false;
        }
        Match other = (Match) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.andresbustamante.yafoot.model.Match[ matId=" + id + " ]";
    }

}
