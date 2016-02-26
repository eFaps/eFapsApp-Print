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
package org.efaps.esjp.print.util;

import java.util.UUID;

import org.efaps.admin.common.SystemConfiguration;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.api.annotation.EFapsSystemConfiguration;
import org.efaps.util.cache.CacheReloadException;

/**
 * The Class Print.
 *
 * @author The eFaps Team
 */
@EFapsUUID("6bf8791a-a1d1-47d1-ac63-7cd93e6df22a")
@EFapsApplication("eFapsApp-Assets")
@EFapsSystemConfiguration("bd750e07-409c-460d-afbf-b44839a42477")
public final class Print
{
    /** The base. */
    public static final String BASE = "org.efaps.print.";

    /** Assets-Configuration. */
    public static final UUID SYSCONFUUID = UUID.fromString("bd750e07-409c-460d-afbf-b44839a42477");

    /**
     * Singelton.
     */
    private Print()
    {
    }

    /**
     * @return the SystemConfigruation for Print
     * @throws CacheReloadException on error
     */
    public static SystemConfiguration getSysConfig()
        throws CacheReloadException
    {
        // Assets-Configuration
        return SystemConfiguration.get(Print.SYSCONFUUID);
    }
}
