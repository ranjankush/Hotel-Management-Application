package com.example.HotelManagement.repository;

import com.example.HotelManagement.model.Room;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    @Aggregation("{ $group: { _id: '$roomType'} }") //get all rooms types base on their uniqueness
    List<String> findDistinctRoomType();

    @Query("{ 'bookings': {$size: 0 } }") //find room that has no bookings
    List<Room> findAllAvailableRooms();

    List<Room> findByRoomTypeLikeAndIdNotIn(String roomType, List<String> bookedRoomIds);
}
