package com.example.ratingsservice.resources;

import com.example.ratingsservice.models.Rating;
import com.example.ratingsservice.models.UserRating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ratingsservice.DButil.RatingDB;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("/ratings")
public class RatingsResource {

    private Connection connection;
    public RatingsResource() {
        connection = RatingDB.getConnection();
    }
    
    @RequestMapping("/{userId}")
    public UserRating getRatting (@PathVariable String userId){
        List<Rating> ratings = new ArrayList<>();
        try {
            PreparedStatement state = connection.prepareStatement("SELECT movie_id,rating from rating where user_id = " + userId);
            ResultSet resultSet = state.executeQuery();
            while (resultSet.next()){
                Rating r = new Rating(resultSet.getString(1),resultSet.getInt(2));
                ratings.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return new UserRating(ratings);
    }
}
