import tuwien.auto.calimero.KNXFormatException;
import tuwien.auto.calimero.KNXRemoteException;
import tuwien.auto.calimero.KNXTimeoutException;
import tuwien.auto.calimero.link.KNXLinkClosedException;


public interface ProcessCommunicator extends tuwien.auto.calimero.process.ProcessCommunicator {
    /**
     *
     * @param readStateGroupAddress The group Address from where the initial state will be read from
     * @param writeGroupAddress The group Address which we can write to to switch states from true to false from false to true
     * @param seconds The time how long the programm should run
     * @throws KNXTimeoutException
     * @throws KNXLinkClosedException
     * @throws InterruptedException
     * @throws KNXFormatException
     * @throws KNXRemoteException
     */
    public void toggleOnOffForGivenSeconds(String readStateGroupAddress, String writeGroupAddress, int seconds) throws KNXTimeoutException, KNXLinkClosedException, InterruptedException, KNXFormatException, KNXRemoteException;


}
