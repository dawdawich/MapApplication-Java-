package com.mapserver.Adapter;

import com.google.gson.*;
import com.mapserver.Entities.InviteEntity;
import com.mapserver.Entities.UserEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class UserEntityAdapter implements JsonSerializer<UserEntity> {

    @Override
    public JsonElement serialize(UserEntity src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("nickname", src.getNickname());
        jsonObject.addProperty("email", src.getEmail());
        JsonArray incomingInvites = null;
        if (src.getIncomingInvites() != null && src.getIncomingInvites().size() > 0) {
            incomingInvites = new JsonArray();
            for (InviteEntity user : src.getIncomingInvites()) {
                JsonObject from = new JsonObject();
                from.addProperty("id", user.getFrom().getId());
                from.addProperty("nickname", user.getFrom().getNickname());
                incomingInvites.add(from);
            }
        }
        jsonObject.add("incomingInvites", incomingInvites);
        JsonArray outcomingcomingInvites = null;
        if (src.getOutcomingInvite() != null && src.getOutcomingInvite().size() > 0) {
            outcomingcomingInvites = new JsonArray();
            for (InviteEntity user : src.getOutcomingInvite()) {
                JsonObject to = new JsonObject();
                to.addProperty("id", user.getTo().getId());
                to.addProperty("nickname", user.getTo().getNickname());
                outcomingcomingInvites.add(to);
            }
        }
        jsonObject.add("outcomingcomingInvites", outcomingcomingInvites);
        JsonArray friends = null;
        if (src.getFriends() != null && src.getFriends().size() > 0) {
            friends = new JsonArray();
            for (UserEntity user : src.getFriends()) {
                JsonObject friend = new JsonObject();
                friend.addProperty("id", user.getId());
                friend.addProperty("nickname", user.getNickname());
                friends.add(friend);
            }
        }
        jsonObject.add("friends", friends);
        return jsonObject;
    }
}
