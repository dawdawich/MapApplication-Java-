package com.mapserver.Controllers;


import com.mapserver.Entities.InviteEntity;
import com.mapserver.Entities.UserEntity;
import com.mapserver.Repositories.InviteRepository;
import com.mapserver.Repositories.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

            UserEntity from = userRepository.findByNickname(jsonObject.getString("user_from"));
            UserEntity to = userRepository.findByNickname(jsonObject.getString("user_to"));

            if (from == null || to == null)
            {
                JSONObject msg = new JSONObject();
                msg.put("error", false);
                msg.put("error_msg", "profile not allowed!");
                return msg.toString();
            }

            InviteEntity invite = new InviteEntity();
            invite.setFrom(from);
            invite.setTo(to);
            invite.setStatus(false);

            inviteRepository.save(invite);

            JSONObject msg = new JSONObject();
            msg.put("error", false);

            return msg.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            JSONObject msg = new JSONObject();
            try {
                msg.put("error", false);
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

            InviteEntity inviteEntity = inviteRepository.findByFromNicknameAndToNickname(jsonObject.getString("user_from"), jsonObject.getString("user_to"));

            inviteEntity.getFrom().getFriends().add(inviteEntity.getTo());

            userRepository.save(inviteEntity.getFrom());

            inviteRepository.delete(inviteEntity);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "s";

    }


}
