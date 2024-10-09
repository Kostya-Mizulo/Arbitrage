package session;

public class Session {

    //  public Session session;
    private int deposit;
    private SessionStatus sessionStatus = SessionStatus.WAITING_FOR_INITIAL_DEPOSIT_INPUT;



    public Session(){
        sessionStatus = SessionStatus.INITIAL;
    }








    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
}
