package com.redmancometh.redxs.requests;

public interface NetworkRequest<T>
{
    String getChannel();

    void receive(T e);

}
