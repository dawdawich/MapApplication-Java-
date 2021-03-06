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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;


    //TODO: complete checking for identically nicknames
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(HttpEntity<String> json)
    {
        try {
            JSONObject jsonObject = new JSONObject(json.getBody());

            String nickname = jsonObject.getString("nickname");
            String password = jsonObject.getString("password");
            String email = jsonObject.getString("email");

            UserEntity user = userRepository.findByEmail(email);

            if (user == null) {


                user = new UserEntity();

                user.setNickname(nickname);
                user.setPassword(password);
                user.setEmail(email);

                userRepository.save(user);

                JSONObject msg = new JSONObject();
                JSONObject userJson = new JSONObject();
                userJson.put("nickname", nickname);
                userJson.put("password", password);
                userJson.put("email", email);
                msg.put("error", false);
                msg.put("user", userJson);

                return msg.toString();
            }
            else
            {
                JSONObject msg = new JSONObject();
                msg.put("error", true);
                msg.put("error_msg", "Account already exists!");
                return msg.toString();
            }



        } catch (JSONException e) {
            e.printStackTrace();
            JSONObject msg = new JSONObject();
            try {
                msg.put("error", true);
                msg.put("error_msg", "Wrong json");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return msg.toString();
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpEntity<String> json)
    {
        try {
            JSONObject jsonObject = new JSONObject(json.getBody());
            UserEntity user = userRepository.findByNickname(jsonObject.getString("nickname"));

            if (user != null && user.getPassword().equals(jsonObject.getString("password")))
            {
                GsonBuilder formatterAdapter = new GsonBuilder();
                formatterAdapter.registerTypeAdapter(UserEntity.class, new UserEntityAdapter());
                Gson formatter = formatterAdapter.create();
                JSONObject msg = new JSONObject();
                msg.put("error", false);
                msg.put("user", new JSONObject(formatter.toJson(user)));

                return msg.toString();
            }
            else
            {
                JSONObject msg = new JSONObject();
                msg.put("error", true);
                msg.put("error_msg", "Login failed!");
                return msg.toString();
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        JSONObject response = new JSONObject();
        try {
            response.put("error", true);
            response.put("error_msg", "Login failed!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

}
