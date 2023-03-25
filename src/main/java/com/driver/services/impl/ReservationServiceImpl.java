package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
//        ReservationController: Reserve a spot in a given parking lot for a specific user and vehicle,
//        ensuring that the total price for the reservation is minimized.
//        The price per hour for each spot is different, and the vehicle can only be parked in
//        a spot with a type that is equal to or larger than the given vehicle.
//        In the event that the parking lot is not found, the user is not found or no spot is available,
//        the system should throw an exception indicating that the reservation cannot be made.
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        if(parkingLot==null) throw new Exception("parking lot is not found");
        User user = userRepository3.findById(userId).get();
        if(user==null) throw new Exception("user is not found");
        List<Spot> spotList = parkingLotRepository3.findById(parkingLotId).get().getSpotList();
        Spot spot1 = null;
        int totalprice=Integer.MIN_VALUE;
        for(Spot spot : spotList){
            if(numberOfWheels==2 && !spot.getOccupied()){
                if(spot.getPricePerHour()*timeInHours < totalprice ){
                    totalprice = spot.getPricePerHour()*timeInHours;
                    spot1=spot;
                }
            } else if (numberOfWheels==2 && numberOfWheels==4 && !spot.getOccupied()) {
                if(spot.getPricePerHour()*timeInHours < totalprice ){
                    totalprice = spot.getPricePerHour()*timeInHours;
                    spot1=spot;
                }
            }
            else{
                if(!spot.getOccupied()){
                    if(spot.getPricePerHour()*timeInHours < totalprice ){
                        totalprice = spot.getPricePerHour()*timeInHours;
                        spot1=spot;
                    }
                }
            }
        }
        if(spot1==null) throw new Exception("no spot is available");
        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setUser(user);
        reservation.setSpot(spot1);

        reservationRepository3.save(reservation);

        return reservation;

    }
}
