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

            UserEntity user = userRepository.findById(jsonObject.getInt("id"));
            if (user != null)
            {
                if (user.getUser_position() != null) {
                    user.getUser_position().setLongitude(jsonObject.getDouble("longitude"));
                    user.getUser_position().setLatitude(jsonObject.getDouble("latitude"));
//                    userPositionRepository.save(user.getUser_position());
                    userRepository.save(user);
                }
                else
                {
                    UserPositionEntity userPosition = new UserPositionEntity();
                    userPosition.setLongitude(jsonObject.getDouble("longitude"));
                    userPosition.setLatitude(jsonObject.getDouble("latitude"));
                    userPosition.setUserEntity(user);
                    user.setUser_position(userPosition);
                    userRepository.save(user);
                }
            }


            JSONObject response = new JSONObject();
            try {
                response.put("error", false);
                response.put("error_msg", "");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return response.toString();


        } catch (JSONException e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            try {
                response.put("error", true);
                response.put("error_msg", "problems with json");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return response.toString();
        }
    }

    @RequestMapping(path = "/get_position")
    @ResponseBody
    public String getPosition(HttpEntity<String> json)
    {
        try {
            JSONObject jsonObject = new JSONObject(json.getBody());

            UserEntity user = userRepository.findById(jsonObject.getInt("id"));
            if (user != null && user.getUser_position() != null)
            {
                    JSONObject userPos = new JSONObject();
                    JSONObject msg = new JSONObject();
                    userPos.put("id", user.getId());
                    userPos.put("longitude", user.getUser_position().getLongitude());
                    userPos.put("latitude", user.getUser_position().getLatitude());
                    userPos.put("last_update", user.getUser_position().getLast_update().toString());
                    msg.put("error", false);
                    msg.put("user", userPos);
                    return msg.toString();
            }
            else
            {
                JSONObject response = new JSONObject();
                try {
                    response.put("error", true);
                    response.put("error_msg", "Have no user position!");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                return response.toString();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            try {
                response.put("error", true);
                response.put("error_msg", "problems with json!");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return response.toString();
        }
    }

}
