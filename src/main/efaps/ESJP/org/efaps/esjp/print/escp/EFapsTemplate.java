/*
 * Copyright 2003 - 2016 The eFaps Team
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

package org.efaps.esjp.print.escp;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;

import simple.escp.dom.Report;
import simple.escp.json.JsonTemplate;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
@EFapsUUID("0c41289e-653e-4a64-a161-720526b3dbf3")
@EFapsApplication("eFapsApp-Print")
public class EFapsTemplate
    extends JsonTemplate
{

    /** The eql stmt. */
    private String eqlStmt;

    /** The sub data sources. */
    private final List<SubDataSource> subDataSources = new ArrayList<>();

    /**
     * Instantiates a new e faps template.
     *
     * @param _json the _json
     */
    public EFapsTemplate(final String _json)
    {
        super(_json);
    }

    @Override
    public Report parse()
    {
        if (this.report == null) {
            try (JsonReader reader = Json.createReader(new StringReader(getOriginalText()))) {
                final JsonObject json = reader.readObject();
                parseDataSource(json);
            }
        }
        return super.parse();
    }

    /**
     * Parses the data source.
     *
     * @param _json the _json
     */
    protected void parseDataSource(final JsonObject _json)
    {
        final JsonObject dataSourceObject = _json.getJsonObject("dataSource");
        if (dataSourceObject.containsKey("eql")) {
            setEqlStmt(dataSourceObject.getString("eql"));
        }
        if (dataSourceObject.containsKey("subDataSource")) {
            for (final JsonValue subDSValue : dataSourceObject.getJsonArray("subDataSource")) {
                final JsonObject subDSObject = (JsonObject) subDSValue;
                final SubDataSource sds = new SubDataSource();
                this.subDataSources.add(sds);
                if (subDSObject.containsKey("key")) {
                    sds.setKey(subDSObject.getString("key"));
                }
                if (subDSObject.containsKey("eql")) {
                    sds.setEqlStmt(subDSObject.getString("eql"));
                }
                if (subDSObject.containsKey("paging")) {
                    sds.setPaging(subDSObject.getInt("paging"));
                }
            }
        }
    }

    /**
     * Getter method for the instance variable {@link #eqlStmt}.
     *
     * @return value of instance variable {@link #eqlStmt}
     */
    public String getEqlStmt()
    {
        return this.eqlStmt;
    }

    /**
     * Setter method for instance variable {@link #eqlStmt}.
     *
     * @param _eqlStmt value for instance variable {@link #eqlStmt}
     */
    public void setEqlStmt(final String _eqlStmt)
    {
        this.eqlStmt = _eqlStmt;
    }

    /**
     * Getter method for the instance variable {@link #subDataSources}.
     *
     * @return value of instance variable {@link #subDataSources}
     */
    public List<SubDataSource> getSubDataSources()
    {
        return this.subDataSources;
    }

    /**
     * The Class SubDataSource.
     */
    public static class SubDataSource
    {

        /** The key. */
        private String key;

        /** The eql. */
        private String eqlStmt;

        /** The paging. */
        private int paging;

        /**
         * Getter method for the instance variable {@link #key}.
         *
         * @return value of instance variable {@link #key}
         */
        public String getKey()
        {
            return this.key;
        }

        /**
         * Setter method for instance variable {@link #key}.
         *
         * @param _key value for instance variable {@link #key}
         */
        public void setKey(final String _key)
        {
            this.key = _key;
        }

        /**
         * Getter method for the instance variable {@link #eql}.
         *
         * @return value of instance variable {@link #eql}
         */
        public String getEqlStmt()
        {
            return this.eqlStmt;
        }

        /**
         * Setter method for instance variable {@link #eql}.
         *
         * @param _eqlStmt the new eql stmt
         */
        public void setEqlStmt(final String _eqlStmt)
        {
            this.eqlStmt = _eqlStmt;
        }

        /**
         * Getter method for the instance variable {@link #paging}.
         *
         * @return value of instance variable {@link #paging}
         */
        public int getPaging()
        {
            return this.paging;
        }

        /**
         * Setter method for instance variable {@link #paging}.
         *
         * @param _paging value for instance variable {@link #paging}
         */
        public void setPaging(final int _paging)
        {
            this.paging = _paging;
        }
    }
}
