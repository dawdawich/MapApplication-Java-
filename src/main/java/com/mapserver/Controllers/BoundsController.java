package com.mapserver.Controllers;


import com.google.gson.Gson;
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

//            UserEntity from = userRepository.findByNickname(jsonObject.getString("user_from"));
            UserEntity from = userRepository.findById(jsonObject.getInt("user_from_id"));
//            UserEntity to = userRepository.findByNickname(jsonObject.getString("user_to"));
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

            from.setGetUpdate(true);
            to.setGetUpdate(true);

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
//            InviteEntity inviteEntity = inviteRepository.findByFromNicknameAndToNickname(jsonObject.getString("user_from"), jsonObject.getString("user_to"));
            InviteEntity inviteEntity = inviteRepository.findByFromIdAndToId(jsonObject.getInt("user_from_id"), jsonObject.getInt("user_to_id"));
            inviteEntity.getFrom().getFriends().add(inviteEntity.getTo());
            inviteEntity.getFrom().setGetUpdate(true);
            inviteEntity.getTo().setGetUpdate(true);
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
                return msg.toString();
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
//            UserEntity user = userRepository.findByNickname(jsonObject.getString("nickname"));
            UserEntity user = userRepository.findById(jsonObject.getInt("id"));

            List<Map<String, Object>> friends = new ArrayList<>();

            for (UserEntity friend : user.getFriends())
            {
                Map<String, Object> friend_data = new HashMap<>();
                friend_data.put("id", friend.getId());
                friend_data.put("nickname", friend.getNickname());
                friends.add(friend_data);
            }

            JSONObject msg= new JSONObject();
            msg.put("error", false);
            msg.put("friends", friends);

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

    @RequestMapping(path = "/search_friends", consumes = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public String searchFriends(HttpEntity<String> data)
    {
        try {
            JSONObject jsonObject = new JSONObject(data.getBody());
//            UserEntity user = userRepository.findByNickname(jsonObject.getString("nickname"));
            String nickname = jsonObject.getString("nickname");

            Set<UserEntity> users = userRepository.findByNicknameContains(nickname);



            JSONObject msg= new JSONObject();
            JSONArray usersArray = new JSONArray(new Gson().toJson(users));
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

}
