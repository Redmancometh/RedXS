package com.redmancometh.redxs.config;

import javax.persistence.Entity;

/**
 * 
 * @author Redmancometh
 * 
 * In case we want to persist this into the db using hibernate use entity and ElementCollection.
 *
 */
@Entity(name = "rm_servers")
public class XSConfig
{
    private String instance;
    private String bind;
    private String redis;

    public String getInstance()
    {
        return instance;
    }

    public void setInstance(String instance)
    {
        this.instance = instance;
    }

    public String getBind()
    {
        return bind;
    }

    public void setBind(String bind)
    {
        this.bind = bind;
    }

    public String getRedis()
    {
        return redis;
    }

    public void setRedis(String redis)
    {
        this.redis = redis;
    }
}
