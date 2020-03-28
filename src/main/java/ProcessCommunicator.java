import tuwien.auto.calimero.KNXFormatException;
import tuwien.auto.calimero.KNXRemoteException;
import tuwien.auto.calimero.KNXTimeoutException;
import tuwien.auto.calimero.link.KNXLinkClosedException;

public interface ProcessCommunicator extends tuwien.auto.calimero.process.ProcessCommunicator {
    /**
     *
     * @param groupAddress
     * @throws KNXTimeoutException
     * @throws KNXLinkClosedException
     * @throws InterruptedException
     * @throws KNXFormatException
     * @throws KNXRemoteException
     */
    public void toogleOnOff(String groupAddress) throws KNXTimeoutException, KNXLinkClosedException, InterruptedException, KNXFormatException, KNXRemoteException;
}
