/*
    Calimero 2 - A library for KNX network access
    Copyright (c) 2013, 2017 B. Malinowsky

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

import org.slf4j.Logger;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.datapoint.DatapointMap;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.KNXMediumSettings;
import tuwien.auto.calimero.link.medium.TPSettings;
import tuwien.auto.calimero.log.LogService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Example of Calimero process communication, we read (and write) a boolean datapoint in the KNX network. By default,
 * this example will not change any datapoint value in the network.
 */
public class XMLProcessor
{

	private static final Logger logger = LogService.getLogger("XMLProcessor");
    /**
     * The local host used for the connection. Replace the IP address with a local of yours.
     */
    private static final String localHost = "172.22.1.20";

	// Address of your KNXnet/IP server. Replace the IP host or address as necessary.
	private static final String remoteHost = "172.22.10.71";

	// We will read a boolean from the KNX datapoint with this group address, replace the address as necessary.
	// Make sure this datapoint exists, otherwise you will get a read timeout!
	private static final String group = "1/0/5"; // Light state A3 on/off

	private static ArrayList<GroupAddress> groupAddresses = new ArrayList<>();

	public static void main(final String[] args) {

		try {
			iterateOverXML();
		}
		catch (FileNotFoundException e) {
			System.out.println("Error accessing KNX datapoint: " + e.getMessage());
		}
	}

	/**
	 * This Static Method iterates over the Example XML and finds every Datapoint with the 1.001 DPT
	 * and sets those boolean values to true for 20 seconds and afterwards sets them to false
	 * @throws FileNotFoundException If the Example.xml file is not found
	 */
	public static void iterateOverXML() throws FileNotFoundException {
		// our XML file for this example
		File xmlFile = new File("src/main/resources/Example.xml");

		// Let's get XML file as String using BufferedReader
		// FileReader uses platform's default character encoding
		// if you need to specify a different encoding, use InputStreamReader
		Reader fileReader = new FileReader(xmlFile);

		DefaultXMLReader xmlReader = new DefaultXMLReader(fileReader, true);
		DatapointMap<Datapoint> map = new DatapointMap<Datapoint>();
		map.load(xmlReader);
		map.getDatapoints();

		final InetSocketAddress remote = new InetSocketAddress(remoteHost, 3671);
		final InetSocketAddress localEP = new InetSocketAddress(localHost, 0);
		// Create our network link, and pass it to a process communicator
		KNXMediumSettings knxMediumSettings = TPSettings.TP1;
		try (KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(localEP, remote, false, knxMediumSettings);
			 ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink)){
			// Iterate over all Entrys and when DPT is 1.001 then we set those lights to on
			for (Datapoint point: map.getDatapoints()){
				if(point.getDPT().equals("1.001")){
					logger.info("Got Datapoint with DPT 1.001 the Groupaddress is: " + point.getMainAddress().toString() + " this boolean value will be set to true now.");
					// Set light on if 1.001 and add to Arraylist to later turnoff
					pc.write(point.getMainAddress(), true);
					groupAddresses.add(point.getMainAddress());
				}
			}
			// WAit 20 seconds and turn the lights off
			TimeUnit.SECONDS.sleep(20);
			logger.info("20 seconds are over now");
			for(GroupAddress address : groupAddresses){
				logger.info("Groupaddress " + address.toString() +" will be set to false now.");
				pc.write(address, false);
			}
		}
		catch (KNXException | InterruptedException e) {
			System.out.println("Error accessing KNX datapoint: " + e.getMessage());
		}
	}
}
