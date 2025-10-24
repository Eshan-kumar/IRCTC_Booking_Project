package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;

    private List<User> userlist;

    private static final ObjectMapper objectMapper=new ObjectMapper();

    private static final String USERS_PATH="src/main/java/ticket/booking/localDB/users.json";


    public UserBookingService(User user1)throws IOException {
        this.user=user1;
//          File users=new File(USERS_PATH);//         userlist=objectMapper.readValue(users, new TypeReference<List<User>>() {});
        this.userlist=loadUsers();
    }

    // for loading
    public UserBookingService()throws IOException{
        this.userlist=loadUsers();
    }

    public List<User> loadUsers()throws  IOException{
        File users=new File(USERS_PATH);
        return objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public Boolean loginUser(String name,String password){
        Optional<User> foundUser=userlist.stream().filter(user1->{
            return user1.getName().equalsIgnoreCase(name) && UserServiceUtil.checkPassword(password,user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userlist.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch(IOException e){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile()throws IOException{
        File usersfile=new File(USERS_PATH);
        objectMapper.writeValue(usersfile,userlist);
    }
    // json--> object(User) -> deserialize
    // object(User) --> json -> deserialize

    public void  fetchBooking(){
        user.printTickets();
    }
    public Boolean cancelBooking(String ticketId){
        if(ticketId==null|| ticketId.isEmpty()){
            System.out.println("Ticket Id cannot found");
            return Boolean.FALSE;
        }

        String finalTicketId=ticketId;
        // saving boolean for remove
        boolean removed=user.getTicketBooked().removeIf(ticket->ticket.getTicketId().equals(finalTicketId));
        // actually removing
//        user.getTicketBooked().removeIf(ticket->ticket.getTicketId().equals(finalTicketId));

        if(removed){
            System.out.println("Ticket ID "+ ticketId+" is cancelled");
            return Boolean.TRUE;
        }else{
            System.out.println("No Ticket found with this ID: "+ticketId);
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String source, String dest){
        try{
            TrainService trainService=new TrainService();
            return trainService.searchTrains(source,dest);
        }catch(IOException e){
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
    }

    public boolean bookTrainSeat(Train train, int row, int seat) throws IOException {
        try{
            TrainService trainService=new TrainService();
            List<List<Integer>> seats=train.getSeats();

            if(row>=0&&row<seats.size()&&seat>=0&&seat<seats.get(row).size()){
                if(seats.get(row).get(seat)==0){
                    seats.get(row).set(seat,1);
                    train.setSeats(seats);
                    trainService.updateTrain(train);
                    return true;
                }else{
                    return false; // seat is already booked
                }
            }else{
                    return false;
                }
        }catch(IOException e){
            return Boolean.FALSE;
        }
    }

}