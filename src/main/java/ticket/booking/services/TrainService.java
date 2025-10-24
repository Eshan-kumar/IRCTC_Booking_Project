package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class TrainService {
    private  List<Train> trainList;
    private static ObjectMapper objectMapper= new ObjectMapper();

    private static final String TRAINS_PATH="src/main/java/ticket/booking/localDB/trains.json";

    public TrainService()throws IOException{
        File trains=new File(TRAINS_PATH);
        trainList=objectMapper.readValue(trains,new TypeReference<List<Train>>() {});
    }

    public List<Train> searchTrains(String source, String dest){
        return trainList.stream().filter(train-> validTrain(train,source,dest)).collect(Collectors.toList());
    }

    private void saveTrainListToFile() throws IOException{
        File trainfile=new File(TRAINS_PATH);
        objectMapper.writeValue(trainfile,trainList);
    }

    public void addTrain(Train newTrain) throws IOException{
        Optional<Train> existingTrain=trainList.stream().filter(train->train.getTrainId().equalsIgnoreCase(newTrain.getTrainId())).findFirst();

        if(existingTrain.isPresent()){
            updateTrain(newTrain);
        }else{
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train updatedTrain) throws IOException{
        boolean exists = trainList.stream()
                .anyMatch(train -> train.getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()));

        if (exists) {
            trainList = trainList.stream()
                    .map(train -> train.getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()) ? updatedTrain : train)
                    .collect(Collectors.toList());
        } else {
            trainList.add(updatedTrain);
        }

        saveTrainListToFile();
    }


    private Boolean validTrain(Train train,String source,String dest){
        List<String> stationOrder=train.getStations();
        int sourceIndex=stationOrder.indexOf(source.toLowerCase());
        int destIndex=stationOrder.indexOf(dest.toLowerCase());
        return sourceIndex!= -1&& destIndex!=-1 && sourceIndex<destIndex;
    }


}

