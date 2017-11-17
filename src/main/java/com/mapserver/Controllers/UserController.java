package com.mapserver.Controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapserver.Adapter.UserEntityAdapter;
import com.mapserver.Entities.UserEntity;
import com.mapserver.Repositories.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(path = "/is_user_update")
    @ResponseBody
    public String isUserUpdate(HttpEntity<String> data)
    {
        try {

            JSONObject jsonObject = new JSONObject(data.getBody());

            JSONObject msg = new JSONObject();

            msg.put("error", false);
            msg.put("has_update", userRepository.findById(jsonObject.getInt("id")).getGetUpdate());

            return msg.toString();

        } catch (JSONException e) {
            e.printStackTrace();

            JSONObject msg = new JSONObject();

            try {
                msg.put("error", true);
                msg.put("error_msg", "UserController -> isUserUpdate()");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return msg.toString();

        }
    }

    @RequestMapping(path = "/get_user_info")
    @ResponseBody
    public String getUserInfo(HttpEntity<String> data)
    {
        try {
            JSONObject jsonObject = new JSONObject(data.getBody());

            GsonBuilder formatterAdapter = new GsonBuilder();
            formatterAdapter.registerTypeAdapter(UserEntity.class, new UserEntityAdapter());
            Gson formatter = formatterAdapter.create();

            JSONObject msg = new JSONObject();
            msg.put("error", false);
            msg.put("user", new JSONObject(formatter.toJson(userRepository.findById(jsonObject.getInt("id")))));

            return msg.toString();

        } catch (JSONException e) {
            e.printStackTrace();

            JSONObject msg = new JSONObject();

            try {
                msg.put("error", true);
                msg.put("error_msg", "UserController -> getUserInfo()");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return msg.toString();
        }
    }

}
