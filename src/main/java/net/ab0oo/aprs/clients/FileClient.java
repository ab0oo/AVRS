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
package net.ab0oo.aprs.clients;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;

import net.ab0oo.aprs.parser.APRSPacket;
import net.ab0oo.aprs.parser.CourseAndSpeedExtension;
import net.ab0oo.aprs.parser.MessagePacket;
import net.ab0oo.aprs.parser.ObjectPacket;
import net.ab0oo.aprs.parser.Parser;
import net.ab0oo.aprs.parser.Position;
import net.ab0oo.aprs.parser.PositionPacket;

class FileClient {
	public static String fileName = "/home/johng/rawdata";
	public static int stopAt = 5000;
    static DecimalFormat df = new DecimalFormat("###.000000");

	public static void main(String argv[]) throws Exception {
        String modifiedSentence;
        //Parser parser = new Parser();
        BufferedReader inFromServer = new BufferedReader(new FileReader(fileName));
        APRSPacket packet = null;
        int count=0;
        while (inFromServer.ready() && count < stopAt ) {
            modifiedSentence = inFromServer.readLine();
            if ( modifiedSentence.charAt(0) != '#' ) {
                try {
                    packet = Parser.parse(modifiedSentence);
                } catch ( Exception ex ) {
                    System.err.println("Unable to parse: "+modifiedSentence);
                    ex.printStackTrace();
                    //break;
                }
                if ( packet.isAprs() && packet.getAprsInformation() instanceof PositionPacket) {
                	PositionPacket pp = (PositionPacket)packet.getAprsInformation();
                    Position p = pp.getPosition();
					if (p != null) {
						System.out.printf("%10s", packet.getSourceCall());
						String geomString = df.format(p.getLatitude()) + ","+ df.format(p.getLongitude());
						System.out.print(" -> ");
						System.out.printf("%10s", packet.getDestinationCall());
						System.out.print(" at " + geomString + "\t");
						System.out.print(" Symbol: "+p.getSymbolTable()+p.getSymbolCode());
						if ( pp.getExtension() != null && pp.getExtension() instanceof CourseAndSpeedExtension ) {
							CourseAndSpeedExtension cse = (CourseAndSpeedExtension)pp.getExtension();
							System.out.print(" Course: "+cse.getCourse()+", speed: "+cse.getSpeed());
						}
						System.out.println();
					}

                } else if ( packet.isAprs() && packet.getAprsInformation() instanceof MessagePacket) {
                	MessagePacket mp = (MessagePacket)packet.getAprsInformation();
                	System.out.println(packet.getSourceCall()+" -> "+mp.getTargetCallsign()+":  "+mp.getMessageBody());
                } else if ( packet.isAprs() && packet.getAprsInformation() instanceof ObjectPacket ) {
                    ObjectPacket obj = (ObjectPacket)packet.getAprsInformation();
                    String geomString = df.format(obj.getPosition().getLatitude()) + ","+ df.format(obj.getPosition().getLongitude());
                	System.out.println("Object packet for object "+obj.getObjectName()+" located at "+geomString);
                }
            } else {
                System.out.println(modifiedSentence);
            }
            count++;
        }
        // clientSocket.close();
    }
}