/*
 *
 *  * Copyright 2020 New Relic Corporation. All rights reserved.
 *  * SPDX-License-Identifier: Apache-2.0
 *
 */

package org.apache.solr.client.solrj.impl;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.agent.bridge.datastore.DatastoreVendor;
import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import org.apache.http.client.HttpClient;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.common.util.NamedList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

@Weave(type = MatchType.ExactClass, originalName = "org.apache.solr.client.solrj.impl.HttpSolrServer")
public abstract class HttpSolrServer_Instrumentation {

    @NewField
    private URL url = null;

    public HttpSolrServer_Instrumentation(String baseURL, HttpClient client, ResponseParser parser) {
        try {
            url = new URL(baseURL);
        } catch (MalformedURLException e) {
            AgentBridge.getAgent().getLogger().log(Level.WARNING, "Could not parse host and port from HttpSolrServer", e);
        }
    }

    @Trace
    public NamedList<Object> request(final SolrRequest request, ResponseParser processor) {
        NamedList<Object> namedList = Weaver.callOriginal();

        String collection = url.getPath();
        collection = collection.startsWith("/") ? collection.substring(1) : collection;

        DatastoreParameters params = DatastoreParameters
                .product(DatastoreVendor.Solr.name())
                .collection(collection)
                .operation(request.getMethod().toString())
                .instance(url.getHost(), url.getPort())
                .databaseName(null)
                .build();

        NewRelic.getAgent().getTracedMethod().reportAsExternal(params);

        return namedList;
    }

}
