/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sos.ingenieria;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author luis
 */
@Entity
@Table(name = "llamada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Llamada.findAll", query = "SELECT l FROM Llamada l"),
    @NamedQuery(name = "Llamada.findByLlamadaId", query = "SELECT l FROM Llamada l WHERE l.llamadaId = :llamadaId"),
    @NamedQuery(name = "Llamada.findByOrigen", query = "SELECT l FROM Llamada l WHERE l.origen = :origen"),
    @NamedQuery(name = "Llamada.findByDestino", query = "SELECT l FROM Llamada l WHERE l.destino = :destino"),
    @NamedQuery(name = "Llamada.findByNumeroReintentos", query = "SELECT l FROM Llamada l WHERE l.numeroReintentos = :numeroReintentos"),
    @NamedQuery(name = "Llamada.findByTiempoReintentos", query = "SELECT l FROM Llamada l WHERE l.tiempoReintentos = :tiempoReintentos")})
public class Llamada implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "llamada_id")
    private Integer llamadaId;
    @Size(max = 30)
    @Column(name = "origen")
    private String origen;
    @Size(max = 30)
    @Column(name = "destino")
    private String destino;
    @Column(name = "numero_reintentos")
    private Integer numeroReintentos;
    @Column(name = "tiempo_reintentos")
    private Integer tiempoReintentos;

    public Llamada() {
    }

    public Llamada(Integer llamadaId) {
        this.llamadaId = llamadaId;
    }

    public Integer getLlamadaId() {
        return llamadaId;
    }

    public void setLlamadaId(Integer llamadaId) {
        this.llamadaId = llamadaId;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Integer getNumeroReintentos() {
        return numeroReintentos;
    }

    public void setNumeroReintentos(Integer numeroReintentos) {
        this.numeroReintentos = numeroReintentos;
    }

    public Integer getTiempoReintentos() {
        return tiempoReintentos;
    }

    public void setTiempoReintentos(Integer tiempoReintentos) {
        this.tiempoReintentos = tiempoReintentos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (llamadaId != null ? llamadaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Llamada)) {
            return false;
        }
        Llamada other = (Llamada) object;
        if ((this.llamadaId == null && other.llamadaId != null) || (this.llamadaId != null && !this.llamadaId.equals(other.llamadaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sosingenieria.Llamada[ llamadaId=" + llamadaId + " ]";
    }
    
}
