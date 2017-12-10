package com.redmancometh.redxs.networking;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.redmancometh.redcore.mediators.ObjectManager;
import com.redmancometh.redcore.util.SpecialFuture;
import com.redmancometh.redxs.RedXS;

public class NulledObjectManager extends ObjectManager
{

    public NulledObjectManager()
    {
        super(RedXS.class);
    }

    @Override
    public SpecialFuture getRecord(UUID uuid)
    {
        return SpecialFuture.supplyAsync(() -> null);

    }

    @Override
    public SpecialFuture save(Player p)
    {
        return SpecialFuture.supplyAsync(() -> null);
    }

    @Override
    public SpecialFuture saveAndPurge(Player p)
    {
        return SpecialFuture.supplyAsync(() -> null);

    }
}
