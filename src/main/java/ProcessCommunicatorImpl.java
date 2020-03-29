import org.slf4j.Logger;
import tuwien.auto.calimero.*;
import tuwien.auto.calimero.link.KNXLinkClosedException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.log.LogService;
import java.util.concurrent.TimeUnit;

public class ProcessCommunicatorImpl extends tuwien.auto.calimero.process.ProcessCommunicatorImpl implements ProcessCommunicator {
    private final Logger logger;
    /**
     * Creates a new process communicator attached to the supplied KNX network link.
     * <p>
     * The log service used by this process communicator is named "process " +
     * <code>link.getName()</code>.
     *
     * @param link network link used for communication with a KNX network
     * @throws KNXLinkClosedException if the network link is closed
     */
    public ProcessCommunicatorImpl(KNXNetworkLink link) throws KNXLinkClosedException {
        super(link);
        logger = LogService.getLogger("intruduction " + link.getName());
    }

    @Override
    public void toggleOnOffForGivenSeconds(String readStateGroupAddress, String writeGroupAddress, int seconds) throws KNXTimeoutException, KNXLinkClosedException, InterruptedException, KNXFormatException, KNXRemoteException {
        boolean onOrOff = this.readBool(new GroupAddress(readStateGroupAddress));
        for(int i=0; i<seconds; i++){
            onOrOff = !onOrOff;
            // Here we write to the given GroupAddress to turn on or off
            GroupAddress ga = new GroupAddress(writeGroupAddress);
            this.write(ga, onOrOff);
            logger.info("Switched value for groupAddress:" + writeGroupAddress + " to:" + onOrOff);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
