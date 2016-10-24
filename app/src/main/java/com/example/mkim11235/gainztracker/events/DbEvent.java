package com.example.mkim11235.gainztracker.events;

/**
 * Created by Michael on 10/24/2016.
 */

public abstract class DbEvent {
    protected DbOperationType mDbOperationType;

    public enum DbOperationType {
        ADD, DELETE, UPDATE
    }
}
