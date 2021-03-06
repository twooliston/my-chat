package com.mindlinksoft.recruitment.mychat;

import com.google.gson.*;

import com.mindlinksoft.recruitment.mychat.models.Conversation;
import com.mindlinksoft.recruitment.mychat.models.Conversation.ConversationBuilder;
import com.mindlinksoft.recruitment.mychat.models.Message;
import com.mindlinksoft.recruitment.mychat.models.Message.MessageBuilder;
import com.mindlinksoft.recruitment.mychat.options.Options;

import java.io.*;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a conversation exporter that can read a conversation and write it
 * out in JSON.
 */
public class ConversationExporter {

    /**
     * Exports the conversation at {@code inputFilePath} as JSON to
     * {@code outputFilePath}.
     * 
     * @param configuration Contains {@code inputFilePath} and the
     *                      {@code outputFilePath}.
     * @throws IllegalArgumentException Thrown when the the input is illegal
     * @throws IOException              Thrown when the writting to the output file
     *                                  fails
     */
    public void exportConversation(ConversationExporterConfiguration configuration)
            throws FileNotFoundException, IOException {
        MyChat.logger.trace("Exporting the conversation...");
        Conversation conversation = this.readConversation(configuration.inputFilePath);

        conversation = this.applyOptions(conversation, configuration);
        MyChat.logger.info("Options have been applied to the conversation");

        this.writeConversation(conversation, configuration.outputFilePath);
        MyChat.logger.info("Export complete");
    }

    /**
     * Represents a helper to read a conversation from the given
     * {@code inputFilePath}.
     * 
     * @param inputFilePath The path to the input file.
     * @return The {@link Conversation} representing by the input file.
     * @throws IllegalArgumentException Thrown when the the input is illegal
     * @throws IOException              Thrown when the writting to the output file
     *                                  fails
     */
    public Conversation readConversation(String inputFilePath) throws FileNotFoundException, IOException {
        try (InputStream is = new FileInputStream(inputFilePath);
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            MyChat.logger.trace("Reading conversation...");

            List<Message> messages = new ArrayList<Message>();
            String conversationName = br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ", 3);
                messages.add(new MessageBuilder().buildNewMessage(split[1], split[0], split[2]));
            }
            MyChat.logger.info("Conversation loadded into memory from file: " + inputFilePath);

            // release system resources from stream operations
            br.close();
            is.close();

            return new ConversationBuilder().buildNewConversation(conversationName, messages, null);
        } catch (FileNotFoundException e) {
            MyChat.logger.error("The file '" + inputFilePath + "'" + "was not found." + "\n With the error message: "
                    + e.getMessage());
            throw new FileNotFoundException("The file '" + inputFilePath + "'" + "was not found."
                    + "\n With the error message: " + e.getMessage());
        } catch (IOException e) {
            MyChat.logger.error("Error occured while reading the file '" + inputFilePath + "'"
                    + "\n With the error message: " + e.getMessage());
            throw new IOException("Error occured while reading the file '" + inputFilePath + "'"
                    + "\n With the error message: " + e.getMessage());
        }
    }

    /**
     * Applies the Options to the Conversation
     * 
     * @param conversation  The conversation before options are applied.
     * @param configuration The configuration containing all the option details.
     * @return The {@link Conversation} representing by the input file.
     * @throws IllegalArgumentException Thrown when the the input is illegal
     * @throws IOException              Thrown when the writting to the output file
     *                                  fails
     */
    public Conversation applyOptions(Conversation conversation, ConversationExporterConfiguration configuration)
            throws FileNotFoundException, IOException {
        Options savedOptions = new Options(conversation, configuration);
        return savedOptions.applyOptionsToConversation();
    }

    /**
     * Helper method to write the given {@code conversation} as JSON to the given
     * {@code outputFilePath}.
     * 
     * @param conversation   The conversation to write.
     * @param outputFilePath The file path where the conversation should be written.
     * @throws IllegalArgumentException Thrown when the the input is illegal
     * @throws IOException              Thrown when the writting to the output file
     *                                  fails
     */
    public void writeConversation(Conversation conversation, String outputFilePath)
            throws FileNotFoundException, IOException {
        try (OutputStream os = new FileOutputStream(outputFilePath, false);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os))) {
            MyChat.logger.trace("Writing conversation...");

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Instant.class, new InstantSerializer());
            Gson g = gsonBuilder.setPrettyPrinting().create();
            String convertedConversation = g.toJson(conversation);
            MyChat.logger.info("Conversation converted to JSON");

            bw.write(convertedConversation);
            MyChat.logger.info("Conversation written to JSON file: " + outputFilePath);

            // release system resources from stream operations
            bw.close();
            os.close();

        } catch (FileNotFoundException e) {
            MyChat.logger.error("The file '" + outputFilePath + "'" + "was not found." + "\n With the error message: "
                    + e.getMessage());
            throw new FileNotFoundException("The file '" + outputFilePath + "'" + "was not found."
                    + "\n With the error message: " + e.getMessage());
        } catch (IOException e) {
            MyChat.logger.error("Error occured while writting to the file '" + outputFilePath + "'"
                    + "\n With the error message: " + e.getMessage());
            throw new IOException("Error occured while writting to the file '" + outputFilePath + "'"
                    + "\n With the error message: " + e.getMessage());
        }
    }

    class InstantSerializer implements JsonSerializer<Instant> {
        @Override
        public JsonElement serialize(Instant instant, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(instant.getEpochSecond());
        }
    }
}
