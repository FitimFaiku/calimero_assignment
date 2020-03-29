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

import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.KNXMediumSettings;
import tuwien.auto.calimero.link.medium.TPSettings;
import java.net.InetSocketAddress;

/**
 * Example of Calimero process communication, we read (and write) a boolean datapoint in the KNX network. By default,
 * this example will not change any datapoint value in the network.
 */
public class ProcessListener
{
    /**
     * The local host used for the connection. Replace the IP address with a local of yours.
     */
    private static final String localHost = "172.22.1.20";

	// Address of your KNXnet/IP server. Replace the IP host or address as necessary.
	private static final String remoteHost = "172.22.10.71";

	// We will read a boolean from the KNX datapoint with this group address, replace the address as necessary.
	// Make sure this datapoint exists, otherwise you will get a read timeout!
	private static final String readGroup = "1/0/5"; // Light state A3 on/off

	private static final String writeGroup = "1/0/4";

	public static void main(final String[] args) {

		final InetSocketAddress remote = new InetSocketAddress(remoteHost, 3671);
        final InetSocketAddress localEP = new InetSocketAddress(localHost, 0);
		// Create our network link, and pass it to a process communicator
        KNXMediumSettings knxMediumSettings = TPSettings.TP1;
		try (KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(localEP, remote, false, knxMediumSettings);
            ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink)){
			pc.toggleOnOffForGivenSeconds(readGroup,writeGroup, 10);
		}

		catch (KNXException | InterruptedException e) {
			System.out.println("Error accessing KNX datapoint: " + e.getMessage());
		}

	}
}
