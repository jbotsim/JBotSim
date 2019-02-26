/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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
package io.jbotsim.ui;

import io.jbotsim.core.Properties;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.PropertyListener;
import io.jbotsim.io.TopologySerializer;
import io.jbotsim.io.format.TopologySerializerFilenameMatcher;
import io.jbotsim.io.format.dot.DotTopologySerializer;
import io.jbotsim.io.format.plain.PlainTopologySerializer;
import io.jbotsim.io.format.tikz.TikzTopologySerializer;
import io.jbotsim.io.format.xml.XMLTopologySerializer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * <p>The {@link JViewer} includes a central {@link JTopology} which will draw the attached
 * topology and offer interaction, as well as contextual commands to add or
 * remove a communication range or sensing range tuners (slider bars), or to
 * pause/resume the system clock.</p>
 */
public class JViewer implements CommandListener, ChangeListener, PropertyListener {
    protected JTopology jtp;
    protected JSlider slideBar;
    protected TopologySerializerFilenameMatcher topologySerializerFilenameMatcher;

    protected enum BarType {COMMUNICATION, SENSING, SPEED}

    protected BarType slideBarType = null;
    protected JFrame window = null;

    /**
     * Creates a windowed viewer for the specified topology.
     *
     * @param topo The topology to be drawn and/or manipulated.
     */
    public JViewer(Topology topo) {
        this(topo, true);
    }

    /**
     * Creates a viewer for the specified topology. If <tt>selfContained</tt>
     * is <tt>true</tt>, a new window will be created to contain the viewer
     * (similarly to <tt>JViewer(Topology)</tt>). If it is <tt>false</tt>,
     * no window will be created and the viewer can be subsequently
     * integrated to another swing container (e.g. another <tt>JFrame</tt>
     * or a <tt>JApplet</tt>).
     *
     * @param topo          The topology to be drawn and/or manipulated.
     * @param selfContained Set this to false to avoid creating a JFrame
     *                      (e.g. for embedding the JViewer in your own frame).
     */
    public JViewer(Topology topo, boolean selfContained) {
        this(new JTopology(topo), selfContained);
    }

    /**
     * Creates a windowed viewer encapsulating the specified jtopology.
     *
     * @param jtopo The jtopology to be encapsulated.
     */
    public JViewer(JTopology jtopo) {
        this(jtopo, true);
    }

    /**
     * Creates a viewer encapsulating the specified jtopology. If
     * <tt>selfContained</tt> is <tt>true</tt>, a new window will be created
     * to contain the viewer (similarly to <tt>JViewer(Topology)</tt>). If it
     * is <tt>false</tt>, no window will be created and the viewer can be
     * subsequently integrated to another swing container (e.g. another
     * <tt>JFrame</tt> or a <tt>JApplet</tt>).
     *
     * @param jtopo    The JTopology to be encapsulated.
     * @param windowed Set this to false to avoid creating a JFrame
     *                 (e.g. for embedding the JViewer in your own frame).
     */
    public JViewer(JTopology jtopo, boolean windowed) {
        jtp = jtopo;
        jtp.addCommand("Set communication range");
        jtp.addCommand("Set sensing range");
        jtp.addCommand("Set clock speed");
        jtp.addCommand("Pause or resume execution");
        jtp.addCommand("Execute a single step");
        jtp.addCommand("Restart nodes");
        jtp.addCommand("Load topology");
        jtp.addCommand("Save topology");
        jtp.addCommandListener(this);
        jtp.topo.addPropertyListener(this);
        if (windowed) { // This JViewer creates its own window
            window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.add(jtp);
            window.addKeyListener(jtp.handler);
            window.pack();
            window.setVisible(true);
        }
        slideBar = new JSlider(0, jtp.getWidth());
        slideBar.addChangeListener(this);

        initFilenameMatcher();
    }

    /**
     * Returns the jtopology attached to this viewer. Obtaining the reference
     * can be useful for example to add or remove action commands or action
     * listeners to the jtopology.
     *
     * @return The jtopology reference.
     */
    public JTopology getJTopology() {
        return jtp;
    }

    /**
     * Sets the size of the inner jtopology to the specified dimension.
     *
     * @param width  The desired width, in pixels.
     * @param height The desired height, in pixels.
     */
    public void setSize(int width, int height) {
        jtp.topo.setDimensions(width, height);
        jtp.setPreferredSize(new Dimension(jtp.topo.getWidth(), jtp.topo.getHeight()));
        if (window != null)
            window.pack();
    }

    /**
     * Sets the title of the corresponding window
     * @param title the new title, as a {@link String}.
     */
    public void setTitle(String title) {
        if (window != null)
            window.setTitle(title);
    }

    /**
     * Adds a slide bar at the top of the topology.
     *
     * @param type the {@link BarType} to add.
     * @param value the value for the slide bar, as an integer.
     */
    public void addSlideBar(BarType type, int value) {
        removeSlideBar();
        slideBarType = type;
        jtp.getParent().add(slideBar, BorderLayout.NORTH);
        slideBar.setValue(value);
    }

    /**
     * Removes the slide bar, if any.
     */
    public void removeSlideBar() {
        if (slideBarType != null) {
            jtp.getParent().remove(slideBar);
            slideBarType = null;
        }
    }

    @Override
    public void onCommand(String command) {
        if (command.equals("Set communication range")) {
            executeSetCommunicationRange();
        } else if (command.equals("Set sensing range")) {
            executeSetSensingRange();
        } else if (command.equals("Set clock speed")) {
            executeSetClockSpeed();
        } else if (command.equals("Pause or resume execution")) {
            if (!jtp.topo.isStarted())
                jtp.topo.start();
            else {
                if (jtp.topo.isRunning())
                    jtp.topo.pause();
                else
                    jtp.topo.resume();
            }
        } else if (command.equals("Restart nodes")) {
            jtp.topo.restart();
        } else if (command.equals("Execute a single step")) {
            jtp.topo.step();
        } else {
            if (command.equals("Load topology")) {
                executeLoadTopology();
            } else if (command.equals("Save topology")) {
                executeSaveTopology();
            }
        }
    }

    private void executeSetClockSpeed() {
        // The clock speed is linked by a specific law to the range of the slider
        int value = getSliderValueFromClockSpeed(slideBar.getMaximum(), jtp.topo.getClockSpeed());
        reactToSlideBarCommand(BarType.SPEED, value);
    }

    private void executeSetSensingRange() {
        // The sensing range is linearly linked to the original range of the slider
        reactToSlideBarCommand(BarType.SENSING, (int) jtp.topo.getSensingRange());
    }

    private void executeSetCommunicationRange() {
        // The communication range is linearly linked to the original range of the slider
        reactToSlideBarCommand(BarType.COMMUNICATION, (int) jtp.topo.getCommunicationRange());
    }

    private void reactToSlideBarCommand(BarType barType, int value) {
        if (slideBarType != barType)
            addSlideBar(barType, value);
        else
            removeSlideBar();
        jtp.updateUI();
    }

    private void executeSaveTopology() {
        String filename = getFilenameFromUser(true);
        if (filename == null) return;

        TopologySerializer serializer = getTopologySerializerForFilename(filename, jtp.topo);
        String exportedTopology = serializer.exportToString(jtp.topo);
        jtp.topo.getFileManager().write(filename, exportedTopology);
    }

    private void executeLoadTopology() {
        String filename = getFilenameFromUser(false);
        if (filename == null)
            return;

        if (! jtp.topo.getNodes().isEmpty()) {
            int n = JOptionPane.showConfirmDialog(jtp.getParent(),
                    "Should we reset the current topology ?",
                    "Make a choice",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (n == JOptionPane.YES_OPTION) {
                jtp.getTopology().clear();
            } else if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION) {
                return;
            }
        }

        TopologySerializer serializer = getTopologySerializerForFilename(filename, jtp.topo);
        String fileContent = jtp.topo.getFileManager().read(filename);
        serializer.importFromString(jtp.topo, fileContent);
        if(window != null)
            window.setSize(jtp.topo.getWidth(), jtp.topo.getHeight());
    }

    /**
     * Manages user interactions in order to retrieve a filename.
     *
     * @return the filename in case of success, null otherwise
     */
    private String getFilenameFromUser(boolean saveFile) {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        if (saveFile)
            fc.showSaveDialog(jtp.getParent());
        else
            fc.showOpenDialog(jtp.getParent());
        if (fc.getSelectedFile() == null)
            return null;
        return fc.getSelectedFile().toString();
    }

    /**
     * <p>Search a known {@link TopologySerializer} matching with the provided filename.<br>
     * If not found, the result of {@link Topology#getSerializer()} is returned.</p>
     *
     * @param filename the filename too be matched
     * @param topology the {@link Topology} to use as fallback if no match is found
     * @return a {@link TopologySerializer}
     */
    private TopologySerializer getTopologySerializerForFilename(String filename, Topology topology) {
        TopologySerializer serializer = topologySerializerFilenameMatcher.getTopologySerializerFor(filename);

        if(serializer == null)
            serializer = topology.getSerializer();

        return serializer;
    }

    /**
     * <p>Initializes the {@link TopologySerializerFilenameMatcher} with known/available {@link TopologySerializer}s.</p>
     */
    private void initFilenameMatcher() {
        topologySerializerFilenameMatcher = new TopologySerializerFilenameMatcher();
        addTopologySerializer(".*\\.tikz", new TikzTopologySerializer());
        addTopologySerializer(".*\\.dot", new DotTopologySerializer());
        addTopologySerializer(".*\\.xdot", new DotTopologySerializer());
        addTopologySerializer(".*\\.xml", new XMLTopologySerializer(false));
        addTopologySerializer(".*\\.plain", new PlainTopologySerializer());
    }

    /**
     * <p>Records and links the provided {@link TopologySerializer} to a specific regular expression.</p>
     * <p>This couple will be used during {@link Topology} import/export. If the file's name matches the provided regular
     * expression, the linked {@link TopologySerializer} will be used for (de)serialization.</p>
     *
     * @param regexp a regular expression matching the file names
     * @param topologySerializer the {@link TopologySerializer} to be recorded
     */
    public void addTopologySerializer(String regexp, TopologySerializer topologySerializer) {
        topologySerializerFilenameMatcher.addTopologySerializer(regexp, topologySerializer);
    }

    @Override
    public void onPropertyChanged(Properties o, String p) {
        if (slideBarType != null) {
            if (p.equals("communicationRange") && slideBarType == BarType.COMMUNICATION)
                slideBar.setValue((int) jtp.topo.getCommunicationRange());
            else if (p.equals("sensingRange") && slideBarType == BarType.SENSING)
                slideBar.setValue((int) jtp.topo.getSensingRange());
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (slideBarType == BarType.COMMUNICATION) {
            jtp.topo.setCommunicationRange(slideBar.getValue());
        } else if (slideBarType == BarType.SENSING) {
            jtp.topo.setSensingRange(slideBar.getValue());
        } else if (slideBarType == BarType.SPEED) {
            jtp.topo.setClockSpeed(getClockSpeedFromSlideBarValue(slideBar.getMaximum(), slideBar.getValue()));
        }
        jtp.updateUI();
    }


    // region ClockSpeed <-> JSlider conversion

    /**
     * Computes the clock speed associated with the provided slider parameters.
     * @param slideBarMaxValue the maximum value of the slider (values in [0:slideBarMaxValue]), as an integer.
     * @param slideBarValue the value of the slider, as an integer.
     * @return the corresponding clock speed.
     */
    static int getClockSpeedFromSlideBarValue(final int slideBarMaxValue, final int slideBarValue) {
        return (int) computeClockSpeed(slideBarMaxValue, slideBarValue);
    }

    protected static double computeClockSpeed(final double maxValue, final double value) {
        double normalizedValue = (value / maxValue) * AccelerationFunctionExp.ZERO_AT;

        double normalizedClockSpeed = AccelerationFunctionExp.of(normalizedValue);
        return normalizedClockSpeed;
    }

    /**
     * Computes the value of the slider (i.e. its position) associated with the provided clock speed.
     *
     * @param slideBarMaxValue the maximum value of the slider (values in [0:slideBarMaxValue]), as an integer.
     * @param clockSpeed the clock speed, as an integer.
     * @return the corresponding value of the slider.
     */
    static int getSliderValueFromClockSpeed(final int slideBarMaxValue, final int clockSpeed) {
        return (int) computeSliderPosition(slideBarMaxValue, clockSpeed);
    }

    protected static double computeSliderPosition(double maxValue, double value) {
        double functionSpacedPosition = AccelerationFunctionExp.inverseOf(value);

        double sliderSpacedPosition = (functionSpacedPosition / AccelerationFunctionExp.ZERO_AT) * maxValue;
        return sliderSpacedPosition;
    }

    /**
     * The {@link AccelerationFunctionExp} class implements the following function in
     * [0:{@link #ZERO_AT}]:
     * <ul>
     *  <li> {@value unShiftedValueAtZero} {@code * Math.exp(-x / }{@value decreaseSpeed}{@code ) - }{@value shift} </li>
     * </ul>
     * Two methods are provided:
     * <ul>
     *     <li>{@link #of(double)}: computes the function;</li>
     *     <li>{@link #inverseOf(double)}: computes the inverse of the function.</li>
     * </ul>
     */
    public static class AccelerationFunctionExp {

        private static final double conversionFactor = 125;
        private static final double unShiftedValueAtZero = 5;
        private static final double decreaseSpeed = 15;
        private static final double shift = 3;

        /**
         * The exact value of the function at abscissa 0.
         */
        public static final double VALUE_AT_ZERO = 3000;

        /**
         * The exact abscissa at which the function reaches 0.
         */
        public static final double ZERO_AT = 30517578125./14348907.;
        public static final double ZERO_AT_APPROX_FOR_DOC = 2126.82249;

        /**
         * <p>Computes a value of the acceleration function associated with the provided parameter.</p>
         * <p>Currently, the associated descending function is defined on [0:{@value #ZERO_AT_APPROX_FOR_DOC}] with the
         * following remarkable points:</p>
         * <ul>
         *     <li>f(0) = {@value #VALUE_AT_ZERO}</li>
         *     <li>f({@value #ZERO_AT_APPROX_FOR_DOC}) = 0</li>
         * </ul>
         * @param x a double in [0:{@link #ZERO_AT}].
         * @return the value of the acceleration function
         */
        public static double of(double x) {
            if(x<=0) return AccelerationFunctionExp.VALUE_AT_ZERO;

            double y = unShiftedValueAtZero * Math.exp(-Math.log(x) / decreaseSpeed) - shift;
            return conversionFactor * y;
        }

        /**
         * <p>Inverts the acceleration function for the provided value.</p>
         * <p>Currently, the associated ascending function is defined on [0:{@value #VALUE_AT_ZERO}] with the following
         * remarkable points:</p>
         * <ul>
         *     <li>f({@value #VALUE_AT_ZERO}) = 0</li>
         *     <li>f(0) = {@value #ZERO_AT_APPROX_FOR_DOC}</li>
         * </ul>
         * @param y a double in [0:{@value #VALUE_AT_ZERO}].
         * @return the associated abscissa the acceleration function
         */
        public static double inverseOf(double y) {
            y /= conversionFactor;
            return Math.exp(-decreaseSpeed*Math.log((y+shift)/unShiftedValueAtZero));
        }
    }
    // endregion
}
