/*
 * AVRS - http://avrs.sourceforge.net/
 *
 * Copyright (C) 2011 John Gorkos, AB0OO
 *
 * AVRS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * AVRS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AVRS; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package net.ab0oo.aprs.avrs;

/**
 * This is the main class for the AVRS (Automatic Voice Relay System) that Bob
 * Bruninga, WB4APR has outlined here:  http://www.aprs.org/avrs.html
 * This class connects to the APRS-IS system and monitors for packets destined
 * for AVRS (in the destination call segment of a Message Packet).  
 * It interfaces with a postgreSQL spatially enabled database to determine relative
 * distances to Internet/radio interface nodes and creates messages appropriate to
 * facilitating connections between distant stations using Internet linking technologies
 *
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.ab0oo.aprs.avrs.db.AVRSDao;
import net.ab0oo.aprs.avrs.db.jdbc.JdbcAVRSDao;
import net.ab0oo.aprs.avrs.models.AllPositionEntry;
import net.ab0oo.aprs.clients.PacketListener;
import net.ab0oo.aprs.parser.APRSPacket;
import net.ab0oo.aprs.parser.InformationField;
import net.ab0oo.aprs.parser.MessagePacket;
import net.ab0oo.aprs.parser.Parser;
import net.ab0oo.aprs.parser.Utilities;
import net.ab0oo.aprs.wedjat.models.ReferencePoint;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.postgresql.ds.PGSimpleDataSource;

class AVRSServer implements PacketListener {

    static DecimalFormat                                  df                = new DecimalFormat("###.000000");
    static DecimalFormat                                  distFmt           = new DecimalFormat("###.00");
    static DataOutputStream                               outToServer;
    static Socket                                         clientSocket;
    static BufferedReader                                 inFromServer;
    static ScheduledThreadPoolExecutor                    timer             = new ScheduledThreadPoolExecutor(3);
    static Map<String, ConversationThread>                scheduledMessages = new HashMap<String, ConversationThread>();
    static Map<String, ConversationThread>                scheduledAcks     = new HashMap<String, ConversationThread>();
    static Map<Date, ScheduledFuture<ConversationThread>> futures           = new HashMap<Date, ScheduledFuture<ConversationThread>>();
    static String                                         username          = null, database = null;
    static String                                         password          = null, host = null, aprsIsServer = null;
    static String                                         callsign          = null, aprsPass = null;
    private int                                           staleMs           = 900000;
    private double                                        maxLinkDistance   = 100;
    static int                                            messageNumber     = 0, port = 10152;
    private AVRSDao                                       dao;

    public static void main(String argv[]) {
        AVRSServer server = new AVRSServer();
        try {
            XMLConfiguration config = new XMLConfiguration("config/avrs.xml");
            username = config.getString("postgres.username");
            database = config.getString("postgres.dbname");
            password = config.getString("postgres.password");
            host = config.getString("postgres.host");
            aprsIsServer = config.getString("aprsis.host");
            port = config.getInt("aprsis.port");
            callsign = config.getString("aprsis.callsign");
            aprsPass = config.getString("aprsis.password");
            server.setStaleMs(config.getInt("avrs.staleMs"));
            server.setMaxLinkDistance(config.getDouble("avrs.maxLinkDistance"));
        } catch (ConfigurationException cfex) {
            System.err.println("Unable to load avrs.xml configuration.");
            System.exit(1);
        }
        PGSimpleDataSource source = new PGSimpleDataSource();
        source.setServerName(host);
        source.setDatabaseName(database);
        source.setUser(username);
        source.setPassword(password);
        AVRSDao adao = new JdbcAVRSDao();
        adao.setDataSource(source);
        server.setDao(adao);

        server.connectAndListen();
    }

    public AVRSServer() {
        System.out.println("Initializing AVRS Server");
        // timer.scheduleAtFixedRate(new AckReaper(), 5, 2, TimeUnit.SECONDS);
    }

    private void connectAndListen() {
        String sentence;
        String modifiedSentence = "";
        try {
            clientSocket = new Socket(aprsIsServer, port);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // sentence = "user ab0oo-15 pass 19951 vers test 1.0 filter a/35.000/-85.6/30.3555/-80.84";
            sentence = "user " + callsign + " pass " + aprsPass + " vers test 1.0 filter t/mp";
            outToServer.writeBytes(sentence + '\n');
        } catch (Exception ex) {
            System.err.println("Unable to contact/log-in to APRS-IS:  " + ex);
            System.exit(1);
        }
        APRSPacket packet = null;
        while (true) {
            try {
                modifiedSentence = inFromServer.readLine();
                if (modifiedSentence.length() > 1 && modifiedSentence.charAt(0) != '#') {
                    try {
                        packet = Parser.parse(modifiedSentence);
                    } catch (Exception ex) {
                        System.err.println("Unable to parse: " + modifiedSentence);
                        ex.printStackTrace();
                        continue;
                    }
                    if (packet.hasFault()) {
                        System.err.println("Bad Packet");
                        continue;
                    }
                    processPacket(packet);
                } else {
                    System.out.println(modifiedSentence);
                }
                System.out.println(new Date() + ":  " + modifiedSentence);

            } catch (Exception ex) {
                System.err.println("Exception during Network read:  " + ex);
                ex.printStackTrace();
                System.err.println("Culprit was " + modifiedSentence);
            }
        }
        // clientSocket.close();
    }

    @Override
    public void processPacket(APRSPacket packet) {
        try {
            if (packet.isAprs() && packet.getAprsInformation() instanceof MessagePacket) {
                MessagePacket mp = (MessagePacket) packet.getAprsInformation();
                String sourceCall = packet.getSourceCall();
                String targetCall = mp.getTargetCallsign().toUpperCase().trim();
                if (mp.getMessageBody().contains("AA:") || mp.getMessageBody().contains("[AA]")) {
                    System.out.println("Dealing with autoresponder; aborting transaction");
                    System.out.println("Message from " + packet.getSourceCall() + " was: " + mp.getMessageBody());
                    return;
                }
                if (targetCall.equals("AVRS")) {
                    System.out.println(packet.toString());
                    processAvrs(sourceCall, mp);
                } else if (targetCall.equals("LOCATE")) {
                    System.out.println(packet.toString());
                    processLocate(sourceCall, mp);
                } else {
                    // System.out.println(packet.getSourceCall() +
                    // " -> " + mp.getTargetCallsign()
                    // + "["+mp.getMessageNumber()+"]"+":  "
                    // + mp.getMessageBody());
                    // System.out.println("\tExtracted from "+modifiedSentence);
                }
            }
        } catch (Exception ex) {
            System.err.println(new Date() + "!!!!! Exception tossed during packet processing");
            ex.printStackTrace();
        }

    }

    public void processAvrs(String sourceCall, MessagePacket mp) {
        String messageBody = mp.getMessageBody().toUpperCase().trim();
        if (mp.isAck()) {
            System.out.println("ACK " + mp.getMessageNumber() + " rxed from " + sourceCall);
            String key = sourceCall + ":" + mp.getMessageNumber();
            if (scheduledAcks.containsKey(key)) {
                System.out.println("This ACK was expected from " + sourceCall);
                ConversationThread ct = scheduledAcks.remove(key);
                ct.setCount(0);
                System.out.println("The timer for this ACK was removed.");
            }
        } else if (mp.isRej()) {
            System.out.println(sourceCall + " rejects message " + mp.getMessageNumber() + " from "
                    + mp.getTargetCallsign());
        } else if (messageBody.startsWith("?")) {
            if (mp.getMessageBody().trim().length() == 1) {
                sendAck(sourceCall, mp.getMessageNumber());
                sendClosestNode(sourceCall, sourceCall, '*');
            } else if (messageBody.length() == 2) {
                // this is a transport-specific query. The user has asked for
                // an echolink (?E), IRLP (?I), or AllStar (?A) node
                sendAck(sourceCall, mp.getMessageNumber());
                sendClosestNode(sourceCall, sourceCall, messageBody.charAt(1));
            } else {
                // this is for a query on another station in the format ?N0CAL
                sendAck(sourceCall, mp.getMessageNumber());
                sendClosestNode(mp.getMessageBody().substring(1), sourceCall, '*');
            }
        } else {
            sendAck(sourceCall, mp.getMessageNumber());
            System.out.println("Message body is \"" + mp.getMessageBody() + "\"");
            setupAvrsLink(sourceCall.toUpperCase(), mp.getMessageBody());

        }
    }

    public void processLocate(String sourceCall, MessagePacket mp) {
        if (mp.isAck()) {
            System.out.println("ACK " + mp.getMessageNumber() + " rxed from " + sourceCall);
            String key = sourceCall + ":" + mp.getMessageNumber();
            if (scheduledAcks.containsKey(key)) {
                System.out.println("This ACK was expected from " + sourceCall);
                ConversationThread ct = scheduledAcks.remove(key);
                ct.setCount(0);
                System.out.println("The timer for this ACK was removed.");
            }
        } else if (mp.isRej()) {
            System.out.println(sourceCall + " rejects message " + mp.getMessageNumber() + " from "
                    + mp.getTargetCallsign());
        } else {
            if (mp.getMessageBody().trim().length() == 1) {
                sendAck(sourceCall, mp.getMessageNumber());
                sendLocation(sourceCall, sourceCall, '*');
            } else {
                // this is for a query on another station in the format ?N0CAL
                sendAck(sourceCall, mp.getMessageNumber());
                sendLocation(mp.getMessageBody(), sourceCall, '*');
            }
        }
    }

    /**
     * 
     * @param callsign
     * @return This method will search the database for the most likely station capable of recieving an AVRS message and
     *         responding to it. It uses symbol, timestamp of last message, movement of target, and any inferred
     *         equipment types to decide which SSID of a callsign is most likely to be monitored.
     */
    private String getBestTarget(String callsign) {
        String bestTarget = callsign;
        System.out.println(new Date() + ":  Looking for last position of all SSIDs for "
                + APRSPacket.getBaseCall(callsign));
        List<AllPositionEntry> allPositions = dao.getPositions(APRSPacket.getBaseCall(callsign));
        if (allPositions == null || allPositions.size() == 0) {
            bestTarget = null;
        }
        System.out.println(new Date() + ":  Found " + allPositions.size() + " valid SSIDs for " + callsign);
        // TODO This needs to actually do more to figure out WHICH station is message capable.
        // right now, it returns the callsign of the most RECENT SSID to transmit
        if (allPositions.size() > 1) {
            Collections.sort(allPositions);
            bestTarget = allPositions.get(0).getCallsign();
            System.out.println("Multiple choices for " + callsign + ", using " + bestTarget);
        }
        return bestTarget;
    }

    private void setupAvrsLink(String sourceCall, String targetCall) {
        System.out.println(new Date() + ":  Looking for best target callsign for " + targetCall);
        String bestTarget = getBestTarget(targetCall.toUpperCase());
        System.out.println("Best target is " + bestTarget);
        if (bestTarget == null) {
            sendAndGetAck("AVRS", sourceCall, "Unable to find a position for " + targetCall);
            return;
        }
        LinkPair nodePair = findBestPath(sourceCall.toUpperCase(), bestTarget.toUpperCase());
        if (nodePair.getTargetNode() == null) {
            sendAndGetAck("AVRS", sourceCall, targetCall + " out of range of internet node");
            return;
        }
        if (nodePair.getSourceNode() == null) {
            sendAndGetAck("AVRS", sourceCall, "You are out of range of internet node");
            return;
        }
        double freq = nodePair.getTargetNode().getFrequency();
        String tone = nodePair.getTargetNode().getTone();
        String targetMessage = String.format("VOICE CALL from %s, PSE QSY %3.3fMHz T%-3s", sourceCall, freq, tone);
        sendAndGetAck("AVRS", bestTarget, targetMessage);
        System.out.println(new Date() + ":  " + bestTarget + ">" + targetMessage);
        freq = nodePair.getSourceNode().getFrequency();
        tone = nodePair.getSourceNode().getTone();
        String type = nodePair.getSourceNode().getNodeType();
        int nodeId = nodePair.getTargetNode().getNode();
        String sourceMessage = String.format("CALL %s on %3.3fMHz T%-3s %s node %d", bestTarget, freq, tone, type,
            nodeId);
        System.out.println(new Date() + ":  " + sourceCall + ">" + sourceMessage);
        sendAndGetAck("AVRS", sourceCall, sourceMessage);
    }

    private void sendLocation(String callsign, String requestor, char nodeType) {
        System.out.println(new Date() + ":  " + requestor + " wants to know the location of '" + callsign + "'");
        Date lastPosition = dao.getLatestPosition(callsign);
        if (lastPosition == null) {
            System.err.println("Unkown last position for " + callsign);
            sendAndGetAck("LOCATE", requestor, "UNKNOWN POSITION FOR " + callsign);
            return;
        }
        System.out.println(new Date() + ":  Timestamp of last position is " + lastPosition);
        if (System.currentTimeMillis() - lastPosition.getTime() > staleMs) {
            long elapsedTime = (System.currentTimeMillis() - lastPosition.getTime()) / 1000;
            System.out.println(new Date() + ":  Last position from " + callsign + " is " + elapsedTime
                    + " seconds old.");
        }
        AllPositionEntry lastPositionEntry = dao.getPosition(callsign);
        if (lastPositionEntry != null) {
            List<ReferencePoint> closestCities = dao.listClosestCities(lastPositionEntry.getPosition());
            ReferencePoint closestCity = closestCities.get(0);
            String alertString = callsign + " heard ";
            Date lastHeard = lastPositionEntry.getToi();
            System.out.println("lastHeard is " + lastPositionEntry.getToi());
            if (lastHeard != null) {
                alertString += (toDms(System.currentTimeMillis() - lastPositionEntry.getToi().getTime())) + " ago ";
            }
            double distance = closestCity.getMetersDistance();
            String units = "km ";
            if (requestor.startsWith("A") || requestor.startsWith("G") || requestor.startsWith("K")
                    || requestor.startsWith("N") || requestor.startsWith("W")) {
                distance = Utilities.metersToMiles(distance);
                units = "mi ";
            }
            alertString += distFmt.format(distance) + units;
            alertString += Utilities.degressToCardinal(closestCity.getBearingTo()) + " of ";
            alertString += closestCity.getCity() + ", " + closestCity.getRegion();
            sendAndGetAck("LOCATE", requestor, alertString);
        }
    }

    public String toDms(long interval) {
        long seconds = interval / 1000;
        long days = seconds / 86400;
        seconds = seconds % 86400;
        long hours = seconds / 3600;
        seconds = seconds % 3600;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return days + "d" + hours + "h" + minutes + "m" + seconds + "s";

    }

    private void sendClosestNode(String callsign, String requestor, char nodeType) {
        System.out.println(new Date() + ":  " + requestor + " wants to know the closest VOIP node to '" + callsign
                + "'");
        if (nodeType != '*') {
            System.out.println(new Date() + ": Interested only in " + nodeType + " nodes");
        }
        // first we set up the acknowledgment
        Date lastPosition = dao.getLatestPosition(callsign);
        if (lastPosition == null) {
            System.err.println("Unkown last position for " + callsign);
            sendAndGetAck("AVRS", requestor, "UNKNOWN POSITION FOR " + callsign);
            return;
        }
        System.out.println(new Date() + ":  Timestamp of last position is " + lastPosition);
        if (System.currentTimeMillis() - lastPosition.getTime() > staleMs) {
            long elapsedTime = (System.currentTimeMillis() - lastPosition.getTime()) / 1000;
            System.out.println(new Date() + ":  Last position from " + callsign + " is " + elapsedTime
                    + " seconds old.");
        }
        TreeSet<LinkNode> nodes = dao.getNodes(callsign);
        int gwCount = 0;
        for (LinkNode node : nodes) {
            if (nodeType != '*' && node.getNodeType().charAt(0) != nodeType)
                continue;
            gwCount++;
            if (gwCount < 3) {
                String comment = String.format("%3.3fMHz T%-3s %s#%d %2.2f miles", node.getFrequency(), node.getTone(),
                    node.getNodeType(), node.getNode(), node.getDistance());
                // Position p = new Position(node.getLatitude(), node.getLongitude(), 0, '/', 'n');
                // ObjectPacket op = new ObjectPacket(node.getNodeType() + "#" + node.getNode(), true, p, comment);
                if (gwCount == 1) {
                    if (node.getDistance() > maxLinkDistance) {
                        System.out.println(new Date() + ":  " + callsign + " too far away from a working VOIP node");
                        comment = "SRY, " + callsign + " >50 miles from closest VOIP node";
                    }
                    sendAndGetAck("AVRS", requestor, comment);
                }
                // sendNoAck(op);
            }
        }
    }

    /**
     * @param callsign
     *            - Destination callsign of the station this message is destined for
     * @param message
     *            - Body of the message
     * @param txCount
     *            - Number of times to transmit this message (unless an ack is received)
     * @param txInterval
     *            - Frequency in seconds to send this message.
     * @return - the APRS Message Number assigned to this message (for ack purposes)
     */
    private int sendAndGetAck(String fromcall, String callsign, String message, int txCount, int txInterval) {
        MessagePacket mp = new MessagePacket(callsign, message, Integer.toString(messageNumber));
        APRSPacket outgoingPacket = new APRSPacket(fromcall, "APZ013", null, mp);
        System.out.println(outgoingPacket.toString());
        messageNumber++;
        ResponseThread ackThread = new ResponseThread(outgoingPacket, txCount, txInterval, outToServer, timer);
        timer.schedule(ackThread, 0L, TimeUnit.SECONDS);
        String id = mp.getTargetCallsign() + ":" + mp.getMessageNumber();
        scheduledAcks.put(id, ackThread);
        return messageNumber;
    }

    private int sendAndGetAck(String fromcall, String callsign, String message) {
        return sendAndGetAck(fromcall, callsign, message, 3, 10);
    }

    @SuppressWarnings("unused")
    private void sendNoAck(InformationField infoField) {
        APRSPacket outgoingPacket = new APRSPacket("AVRS", "APZ013", null, infoField);
        System.out.println(new Date() + ":  Sending: " + outgoingPacket.toString());
        Runnable msgThread = new ResponseThread(outgoingPacket, 3, 10, outToServer, timer);
        timer.schedule(msgThread, 0L, TimeUnit.SECONDS);
    }

    private void sendAck(String callsign, String messageNumber) {
        System.out.println("Sending ACK to " + callsign + " for MSG NUM " + messageNumber);
        ConversationThread ackThread = new AckThread(true, callsign, messageNumber, 3, outToServer, timer);
        timer.schedule(ackThread, 0L, TimeUnit.SECONDS);
    }

    private LinkPair findBestPath(String source, String target) {
        // start out pulling nodes within 50 miles of both the caller and the callee
        TreeSet<LinkNode> sourceNodes = dao.getNodes(source);
        TreeSet<LinkNode> targetNodes = dao.getNodes(target);
        // step through the source node list, looking in the target node list for matches
        LinkNode bestSourceNode = null;
        LinkNode bestTargetNode = null;
        double bestDistance = maxLinkDistance * 2;
        for (LinkNode sourceNode : sourceNodes) {
            String sourceNodeType = sourceNode.getNodeType();
            for (LinkNode targetNode : targetNodes) {
                String targetNodeType = targetNode.getNodeType();
                if (sourceNodeType.equals(targetNodeType)) {
                    double distance = sourceNode.getDistance() + targetNode.getDistance();
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestSourceNode = sourceNode;
                        bestTargetNode = targetNode;
                    }
                }
            }
        }
        return new LinkPair(bestSourceNode, bestTargetNode);
    }

    /**
     * @param dao
     *            the dao to set
     */
    public final void setDao(AVRSDao dao) {
        this.dao = dao;
    }

    /**
     * @return the maxLinkDistance
     */
    public final double getMaxLinkDistance() {
        return maxLinkDistance;
    }

    /**
     * @param maxLinkDistance
     *            the maxLinkDistance to set
     */
    public final void setMaxLinkDistance(double maxLinkDistance) {
        this.maxLinkDistance = maxLinkDistance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.ab0oo.aprs.clients.PacketListener#setOutputStream(java.io.DataOutputStream)
     */
    @Override
    public void setOutputStream(DataOutputStream outStream) {
        outToServer = outStream;

    }

    /**
     * @param staleMs
     *            the staleMs to set
     */
    public final void setStaleMs(int staleMs) {
        this.staleMs = staleMs;
    }
}