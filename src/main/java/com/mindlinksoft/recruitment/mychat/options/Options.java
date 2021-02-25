package com.mindlinksoft.recruitment.mychat.options;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mindlinksoft.recruitment.mychat.ConversationExporter;
import com.mindlinksoft.recruitment.mychat.ConversationExporterConfiguration;
import com.mindlinksoft.recruitment.mychat.models.Conversation;
import com.mindlinksoft.recruitment.mychat.models.Message;
import com.mindlinksoft.recruitment.mychat.models.User;

public class Options {
    Conversation conversation;
    String user;
    String keyword;
    String[] blacklist;
    Boolean report = false;

    /**
     * Initializes a new instance of the {@link Options} class.
     * 
     * @param conversation  The conversation.
     * @param configuration To get the filters that need to be applied.
     */
    public Options(Conversation conversation, ConversationExporterConfiguration configuration) {
        this.user = configuration.filterUser;
        this.keyword = configuration.filterKeyword;
        this.blacklist = configuration.blacklist;
        this.report = configuration.report;
        this.conversation = conversation;
    }

    /**
     * Apply the defined filters to the conversation
     */
    public Conversation applyOptionsToConversation() {
        if (this.user != null) {
            this.conversation.messages = new FilterByUser(this.conversation, this.user).process();
        }
        if (this.keyword != null) {
            this.conversation.messages = new FilterByKeyword(this.conversation, this.keyword).process();
        }
        if (this.blacklist != null) {
            this.conversation.messages = new Blacklist(this.conversation, this.blacklist).process();
        }
        if (this.report) {
            this.conversation = new Report(this.conversation).process();
        }
        return this.conversation;
    }

    /**
     * Getter method for retrieving the user for the conversation to be filtered by.
     */
    public String getFilterUser() {
        return this.user;
    }

    /**
     * Getter method for retrieving the keyword for the conversation to be filtered
     * by.
     */
    public String getFilterKeyword() {
        return this.keyword;
    }

    /**
     * Getter method for retrieving the blacklist for the conversation to be
     * filtered by.
     */
    public String[] getBlacklist() {
        return this.blacklist;
    }

    /**
     * Getter method for retrieving the boolean to decide if the report should be
     * generated.
     */
    public Boolean getReport() {
        return this.report;
    }

}
