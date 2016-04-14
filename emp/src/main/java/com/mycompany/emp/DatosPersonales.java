/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.emp;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ciro
 */
@Entity
@Table(name = "datos_personales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosPersonales.findAll", query = "SELECT d FROM DatosPersonales d"),
    @NamedQuery(name = "DatosPersonales.findByIdEmp", query = "SELECT d FROM DatosPersonales d WHERE d.idEmp = :idEmp"),
    @NamedQuery(name = "DatosPersonales.findByApellido1", query = "SELECT d FROM DatosPersonales d WHERE d.apellido1 = :apellido1"),
    @NamedQuery(name = "DatosPersonales.findByApellido2", query = "SELECT d FROM DatosPersonales d WHERE d.apellido2 = :apellido2"),
    @NamedQuery(name = "DatosPersonales.findByNombre1", query = "SELECT d FROM DatosPersonales d WHERE d.nombre1 = :nombre1"),
    @NamedQuery(name = "DatosPersonales.findByNombre2", query = "SELECT d FROM DatosPersonales d WHERE d.nombre2 = :nombre2"),
    @NamedQuery(name = "DatosPersonales.findByFechaNac", query = "SELECT d FROM DatosPersonales d WHERE d.fechaNac = :fechaNac"),
    @NamedQuery(name = "DatosPersonales.findByGenero", query = "SELECT d FROM DatosPersonales d WHERE d.genero = :genero")})
public class DatosPersonales implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_emp")
    private Integer idEmp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "apellido1")
    private String apellido1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "apellido2")
    private String apellido2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre1")
    private String nombre1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre2")
    private String nombre2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_nac")
    @Temporal(TemporalType.DATE)
    private Date fechaNac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "genero")
    private char genero;

    public DatosPersonales() {
    }

    public DatosPersonales(Integer idEmp) {
        this.idEmp = idEmp;
    }

    public DatosPersonales(Integer idEmp, String apellido1, String apellido2, String nombre1, String nombre2, Date fechaNac, char genero) {
        this.idEmp = idEmp;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;
        this.fechaNac = fechaNac;
        this.genero = genero;
    }

    public Integer getIdEmp() {
        return idEmp;
    }

    public void setIdEmp(Integer idEmp) {
        this.idEmp = idEmp;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmp != null ? idEmp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatosPersonales)) {
            return false;
        }
        DatosPersonales other = (DatosPersonales) object;
        if ((this.idEmp == null && other.idEmp != null) || (this.idEmp != null && !this.idEmp.equals(other.idEmp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.emp.DatosPersonales[ idEmp=" + idEmp + " ]";
    }
    
}
