/*
 * Copyright 2003 - 2020 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.efaps.esjp.print.printnode.rest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.esjp.print.printnode.dto.PrinterDto;
import org.efaps.esjp.print.printnode.dto.WhoamiDto;
import org.efaps.esjp.print.util.Print;
import org.efaps.util.EFapsException;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EFapsUUID("138d1be8-fcec-45be-82cd-f92dd7a8a745")
@EFapsApplication("eFapsApp-Print")
public abstract class RestClient_Base
{

    private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);

    public WhoamiDto whoami()
        throws EFapsException
    {
        final Builder request = getClient().target(Print.PRINTNODE_BASEURL.get())
                        .path("whoami")
                        .request(MediaType.APPLICATION_JSON);
        addAuth(request);
        final WhoamiDto whoami = request.get(WhoamiDto.class);
        LOG.info("{}", whoami);
        return whoami;
    }

    public List<PrinterDto> printers()
        throws EFapsException
    {
        final Builder request = getClient().target(Print.PRINTNODE_BASEURL.get())
                        .path("printers")
                        .request(MediaType.APPLICATION_JSON);
        addAuth(request);
        final List<PrinterDto> printers = request.get(new GenericType<List<PrinterDto>>(){});
        LOG.info("{}", printers);
        return printers;
    }

    protected void addAuth(final Builder request)
        throws EFapsException
    {
        final String apiKey = Base64.getEncoder().encodeToString(Print.PRINTNODE_APIKEY.get()
                        .getBytes(StandardCharsets.UTF_8));
        request.header("Authorization", "Basic " + apiKey)
                        .header("Accept-Version", "~3");
    }

    protected Client getClient()
    {
        final ClientConfig clientConfig = new ClientConfig();
        try {
            final Class<?> clazz = Class.forName("org.efaps.esjp.logback.jersey.JerseyLogFeature");
            if (clazz != null) {
                final Object filter = clazz.getConstructor().newInstance();
                final Method method = clazz.getMethod("setLogger", Logger.class);
                method.invoke(filter, LOG);
                clientConfig.register(filter);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
                        | SecurityException | IllegalArgumentException | InvocationTargetException e) {
            LOG.error("Cached", e);
        }
        final Client client = ClientBuilder.newClient(clientConfig);
        return client;
    }
}
