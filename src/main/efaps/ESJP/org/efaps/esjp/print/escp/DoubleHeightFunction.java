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
package org.efaps.esjp.print.escp;

import java.util.regex.Matcher;

import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;

import simple.escp.dom.Line;
import simple.escp.dom.Page;
import simple.escp.dom.Report;
import simple.escp.fill.function.Function;
import simple.escp.util.EscpUtil;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
@EFapsUUID("91fc6c7f-06c5-40c9-9207-266b8faadab1f")
@EFapsApplication("eFapsApp-Print")
public class DoubleHeightFunction
    extends Function
{

    /** The Constant COMMAND_DOUBLE_HEIGHT. (w) */
    public static final int COMMAND_DOUBLE_HEIGHT = 119;

    /** The function. */
    private static DoubleHeightFunction FUNCTION;

    /** The double height. */
    private boolean doubleHeight;

    /**
     * Instantiates a new double height function.
     */
    private DoubleHeightFunction()
    {
        super("%\\{\\s*(DHEIGHT)\\s*\\}");
    }

    @Override
    public String process(final Matcher _matcher,
                          final Report _report,
                          final Page _page,
                          final Line _line)
    {
        final String result = this.doubleHeight ? EscpUtil.esc(COMMAND_DOUBLE_HEIGHT)
                        : EscpUtil.esc(COMMAND_DOUBLE_HEIGHT);
        this.doubleHeight = !this.doubleHeight;
        return result;
    }

    @Override
    public void reset()
    {
        this.doubleHeight = false;
    }

    /**
     * Gets the function.
     *
     * @return the function
     */
    public static DoubleHeightFunction getFunction()
    {
        if (FUNCTION == null) {
            FUNCTION = new DoubleHeightFunction();
        }
        return FUNCTION;
    }
}
