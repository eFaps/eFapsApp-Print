/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
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
 */
package org.efaps.esjp.print.printnode.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = PrintJobDto.Builder.class)
@EFapsUUID("71c02a54-a21a-4a5f-94b3-7fcd6b412583")
@EFapsApplication("eFapsApp-Print")
public class PrintJobDto
{

    private final Long printerId;
    private final String title;
    private final String contentType;
    private final String content;
    private final String source;

    private PrintJobDto(final Builder builder)
    {
        printerId = builder.printerId;
        title = builder.title;
        contentType = builder.contentType;
        content = builder.content;
        source = builder.source;
    }

    public Long getPrinterId()
    {
        return printerId;
    }

    public String getTitle()
    {
        return title;
    }

    public String getContentType()
    {
        return contentType;
    }

    public String getContent()
    {
        return content;
    }

    public String getSource()
    {
        return source;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Creates builder to build {@link PrintJobDto}.
     *
     * @return created builder
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link PrintJobDto}.
     */
    public static final class Builder
    {

        private Long printerId;
        private String title;
        private String contentType;
        private String content;
        private String source;

        private Builder()
        {
        }

        public Builder withPrinterId(final Long printerId)
        {
            this.printerId = printerId;
            return this;
        }

        public Builder withTitle(final String title)
        {
            this.title = title;
            return this;
        }

        public Builder withContentType(final String contentType)
        {
            this.contentType = contentType;
            return this;
        }

        public Builder withContent(final String content)
        {
            this.content = content;
            return this;
        }

        public Builder withSource(final String source)
        {
            this.source = source;
            return this;
        }

        public PrintJobDto build()
        {
            return new PrintJobDto(this);
        }
    }
}
