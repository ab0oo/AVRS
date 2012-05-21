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
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.publicalerts.cap.Alert;
import com.google.publicalerts.cap.CapXmlParser;
import com.google.publicalerts.cap.Info;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


/**
 * @author johng
 *
 */
public class CapClient {
    private static boolean noFetch = false;
    
    public static void main(String[] args) {
        try {
            URL url = new URL("http://alerts.weather.gov/cap/ga.php?x=0");
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));
            @SuppressWarnings("unchecked")
            List<SyndEntry> entries = feed.getEntries();
            System.out.println(feed.getTitle());
            List<Alert> alerts = new ArrayList<Alert>();
            for ( SyndEntry entry : entries ) {
                System.out.println(entry.getTitle());
                if ( noFetch ) continue;
                URL capUrl = new URL(entry.getLink());
                BufferedReader capBr = new BufferedReader(new InputStreamReader(capUrl.openStream()));
                StringBuilder sb = new StringBuilder();
                String line = capBr.readLine();
                while ( line != null ) {
                    sb.append(line);
                    line = capBr.readLine();
                }
                CapXmlParser parser = new CapXmlParser(false);
                Alert alert = parser.parseFrom(sb.toString());
                System.out.println(alert.getStatus()+":"+alert.getScope()+":"+alert.getMsgType());
                List<Info> infoList = alert.getInfoList();
                for ( Info infoItem : infoList ) {
                    System.out.println("\t"+infoItem.getHeadline());
                    for ( int i=0; i < infoItem.getCategoryCount(); i++) {
                        System.out.print("\t\tCategory: "+infoItem.getCategory(i));
                    }
                    System.out.println();
                    System.out.println("\t"+infoItem.getSeverity()+":"+infoItem.getUrgency());
                    System.out.println("\t"+infoItem.getEvent());
                    for ( int i=0; i < infoItem.getAreaCount(); i++) {
                        System.out.println("\t\tArea affected: "+infoItem.getArea(i));
                    }
                }
                alerts.add(alert);
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
}
