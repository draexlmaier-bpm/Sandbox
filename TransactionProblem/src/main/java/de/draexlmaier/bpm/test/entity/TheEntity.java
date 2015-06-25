package de.draexlmaier.bpm.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TheEntity
{
    @Id
//    @Column(name = "realid")
//    private long realId;
//
    @Column(name = "fakeid")
    private long id;

    private String value;

    public long getId()
    {
        return this.id;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setValue(final String value)
    {
        this.value = value;
    }

}
