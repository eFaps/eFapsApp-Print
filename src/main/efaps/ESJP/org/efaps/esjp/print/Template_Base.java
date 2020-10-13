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
package org.efaps.esjp.print;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.SimpleDoc;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.db.PrintQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.eql.InvokerUtil;
import org.efaps.eql.stmt.IEQLStmt;
import org.efaps.eql.stmt.parts.ISelectStmtPart;
import org.efaps.esjp.ci.CIFormPrint;
import org.efaps.esjp.ci.CIPrint;
import org.efaps.esjp.common.AbstractCommon;
import org.efaps.esjp.db.InstanceUtils;
import org.efaps.esjp.print.escp.DoubleHeightFunction;
import org.efaps.esjp.print.escp.EFapsTemplate;
import org.efaps.esjp.print.escp.EFapsTemplate.SubDataSource;
import org.efaps.esjp.print.printnode.PrintNodeService;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simple.escp.SimpleEscp;
import simple.escp.data.DataSources;
import simple.escp.dom.Report;
import simple.escp.fill.FillJob;

/**
 * The Class Template_Base.
 *
 * @author The eFaps Team
 */
@EFapsUUID("9816de5a-d96c-45c2-9ebc-4d914664dd6f")
@EFapsApplication("eFapsApp-Print")
public abstract class Template_Base
    extends AbstractCommon
{
    /**
     * Logger used in this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Template.class);

    /**
     * Pattern for getting the Parameter including there special symbols.
     */
    private final Pattern parameterPattern = Pattern.compile("\\$P!\\{[a-z,A-Z,1-9,_,-]*\\}");

    /**
     * Pattern for getting the key of the Parameter.
     */
    private final Pattern keyPattern = Pattern.compile("(?<=\\{)([a-z,A-Z,1-9,_,-]*?)(?=\\})");

    /**
     * Replace parameters.
     *
     * @param _stmtStr stamentStr the parameters will be replace for
     * @param _parameters the _parameters
     * @return stmt
     */
    protected String replaceParameters(final String _stmtStr,
                                       final Map<String, Object> _parameters)
    {
        final Pattern mainPattern = getParameterPattern();
        final Pattern subPattern = getKeyPattern();
        final Matcher matcher = mainPattern.matcher(_stmtStr);
        final Map<String, String> replaceMap = new HashMap<>();
        while (matcher.find()) {
            final String mainStr = matcher.group();
            final Matcher subMatcher = subPattern.matcher(mainStr);
            subMatcher.find();
            replaceMap.put(mainStr, getStringValue(_stmtStr, subMatcher.group(), _parameters));
        }
        String ret = _stmtStr;
        for (final Entry<String, String> entry  :replaceMap.entrySet()) {
            ret = ret.replace(entry.getKey(), entry.getValue());
        }
        LOG.debug("Stmt with replaced Parameters: {}", ret);
        return ret;
    }

    /**
     * Gets the string value.
     *
     * @param _stmtStr stamentStr the parameters will be replace for
     * @param _key key the value will bee searched for
     * @param _parameters the _parameters
     * @return String representation of the object
     */
    protected String getStringValue(final String _stmtStr,
                                    final String _key,
                                    final Map<String, Object> _parameters)
    {
        String ret = "";
        if (_key != null) {
            final Object obj = _parameters.get(_key);
            if (obj != null) {
                LOG.trace("Found object to be replaced: {}", obj);
                if (obj instanceof Instance) {
                    if (_stmtStr.trim().startsWith("print")) {
                        ret = ((Instance) obj).getOid();
                    } else {
                        ret = Long.valueOf(((Instance) obj).getId()).toString();
                    }
                } else {
                    ret = obj.toString();
                }
            }
        }
        return ret;
    }

    /**
     * Prints the.
     *
     * @param _parameter the _parameter
     * @param _templateInst the _template inst
     * @param _objInst the _obj inst
     * @param _printerInst the _printer inst
     * @throws EFapsException the e faps exception
     */
    protected void print(final Parameter _parameter,
                         final Instance _templateInst,
                         final Instance _objInst,
                         final Instance _printerInst)
        throws EFapsException
    {
        final PrintQuery print = new PrintQuery(_templateInst);
        print.addAttribute(CIPrint.TemplateESCP.Template);
        print.execute();
        final String templStr = print.getAttribute(CIPrint.TemplateESCP.Template);

        String printerKey = null;
        if (_printerInst != null && _printerInst.isValid()) {
            final PrintQuery print2 = new PrintQuery(_printerInst);
            print2.addAttribute(CIPrint.PrinterAbstract.Key);
            print2.execute();
            printerKey = print2.getAttribute(CIPrint.PrinterAbstract.Key);
        }

        final EFapsTemplate template = new EFapsTemplate(templStr);
        final Report report = template.parse();

        final Map<String, Object> parameters = new HashMap<>();
        final Map<String, Object> value = new HashMap<>();
        value.put("CURRENTDATE", new Date());

        if (_objInst.isValid()) {
            parameters.put("INSTANCE", _objInst);
        }
        try {
            final String eql = replaceParameters(template.getEqlStmt(), parameters);
            final IEQLStmt stmt = InvokerUtil.getInvoker().invoke(eql);
            if (stmt instanceof ISelectStmtPart) {
                final List<Map<String, Object>> listmap = ((ISelectStmtPart) stmt).getData();
                if (listmap != null && !listmap.isEmpty()) {
                    value.putAll(listmap.get(0));
                }
            }
        } catch (final Exception e) {
            LOG.error("Catched error", e);
        }
        for (final SubDataSource sds  :template.getSubDataSources()) {
            final String eql = replaceParameters(sds.getEqlStmt(), parameters);
            try {
                final IEQLStmt stmt = InvokerUtil.getInvoker().invoke(eql);
                if (stmt instanceof ISelectStmtPart) {
                    final List<Map<String, Object>> listmap = ((ISelectStmtPart) stmt).getData();
                    if (sds.getPaging() > 0) {
                        while (listmap.size() % sds.getPaging() != 0) {
                            listmap.add(getEmptyMap(listmap.size() > 0 ? listmap.get(0) : null));
                        }
                    }
                    if (listmap != null && !listmap.isEmpty()) {
                        value.put(sds.getKey(), listmap);
                    }
                }
            } catch (final Exception e) {
                LOG.error("Catched error", e);
            }
        }
        FillJob.addFunction(DoubleHeightFunction.getFunction());

        final FillJob fillJob = new FillJob(report, DataSources.from(value));

        final boolean print2file = printerKey == null;
        if (print2file) {
            LOG.info(fillJob.fill());
        } else if (InstanceUtils.isType(_printerInst, CIPrint.Printer)){
            final SimpleEscp simpleEscp = new SimpleEscp(printerKey);
            final Charset charset = Charset.isSupported("ISO-8859-1") ? Charset.forName("ISO-8859-1") :
                StandardCharsets.US_ASCII;
            final InputStream in = new ByteArrayInputStream(fillJob.fill().getBytes(charset));
            final Doc doc = new SimpleDoc(in, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
            final DocPrintJob job = simpleEscp.getPrintService().createPrintJob();
            final PrintJobMonitor monitor = new PrintJobMonitor(_parameter, _printerInst, _objInst);
            job.addPrintJobListener(monitor);
            try {
                job.print(doc, null);
                monitor.waitForJobCompletion();
            } catch (final PrintException e) {
                LOG.error("Error during printing.", e);
                throw new EFapsException("Error during printing", e);
            }
        } else {
            final PrintNodeService printNodeService = new PrintNodeService();
            printNodeService.printRaw(printerKey, fillJob.fill());
        }
    }

    /**
     * Gets the empty map.
     *
     * @param _templateMap the _template map
     * @return the empty map
     */
    protected Map<String, Object> getEmptyMap(final Map<String, Object> _templateMap)
    {
        final Map<String,Object> ret = new HashMap<>();
        if (_templateMap != null ) {
            for (final Entry<String,Object> entry : _templateMap.entrySet()) {
                ret.put(entry.getKey(), null);
            }
        }
        return ret;
    }

    /**
     * Test print.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @return the return
     * @throws EFapsException on error
     */
    public Return testPrint(final Parameter _parameter)
        throws EFapsException
    {
        final Instance templInst = _parameter.getInstance();
        if (templInst.isValid() && templInst.getType().isCIType(CIPrint.TemplateESCP)) {
            final Instance objInst = Instance
                            .get(_parameter.getParameterValue(CIFormPrint.Print_TemplatePrintTestForm.objectOID.name));
            final Instance printerInst = Instance
                            .get(_parameter.getParameterValue(CIFormPrint.Print_TemplatePrintTestForm.printer.name));
            print(_parameter, templInst, objInst, printerInst);
        }
        return new Return();
    }

    /**
     * Getter method for the instance variable {@link #pattern}.
     *
     * @return value of instance variable {@link #pattern}
     */
    protected Pattern getParameterPattern()
    {
        return parameterPattern;
    }

    /**
     * Getter method for the instance variable {@link #keyPattern}.
     *
     * @return value of instance variable {@link #keyPattern}
     */
    protected Pattern getKeyPattern()
    {
        return keyPattern;
    }

    /**
     * Gets the template instance for a given name.
     *
     * @param _parameter the _parameter
     * @param _templateName the _template name
     * @return the template inst4 name
     * @throws EFapsException the e faps exception
     */
    public static Instance getTemplateInst4Name(final Parameter _parameter,
                                                final String _templateName)
        throws EFapsException
    {
        final QueryBuilder queryBldr = new QueryBuilder(CIPrint.TemplateAbstract);
        queryBldr.addWhereAttrEqValue(CIPrint.TemplateAbstract.Name, _templateName);
        final List<Instance> templInsts = queryBldr.getQuery().executeWithoutAccessCheck();
        return templInsts.isEmpty() ? null : templInsts.get(0);
    }
}
