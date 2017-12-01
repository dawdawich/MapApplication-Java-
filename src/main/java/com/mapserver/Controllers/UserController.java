package com.mapserver.Controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapserver.Adapter.UserEntityAdapter;
import com.mapserver.Entities.UserEntity;
import com.mapserver.Helper.Helper;
import com.mapserver.Repositories.UserRepository;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            msg.put("is_update", userRepository.findById(jsonObject.getInt("id")).getUpdate());

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


            UserEntity user = userRepository.findById(jsonObject.getInt("id"));
            if (user == null)
            {
                JSONObject msg = new JSONObject();
                msg.put("error", true);
                msg.put("error_msg", "User is null");

                return msg.toString();
            }
            user.combineFriends();
            JSONObject msg = new JSONObject();
            msg.put("error", false);
            msg.put("user", new JSONObject(formatter.toJson(user)));
            user.setUpdate(false);
            userRepository.save(user);

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

    @RequestMapping(path = "/send_avatar")
    @ResponseBody
    public String sendAvatar(HttpEntity<String> data)
    {
        try {
            JSONObject jsonObject = new JSONObject(data.getBody());

            UserEntity user = userRepository.findById(jsonObject.getInt("id"));
            if (user == null)
            {
                JSONObject msg = new JSONObject();
                msg.put("error", true);

                return msg.toString();
            }

            if (user.getAvatar_path() != null && !user.getAvatar_path().equals(""))
            {
                new File(user.getAvatar_path()).delete();
            }

            String sAvatar = jsonObject.getString("avatar");
            byte[] bAvatar = Base64.decode(sAvatar.getBytes());
            String name = String.format("%s(%s).png", user.getNickname(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            String path = String.format("G:\\locator\\%s\\%s", user.getNickname(), name);
            File file = new File(path);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(bAvatar);
                os.flush();
            }
            JSONObject msg = new JSONObject();
            msg.put("error", false);
            msg.put("avatar_name", name);
            user.setAvatar_path(path);
            user.setAvatar_name(name);
            userRepository.save(user);

            return msg.toString();

        } catch (JSONException | Base64DecodingException | IOException e) {
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

    @RequestMapping(path = "/get_avatar")
    @ResponseBody
    public String getAvatar(HttpEntity<String> data)
    {
        try {
            JSONObject jsonObject = new JSONObject(data.getBody());

            UserEntity user = userRepository.findById(jsonObject.getInt("id"));
            if (user == null)
            {
                JSONObject msg = new JSONObject();
                msg.put("error", true);

                return msg.toString();
            }

//            Path path = Paths.get(user.getAvatar_path());
//            byte[] bytes = Files.readAllBytes(path);
            String bAvatar = Base64.encode(Helper.loadFile(new File(user.getAvatar_path())));

            JSONObject msg = new JSONObject();
            msg.put("error", false);
            msg.put("avatar", bAvatar);
            msg.put("id", user.getId());
            msg.put("avatar_name", user.getAvatar_name());
            return msg.toString();

        } catch (JSONException | IOException e) {
            e.printStackTrace();

            JSONObject msg = new JSONObject();

            try {
                msg.put("error", true);
                msg.put("error_msg", "no avatar");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return msg.toString();
        }
    }

    @RequestMapping(path = "/is_same_avatar")
    @ResponseBody
    public String isSameAvatar(HttpEntity<String> data)
    {
        try {
            JSONObject jsonObject = new JSONObject(data.getBody());

            UserEntity user = userRepository.findById(jsonObject.getInt("id"));
            if (user == null)
            {
                JSONObject msg = new JSONObject();
                msg.put("error", true);

                return msg.toString();
            }

            String name = user.getAvatar_name() != null ? user.getAvatar_name() : "";
            String currentName = jsonObject.getString("avatar_name");


            JSONObject msg = new JSONObject();
            msg.put("error", false);
            msg.put("id", user.getId());
            msg.put("isSame", name.equals(currentName));
            return msg.toString();

        } catch (JSONException e) {
            e.printStackTrace();

            JSONObject msg = new JSONObject();

            try {
                msg.put("error", true);
                msg.put("error_msg", "no avatar");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return msg.toString();
        }
    }

}
