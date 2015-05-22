package com.dotTracePlugin.server;

import com.dotTracePlugin.common.dotTraceRunnerConstants;
import jetbrains.buildServer.log.LogUtil;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.messages.BuildMessage1;
import jetbrains.buildServer.messages.DefaultMessagesInfo;
import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.messages.serviceMessages.ServiceMessage;
import jetbrains.buildServer.messages.serviceMessages.ServiceMessageTranslator;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.serverSide.ServerExtensionHolder;
import jetbrains.buildServer.serverSide.statistics.build.BuildDataStorage;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by Alexey.Totin on 5/20/2015.
 */
public class dotTraceStatisticValueTranslator implements ServiceMessageTranslator {

    private final BuildDataStorage myStorage;

    public dotTraceStatisticValueTranslator(ServerExtensionHolder server, BuildDataStorage storage) {
        myStorage = storage;
        server.registerExtension(ServiceMessageTranslator.class, getClass().getName(), this);
    }

    @NotNull
    public List<BuildMessage1> translate(SRunningBuild runningBuild, BuildMessage1 buildMessage, ServiceMessage serviceMessage) {
//        buildMessage.updateTags(DefaultMessagesInfo.TAG_INTERNAL);

        try {
            final String key = serviceMessage.getAttributes().get("key");
            final String value = serviceMessage.getAttributes().get("value");
            if (key != null && value != null) {
                myStorage.publishValue(key, runningBuild.getBuildId(), parseValue(value));
            }

            return Collections.singletonList(buildMessage);
        } catch (Exception e) {

            String message = buildErrorMessage(serviceMessage, e);
            Loggers.SERVER.info("Error processing service message for build " + LogUtil.describe(runningBuild) + ": " + message);
            Loggers.SERVER.debug("Error processing service message for build " + LogUtil.describe(runningBuild) + ": " + message, e);
            return Collections.singletonList(DefaultMessagesInfo.createTextMessage(message, Status.WARNING));
        }
    }

    @NotNull
    private BigDecimal parseValue(String value) {
        try {
            return new BigDecimal(value.trim());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse decimal value: \"" + value + "\"");
        }
    }

    private String buildErrorMessage(final ServiceMessage serviceMessage, Exception e) {
        final String key = serviceMessage.getAttributes().get("key");
        final String value = serviceMessage.getAttributes().get("value");
        final String messageName = serviceMessage.getMessageName();
        String exceptionMessage = e.getMessage();
        if (exceptionMessage != null) {
            exceptionMessage = ". " + exceptionMessage;
        }
        return "Unable to process service message: \"[" + messageName + " key='" + key + "' value='" + value + "']\"" + exceptionMessage;
    }

    @NotNull
    public String getServiceMessageName() {
        return dotTraceRunnerConstants.DT_SERVICE_MESSAGE_NAME;
    }
}
