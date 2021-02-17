package com.mindlinksoft.recruitment.mychat.models;

import java.util.Collection;

/**
 * Represents the model of a conversation.
 */
public final class Conversation {
    /**
     * The name of the conversation.
     */
    public String name;

    /**
     * The messages in the conversation.
     */
    public Collection<Message> messages;

    /**
     * Initializes a new instance of the {@link Conversation} class.
     * 
     * @param name     The name of the conversation.
     * @param messages The messages in the conversation.
     */
    public Conversation(String name, Collection<Message> messages) {
        this.name = name;
        this.messages = messages;
    }

    /**
     * Getter method for retrieve the name of the conversation.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for retrieve the messages in the conversation.
     */
    public Collection<Message> getMessages() {
        return messages;
    }
}
