package de.draexlmaier.bpm.test.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.draexlmaier.bpm.test.entity.TheEntity;

@Stateless
@LocalBean
public class TheService
{
    @PersistenceContext
    private EntityManager em;

    public String readData(final long id)
    {
        return this.em.find(TheEntity.class, id).getValue();
    }
}
