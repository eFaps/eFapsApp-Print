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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = WhoamiDto.Builder.class)
@EFapsUUID("d00d7e9e-e2d4-4f3e-9478-ee6e2ef7d65a")
@EFapsApplication("eFapsApp-Print")
public class WhoamiDto
{

    private final Long id;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final Long credits;
    private final Long numComputers;
    private final Long totalPrints;

    private WhoamiDto(final Builder builder)
    {
        id = builder.id;
        firstname = builder.firstname;
        lastname = builder.lastname;
        email = builder.email;
        credits = builder.credits;
        numComputers = builder.numComputers;
        totalPrints = builder.totalPrints;
    }

    public Long getId()
    {
        return id;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public String getEmail()
    {
        return email;
    }

    public Long getCredits()
    {
        return credits;
    }

    public Long getNumComputers()
    {
        return numComputers;
    }

    public Long getTotalPrints()
    {
        return totalPrints;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Creates builder to build {@link WhoamiDto}.
     *
     * @return created builder
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link WhoamiDto}.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder
    {

        private Long id;
        private String firstname;
        private String lastname;
        private String email;
        private Long credits;
        private Long numComputers;
        private Long totalPrints;

        private Builder()
        {
        }

        public Builder withId(final Long id)
        {
            this.id = id;
            return this;
        }

        public Builder withFirstname(final String firstname)
        {
            this.firstname = firstname;
            return this;
        }

        public Builder withLastname(final String lastname)
        {
            this.lastname = lastname;
            return this;
        }

        public Builder withEmail(final String email)
        {
            this.email = email;
            return this;
        }

        public Builder withCredits(final Long credits)
        {
            this.credits = credits;
            return this;
        }

        public Builder withNumComputers(final Long numComputers)
        {
            this.numComputers = numComputers;
            return this;
        }

        public Builder withTotalPrints(final Long totalPrints)
        {
            this.totalPrints = totalPrints;
            return this;
        }

        public WhoamiDto build()
        {
            return new WhoamiDto(this);
        }
    }

}
