/*
 *
 *  * Copyright 2020 New Relic Corporation. All rights reserved.
 *  * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.newrelic.api.agent;

/**
 * Creates the parameters to report a message that was pulled from a message queue. This should be used with
 * {@link TracedMethod#reportAsExternal(ExternalParameters)}. A fluent builder is provided to allow for easy usage and
 * management of this API.
 *
 * @since 3.36.0
 */
public class MessageConsumeParameters implements ExternalParameters {

    private final String library;
    private final DestinationType destinationType;
    private final String destinationName;
    private final InboundHeaders inboundHeaders;

    protected MessageConsumeParameters(String library, DestinationType destinationType, String destinationName,
            InboundHeaders inboundHeaders) {
        this.library = library;
        this.destinationType = destinationType;
        this.destinationName = destinationName;
        this.inboundHeaders = inboundHeaders;
    }

    protected MessageConsumeParameters(MessageConsumeParameters messageConsumeParameters) {
        this.library = messageConsumeParameters.library;
        this.destinationType = messageConsumeParameters.destinationType;
        this.destinationName = messageConsumeParameters.destinationName;
        this.inboundHeaders = messageConsumeParameters.inboundHeaders;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public DestinationType getDestinationType() {
        return destinationType;
    }

    public InboundHeaders getInboundHeaders() {
        return inboundHeaders;
    }

    public String getLibrary() {
        return library;
    }

    protected static class Builder implements DestinationTypeParameter, DestinationNameParameter,
            InboundHeadersParameter, Build {
        private String library;
        private DestinationType destinationType;
        private String destinationName;
        private InboundHeaders inboundHeaders;

        public Builder(String library) {
            this.library = library;
        }

        public DestinationNameParameter destinationType(DestinationType destinationType) {
            this.destinationType = destinationType;
            return this;
        }

        public InboundHeadersParameter destinationName(String destinationName) {
            this.destinationName = destinationName;
            return this;
        }

        public Build inboundHeaders(InboundHeaders inboundHeaders) {
            this.inboundHeaders = inboundHeaders;
            return this;
        }

        public MessageConsumeParameters build() {
            return new MessageConsumeParameters(library, destinationType, destinationName, inboundHeaders);
        }
    }

    /**
     * Set the name of the library.
     *
     * @param library the name of the library
     * @return the next builder interface
     */
    public static DestinationTypeParameter library(String library) {
        return new MessageConsumeParameters.Builder(library);
    }

    public interface DestinationTypeParameter {

        /**
         * Set the destination type of the external call.
         *
         * @param destinationType the destination type of the external call
         * @return the next builder interface
         */
        DestinationNameParameter destinationType(DestinationType destinationType);
    }

    public interface DestinationNameParameter {

        /**
         * Set the destination name of the external call.
         *
         * @param destinationName the destination name of the external call
         * @return the next builder interface
         */
        InboundHeadersParameter destinationName(String destinationName);
    }

    public interface InboundHeadersParameter {

        /**
         * Set the inbound headers on the external call.
         *
         * @param inboundHeaders the inbound headers for the external call
         * @return the completed HttpParameters object
         */
        Build inboundHeaders(InboundHeaders inboundHeaders);
    }

    public interface Build {

        /**
         * Build the final {@link MessageConsumeParameters} for the API call.
         *
         * @return the completed GenericParameters object
         */
        MessageConsumeParameters build();
    }

}
