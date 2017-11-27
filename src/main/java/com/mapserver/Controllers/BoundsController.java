package com.mapserver.Controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapserver.Adapter.UserEntityAdapter;
import com.mapserver.Entities.InviteEntity;
import com.mapserver.Entities.UserEntity;
import com.mapserver.Repositories.InviteRepository;
import com.mapserver.Repositories.UserRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class BoundsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InviteRepository inviteRepository;

    @RequestMapping(path = "/send_invitation")
    @ResponseBody
    public String sendInvitation(HttpEntity<String> data) {

        try {
            JSONObject jsonObject = new JSONObject(data.getBody());

            UserEntity from = userRepository.findById(jsonObject.getInt("user_from_id"));
            UserEntity to = userRepository.findById(jsonObject.getInt("user_to_id"));

            if (from == null || to == null)
            {
                JSONObject msg = new JSONObject();
                msg.put("error", true);
                msg.put("error_msg", "profile not allowed!");
                return msg.toString();
            }

            InviteEntity invite = new InviteEntity();
            invite.setFrom(from);
            invite.setTo(to);
//            invite.setStatus(false);

            inviteRepository.save(invite);

            from.setUpdate(true);
            to.setUpdate(true);

            userRepository.save(from);
            userRepository.save(to);

            JSONObject msg = new JSONObject();
            msg.put("error", false);

            return msg.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            JSONObject msg = new JSONObject();
            try {
                msg.put("error", true);
                msg.put("error_msg", "BoundsController -> sendInvitation()");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return msg.toString();
        }
    }

    @RequestMapping(path = "/confirm_invitation")
    @ResponseBody
    public String confirmInvitation(HttpEntity<String> data)
    {

        try {
            JSONObject jsonObject = new JSONObject(data.getBody());
            InviteEntity inviteEntity = inviteRepository.findByFromIdAndToId(jsonObject.getInt("user_from_id"), jsonObject.getInt("user_to_id"));
            inviteEntity.getFrom().getFriendsPartOne().add(inviteEntity.getTo());
            inviteEntity.getFrom().setUpdate(true);
            inviteEntity.getTo().setUpdate(true);
            userRepository.save(inviteEntity.getFrom());
            userRepository.save(inviteEntity.getTo());
            inviteRepository.delete(inviteEntity);

            JSONObject msg = new JSONObject();
            msg.put("error", false);
            return msg.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            JSONObject msg = new JSONObject();
            try {
                msg.put("error", true);
                msg.put("error_msg", "BoundsController -> confirmInvitation()");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return msg.toString();
        }
    }

    @RequestMapping(path = "/get_friends")
    @ResponseBody
    public String getFriends(HttpEntity<String> data)
    {
        try {
            JSONObject jsonObject = new JSONObject(data.getBody());
            UserEntity user = userRepository.findById(jsonObject.getInt("id"));

            GsonBuilder formatterAdapter = new GsonBuilder();
            formatterAdapter.registerTypeAdapter(UserEntity.class, new UserEntityAdapter());
            Gson formatter = formatterAdapter.create();

            JSONObject msg= new JSONObject();
            msg.put("error", false);
            msg.put("friends", formatter.toJson(user.getFriends()));

            return msg.toString();
        } catch (JSONException e) {

            JSONObject msg = new JSONObject();
            try {
                msg.put("error", true);
                msg.put("error_msg", "BoundsController -> getFriends()");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
            return msg.toString();
        }
    }

    @RequestMapping(path = "/search_friends", consumes = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public String searchFriends(HttpEntity<String> data)
    {
        try {
            JSONObject jsonObject = new JSONObject(data.getBody());
            String nickname = jsonObject.getString("nickname");

            Set<UserEntity> users = userRepository.findByNicknameContains(nickname);

            for (UserEntity user : users)
            {
                user.setFriends(null);
                user.setOutcomingInvite(null);
                user.setIncomingInvites(null);
            }

            GsonBuilder formatterAdapter = new GsonBuilder();
            formatterAdapter.registerTypeAdapter(UserEntity.class, new UserEntityAdapter());
            Gson formatter = formatterAdapter.create();

            JSONObject msg= new JSONObject();
            JSONArray usersArray = new JSONArray(formatter.toJson(users));
            msg.put("error", false);
            msg.put("friends", usersArray);

            return msg.toString();
        } catch (JSONException e) {

            JSONObject msg = new JSONObject();
            try {
                msg.put("error", true);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
            return msg.toString();
        }
    }

    @RequestMapping(path = "/send_invite", consumes = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public String sendInvite(HttpEntity<String> data)
    {
        try {
            JSONObject jsonObject = new JSONObject(data.getBody());
//            UserEntity user = userRepository.findByNickname(jsonObject.getString("nickname"));

            if (inviteRepository.findByFromIdAndToId(jsonObject.getInt("this_user_id"), jsonObject.getInt("current_user_id")) != null ||
                    inviteRepository.findByFromIdAndToId(jsonObject.getInt("current_user_id"), jsonObject.getInt("this_user_id")) != null)
            {
                UserEntity to = userRepository.findById(jsonObject.getInt("current_user_id"));
                GsonBuilder formatterAdapter = new GsonBuilder();
                formatterAdapter.registerTypeAdapter(UserEntity.class, new UserEntityAdapter());
                Gson formatter = formatterAdapter.create();

                JSONObject msg= new JSONObject();
                JSONObject friend = new JSONObject(formatter.toJson(to));
                msg.put("error", false);
                msg.put("friend", friend);
                return msg.toString();
            }


            UserEntity from = userRepository.findById(jsonObject.getInt("this_user_id"));
            UserEntity to = userRepository.findById(jsonObject.getInt("current_user_id"));

            InviteEntity bound = new InviteEntity();
            from.setUpdate(true);
            to.setUpdate(true);
            bound.setTo(to);
            bound.setFrom(from);
            inviteRepository.save(bound);
            userRepository.save(to);
            userRepository.save(from);

            GsonBuilder formatterAdapter = new GsonBuilder();
            formatterAdapter.registerTypeAdapter(UserEntity.class, new UserEntityAdapter());
            Gson formatter = formatterAdapter.create();

            JSONObject msg= new JSONObject();
            JSONObject friend = new JSONObject(formatter.toJson(to));
            msg.put("error", false);
            msg.put("friend", friend);

            return msg.toString();
        } catch (JSONException e) {

            JSONObject msg = new JSONObject();
            try {
                msg.put("error", true);
                msg.put("error_msg", "BoundsController -> sendInvitation()");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
            return msg.toString();
        }
    }

}
