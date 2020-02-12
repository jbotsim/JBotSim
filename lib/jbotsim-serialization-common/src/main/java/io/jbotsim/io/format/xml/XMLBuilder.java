/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package io.jbotsim.io.format.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

/**
 * Base class for ones used to build JBotSim XML document.
 *
 * <p>The class creates a {@link Document XML document}. Following {@code jbotsim-}{@link #DEFAULT_VERSION}{@code .xsd}
 * schema, a root element labelled {@code "jbotsim"} is added to the document and its attribute {@code version} is
 * assigned the value {@link #DEFAULT_VERSION}.</p>
 *
 * <p>Helper methods exists to output the document using methods of the {@link XMLIO} class.</p>
 *
 * @see XMLIO
 * @see XMLIO.XMLIOException
 */
public abstract class XMLBuilder {
    /**
     * Default version of the XSD schema used to parse JBotsim XML documents.
     *
     * Its current value is {@value DEFAULT_VERSION}.
     */
    public static final String DEFAULT_VERSION = "1.0";

    private Document document;

    protected XMLBuilder() throws BuilderException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.newDocument();
            Element root = XMLKeys.JBOTSIM.createElement(document);
            XMLKeys.VERSION_ATTR.setAttribute(root, DEFAULT_VERSION);
            document.appendChild(root);
        } catch (ParserConfigurationException e) {
            throw new BuilderException(e);
        }

    }

    /**
     * Accessor to the  {@link Document} handled by this builder.
     *
     * @return the {@link Document} under construction
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Outputs the current XML document into the specified file pointed by the given {@link File} descriptor.
     *
     * @return the {@link Document} converted into a {@link String}.
     * @throws BuilderException raised either when an XML error occurs while the document is created or if an IO error
     *         occurs.
     */
    public String writeToString() throws BuilderException {
        try {
            return XMLIO.writeToString(document);
        } catch (XMLIO.XMLIOException e) {
            throw new BuilderException(e);
        }
    }

    /**
     * Exception raised whenever an error occurs while a {@link Document} is built.
     */
    public static class BuilderException extends Exception {
        public BuilderException(Throwable cause) {
            this("XML generator yields an exception.", cause);
        }

        public BuilderException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
