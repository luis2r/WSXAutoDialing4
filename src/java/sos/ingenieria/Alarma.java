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
import javax.persistence.Lob;
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
@Table(name = "alarma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alarma.findAll", query = "SELECT a FROM Alarma a"),
    @NamedQuery(name = "Alarma.findByAlarmaId", query = "SELECT a FROM Alarma a WHERE a.alarmaId = :alarmaId"),
    @NamedQuery(name = "Alarma.findByDestino", query = "SELECT a FROM Alarma a WHERE a.destino = :destino"),
    @NamedQuery(name = "Alarma.findByNumeroReintentos", query = "SELECT a FROM Alarma a WHERE a.numeroReintentos = :numeroReintentos"),
    @NamedQuery(name = "Alarma.findByTiempoReintentos", query = "SELECT a FROM Alarma a WHERE a.tiempoReintentos = :tiempoReintentos")})
public class Alarma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "alarma_id")
    private Integer alarmaId;
    @Lob
    @Size(max = 65535)
    @Column(name = "mensaje")
    private String mensaje;
    @Size(max = 30)
    @Column(name = "destino")
    private String destino;
    @Column(name = "numero_reintentos")
    private Integer numeroReintentos;
    @Column(name = "tiempo_reintentos")
    private Integer tiempoReintentos;

    public Alarma() {
    }

    public Alarma(Integer alarmaId) {
        this.alarmaId = alarmaId;
    }

    public Integer getAlarmaId() {
        return alarmaId;
    }

    public void setAlarmaId(Integer alarmaId) {
        this.alarmaId = alarmaId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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
        hash += (alarmaId != null ? alarmaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alarma)) {
            return false;
        }
        Alarma other = (Alarma) object;
        if ((this.alarmaId == null && other.alarmaId != null) || (this.alarmaId != null && !this.alarmaId.equals(other.alarmaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sosingenieria.Alarma[ alarmaId=" + alarmaId + " ]";
    }
    
}
