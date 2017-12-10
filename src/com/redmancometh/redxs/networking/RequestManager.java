package com.redmancometh.redxs.networking;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.redmancometh.redxs.RedXS;
import com.redmancometh.redxs.requests.NetworkRequest;

import lilypad.client.connect.api.request.impl.MessageRequest;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestManager
{
    public Map<String, Class<? extends NetworkRequest>> classMapping = new ConcurrentHashMap();
    Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PROTECTED).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();

    public void registerRequest(String channel, Class<? extends NetworkRequest> requestClass)
    {
        this.classMapping.put(channel, requestClass);
    }

    public void receiveMessage(String channel, String string)
    {
        Class<? extends NetworkRequest> cl = classMapping.get(channel);
        if (cl != null)
        {
            NetworkRequest request = gson.fromJson(string, cl);
            if (request != null) request.receive(request);
        }
    }

    public void sendTo(NetworkRequest request, String target)
    {
        try
        {
            String jsonString = gson.toJson(request);
            MessageRequest mRequest = new MessageRequest(target, request.getChannel(), jsonString);
            RedXS.getConnect().request(mRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public Gson getGson()
    {
        return gson;
    }

    @SuppressWarnings("unused")
    private static class AtomicIntegerTypeAdapter implements JsonSerializer<AtomicInteger>, JsonDeserializer<AtomicInteger>
    {
        @Override
        public JsonElement serialize(AtomicInteger src, Type typeOfSrc, JsonSerializationContext context)
        {
            return new JsonPrimitive(src.incrementAndGet());
        }

        @Override
        public AtomicInteger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            int intValue = json.getAsInt();
            return new AtomicInteger(--intValue);
        }
    }
}
