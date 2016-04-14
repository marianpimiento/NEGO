/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.emp;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ciro
 */
@Stateless
public class DatosPersonalesFacade extends AbstractFacade<DatosPersonales> {
    @PersistenceContext(unitName = "com.mycompany_emp_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DatosPersonalesFacade() {
        super(DatosPersonales.class);
    }
    
}
