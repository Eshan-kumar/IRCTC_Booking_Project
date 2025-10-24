package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    @JsonProperty("date_of_travel")
    private String dateOfTravel;
    private Train train;

    public Ticket(String ticketId, String userId, String source, String destination, String dateOfTravel, Train train) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.train = train;
    }

    public Ticket(){}

// Getters
    public String getTicketId() {return this.ticketId;}
    public String getUserId() {return this.userId;}
    public String getSource() {return this.source;}
    public String getDestination() {return this.destination;}
    public String getDateOfTravel() {return this.dateOfTravel;}
    public Train getTrain() {return this.train;}

    // Setters
    public void setTicketId(String ticketId) {this.ticketId = ticketId;}
    public void setUserId(String userId) {this.userId = userId;}
    public void setSource(String source) {this.source = source;}
    public void setDestination(String destination) {this.destination = destination;}
    public void setDateOfTravel(String dateOfTravel) {this.dateOfTravel = dateOfTravel;}
    public void setTrain(Train train) {this.train = train;}

    public String getTicketInfo(){
        return String.format("Ticket ID: %s belongs to USer %s from %s to %s ",ticketId,userId,source,destination);
    }
}
