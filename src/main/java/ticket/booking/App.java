package ticket.booking;


import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

import static java.util.UUID.randomUUID;

public class App {
    public static void main(String[] args) {
        System.out.println("Running Train booking system");
        Scanner scanner=new Scanner(System.in);

        int option=0;
        UserBookingService userBookingService;

        try{
            userBookingService=new UserBookingService();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("there is something wrong");
            scanner.close();
            return;
        }
        Train trainSelectedForBooking=new Train();
        while(option!=7){
            System.out.println("Choose a option");
            System.out.println("1.SignUp \n2. Login \n3. Fetch Booking\n4. Search Trains \n5. Book a Seat \n6. Cancel my Booking \n7. Exit the App");
            try {
                option= scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue;
            } catch (java.util.NoSuchElementException e) {
                System.out.println("No input available. Exiting...");
                break;
            }
            switch(option){
                case 1:
                    System.out.println("Enter the username to signup");
                    String nameToSignUp=scanner.nextLine();
                    System.out.println("Enter the password to signup");
                    String passwordToSignup=scanner.nextLine();
                    User userToSignup= new User(nameToSignUp,passwordToSignup, UserServiceUtil.hashedPassword(passwordToSignup),new ArrayList<>(),randomUUID().toString());
                    userBookingService.signUp(userToSignup);
                    break;
                case 2:
                    System.out.println("Enter username to Login");
                    String nameToLogin=scanner.nextLine();
                    System.out.println("Enter password to Login");
                    String passwordToLogin=scanner.nextLine();

                    Optional<User> success= userBookingService.loginUser(nameToLogin,passwordToLogin);
                    if(success.isPresent()){
                        userBookingService.setUser(success.get());
                        System.out.println("Login successful");
                    }else{
                        System.out.println("Login failed");
                    }
                    break;
                case 3:
                    if(userBookingService.getUser() == null) {
                        System.out.println("Please login first to view your bookings (option 2)");
                        break;
                    }
                    System.out.println("Fetching ur Bookings");
                    userBookingService.fetchBooking();
                    break;
                case 4:
                    System.out.println("Type ur source station");
                    String source=scanner.nextLine();
                    System.out.println("Type ur destination station");
                    String dest=scanner.nextLine();
                    List<Train> trains=userBookingService.getTrains(source,dest);
                    int index=1;
                    for(Train t:trains){
                        System.out.println(index+" Train id: "+t.getTrainId());
                        if(t.getStationTimes() != null) {
                            for(Map.Entry<String,String> entry: t.getStationTimes().entrySet()){
                                System.out.println("Station: "+ entry.getKey()+" Time: "+entry.getValue());
                            }
                        } else {
                            System.out.println("No station times available");
                        }
                    }
                    if(!trains.isEmpty()) {
                        System.out.println("Select Trains by typing 1,2,3...");
                        try {
                            int trainChoice = scanner.nextInt();
                            if(trainChoice > 0 && trainChoice <= trains.size()) {
                                trainSelectedForBooking = trains.get(trainChoice - 1);
                                System.out.println("Train selected: " + trainSelectedForBooking.getTrainId());
                            } else {
                                System.out.println("Invalid train selection");
                            }
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            scanner.nextLine();
                        } catch (java.util.NoSuchElementException e) {
                            System.out.println("No input available for train selection");
                        }
                    } else {
                        System.out.println("No trains found for the given route");
                    }
                    break;
                case 5:
                    if(userBookingService.getUser() == null) {
                        System.out.println("Please login first before booking a ticket (option 2)");
                        break;
                    }
                    try{
                        if(trainSelectedForBooking.getSeats() == null) {
                            System.out.println("Please search and select a train first (option 4)");
                            break;
                        }
                        System.out.println("Select a seat out of these seats");
                        List<List<Integer>> seats=userBookingService.fetchSeats(trainSelectedForBooking);
                        for(List<Integer> row:seats){
                            for(Integer val:row){
                                System.out.print(val+" ");
                            }
                            System.out.println();
                        }
                        System.out.println("Select the seat by typing the row and column: ");
                        System.out.println("Enter Row: ");
                        try {
                            int row=scanner.nextInt();
                            System.out.println("Enter Column: ");
                            int column=scanner.nextInt();
                            System.out.println("Booking ur seat....");
                            Boolean booked=userBookingService.bookTrainSeat(trainSelectedForBooking,row,column);
                            if(booked.equals(Boolean.TRUE)){
                                System.out.println("Booked , Enjoy ur journey <3");
                            }else{
                                System.out.println("Can't book this seat");
                            }
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Invalid input. Please enter numbers for row and column.");
                            scanner.nextLine();
                        } catch (java.util.NoSuchElementException e) {
                            System.out.println("No input available for seat selection");
                        }
                        break;
                    }catch (IOException e){
                        System.out.println("Something went wrong");
                    }
                    break;
                case 6:
                    if(userBookingService.getUser() == null) {
                        System.out.println("Please login first to cancel bookings (option 2)");
                        break;
                    }
                    System.out.println("Enter ticket id to cancel: ");
                    String ticketId=scanner.next();
                    userBookingService.cancelBooking(ticketId);
                    break;
            }
        }
        scanner.close();
    }
}