import java.util.Date;

public class Trip {
    private int id;
    private double distance;
    private String transport;
    private double carbon;
    private String suggestedTransport;
    private double potentialSaving;
    private Date date;

    public Trip(double distance, String transport, double carbon, String suggestedTransport, double potentialSaving, Date date) {
        this.distance = distance;
        this.transport = transport;
        this.carbon = carbon;
        this.suggestedTransport = suggestedTransport;
        this.potentialSaving = potentialSaving;
        this.date = date;
    }

    public Trip(double distance, String transport, double carbon, String suggestedTransport, double potentialSaving) {
        this(distance, transport, carbon, suggestedTransport, potentialSaving, new Date());
    }

    // Getters
    public int getId() { return id; }
    public double getDistance() { return distance; }
    public String getTransport() { return transport; }
    public double getCarbon() { return carbon; }
    public String getSuggestedTransport() { return suggestedTransport; }
    public double getPotentialSaving() { return potentialSaving; }
    public Date getDate() { return date; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setDistance(double distance) { this.distance = distance; }
    public void setTransport(String transport) { this.transport = transport; }
    public void setCarbon(double carbon) { this.carbon = carbon; }
    public void setSuggestedTransport(String suggestedTransport) { this.suggestedTransport = suggestedTransport; }
    public void setPotentialSaving(double potentialSaving) { this.potentialSaving = potentialSaving; }
    public void setDate(Date date) { this.date = date; }
}
