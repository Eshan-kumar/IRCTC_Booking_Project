package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public class User {
    @JsonProperty("name")private String name;
    @JsonProperty("password") private String password;
    @JsonProperty("hashedPassword") private String hashedPassword;
    @JsonProperty("tickets_booked") private List<Ticket> ticketBooked;
    @JsonProperty("user_id") private String userId;

    public User(String name,String password,String hashedPassword,List<Ticket> ticketBooked,String userId){
        this.name=name;
        this.password=password;
        this.hashedPassword=hashedPassword;
        this.ticketBooked=ticketBooked;
        this.userId=userId;
    }

    public User(){}

    public String getName(){
        return this.name;
    }
    public String getPassword(){
        return this.password;
    }
    public String getHashedPassword(){return this.hashedPassword;}
    public List<Ticket> getTicketBooked(){
        return this.ticketBooked;
    }
    public String getUserId(){
        return this.userId;
    }
    public void printTickets(){
       for(int i=0;i< ticketBooked.size();i++){
           System.out.println(ticketBooked.get(i).getTicketInfo());
        }
    }

// Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setTicketBooked(List<Ticket> ticketBooked) {
        this.ticketBooked = ticketBooked;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
