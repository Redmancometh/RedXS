package com.redmancometh.redxs;

import com.redmancometh.redcore.RedPlugin;
import com.redmancometh.redcore.config.ConfigManager;
import com.redmancometh.redcore.mediators.ObjectManager;
import com.redmancometh.redxs.commands.ReloadCommand;
import com.redmancometh.redxs.config.XSConfig;
import com.redmancometh.redxs.networking.*;

import lilypad.client.connect.api.Connect;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RedXS extends JavaPlugin implements RedPlugin
{
    // The SessionFactory is the hibernate factory which provides us sessions to query objects
    private SessionFactory factory;
    // The classList is a list of the classes we need SubDatabases for. 
    // Let's try and limit this to once a plugin, and not test if it works properly with mutliples.
    private List<Class> classList = new CopyOnWriteArrayList();
    // ExampleManager is the ObjectManager for this class. 
    // It satisfies the abstract method RedPlugin.getManager()
    // This is the gson-backed config manager. 
    // Very easy to use, and provides our reload, get config, etc methods.
    private NulledObjectManager objectManager = new NulledObjectManager();
    private ConfigManager<XSConfig> configManager;
    private RequestManager requestManager;
    private String serverName;
    NetworkListener listener;

    public void onEnable()
    {
        requestManager = new RequestManager();
        // First thing to do is instnatiate the instances of all of our objects
        this.configManager = new ConfigManager("servers.json", XSConfig.class);
        // We want to persist ExampleObject, so we need a subdatabase for it. 
        // Always initialize the configManager before calling RedPlugin.enable()!
        configManager.init(this);
        // Always call enable after adding all persisteed classes to the classList
        // Init will initialize the SubDatabases, and get hibernate set up for this plugin
        this.enable();
        Connect connect = this.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        setServerName(connect.getSettings().getUsername());
        listener = new NetworkListener();
        connect.registerEvents(listener);
        getCommand("xsreload").setExecutor(new ReloadCommand());

    }

    @Override
    public void onDisable()
    {
        // First call bukkit's onEnable method
        super.onDisable();
        // Now call RedCore's disable method to wipe the plugin from it's maps, and power down all the hibernate stuff
        this.disable();
        Connect connect = this.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        connect.unregisterEvents(listener);
    }

    public static String getCurrentServer()
    {
        return getInstance().serverName;
    }

    public void reloadConfig()
    {
        // ConfigManager.init(JavaPlugin plugin) can also be called to reload the plugin.
        // The SlowPollerTask in RedCore will automatically refresh this every 6000 ticks (5 minutes)
        configManager.init(this);
    }

    public static Connect getConnect()
    {
        return Bukkit.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
    }

    public SessionFactory getFactory()
    {
        return factory;
    }

    public void setFactory(SessionFactory factory)
    {
        this.factory = factory;
    }

    public List<Class> getClassList()
    {
        return classList;
    }

    public void setClassList(List<Class> classList)
    {
        this.classList = classList;
    }

    public static XSConfig getCfg()
    {
        return getInstance().getConfigManager().getCurrentConfig();
    }

    public static RedXS getInstance()
    {
        return JavaPlugin.getPlugin(RedXS.class);
    }

    @Override
    public JavaPlugin getBukkitPlugin()
    {
        return this;
    }

    @Override
    public SessionFactory getInternalFactory()
    {
        return factory;
    }

    @Override
    public List<Class> getMappedClasses()
    {
        return classList;
    }

    @Override
    public void setInternalFactory(SessionFactory newFactory)
    {
        this.factory = newFactory;
    }

    public ConfigManager<XSConfig> getConfigManager()
    {
        return configManager;
    }

    public void setConfigManager(ConfigManager<XSConfig> configManager)
    {
        this.configManager = configManager;
    }

    // This can be called from any RedPlugin.
    // To get a RedPlugin instance from the Bukkit plugin
    // Use RedCore.getInstance().get
    @Override
    public ObjectManager getManager()
    {
        return objectManager;
    }

    public RequestManager getRequestManager()
    {
        return requestManager;
    }

    public void setRequestManager(RequestManager requestManager)
    {
        this.requestManager = requestManager;
    }

    public String getServerName()
    {
        return serverName;
    }

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

}
