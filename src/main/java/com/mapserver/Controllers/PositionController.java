package com.mapserver.Controllers;


import com.mapserver.Entities.UserEntity;
import com.mapserver.Entities.UserPositionEntity;
import com.mapserver.Repositories.UserPositionRepository;
import com.mapserver.Repositories.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PositionController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserPositionRepository userPositionRepository;

    @RequestMapping(path = "/update_position")
    @ResponseBody
    public String updatePosition(HttpEntity<String> json)
    {
        try {
            JSONObject jsonObject = new JSONObject(json.getBody());

            UserEntity user = userRepository.findByNickname(jsonObject.getString("nickname"));
            if (user != null)
            {
                if (user.getUser_position() != null) {
                    user.getUser_position().setLongitude(jsonObject.getLong("longitude"));
                    user.getUser_position().setLatitude(jsonObject.getLong("latitude"));
                    userPositionRepository.save(user.getUser_position());
                    userRepository.save(user);
                }
                else
                {
                    UserPositionEntity userPosition = new UserPositionEntity();
                    userPosition.setLongitude(jsonObject.getLong("longitude"));
                    userPosition.setLatitude(jsonObject.getLong("latitude"));
                    user.setUser_position(userPosition);
                    userPositionRepository.save(user.getUser_position());
                    userRepository.save(user);
                }
            }


            JSONObject response = new JSONObject();
            try {
                response.put("successful", true);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return response.toString();


        } catch (JSONException e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            try {
                response.put("successful", false);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return response.toString();
        }
    }

}
