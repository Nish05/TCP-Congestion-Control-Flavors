/**
 * This acts as the listener of packets and ACKs for the routers
 *
 * @author Alimuddin Khan
 * @author Nisha Bhanushali
 */
public class TCPClassicalAckhandler
        implements Runnable{
    private TCP tcp;
    private Ack ack;

    /**
     * This is the parameterized constructor
     * @param tcp   TCP object
     * @param ack   Ack received
     */
    public TCPClassicalAckhandler(TCP tcp, Ack ack) {
        this.tcp = tcp;
        this.ack = ack;
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        CongestionWindow cw;

        // checking if we have the destination in our list
        if (tcp.getOutGoingConnections().containsKey(ack.source.getAddress())){

            cw = tcp.getOutGoingConnections().get(ack.getSource().getAddress());

            // make sure you are the only on editing the congestion window
            synchronized (cw){

                // check no of dupAckCounts
                if(ack.getDupAckCount() == 0){
                    // increase cwnd
                    cw.increaseCWNDsize();

                }

                // update the ACK status
                cw.updateAckStatus(ack.getAckID() - 1, true);

                // slide the window
                cw.slideWindow();

                // update cwnd contents
                cw.updateCongestionWindow();

            }

        }else{
            // destination not present
        }

    }
}


