package e2b.model.request;


import com.e2b.utils.GsonUtils;
import com.google.gson.JsonObject;

/**
 * Class is used to base request structure.
 */

public class BaseRequest<T> {

    public static <T> T fromJson(Class<T> tClass, String s) {
        return GsonUtils.parseJson(s, tClass);
    }

    public String toJson() {
        return GsonUtils.getJson(this);
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("a", GsonUtils.getJsonTree(this));
        return jsonObject.getAsJsonObject("a");
    }

    public String emptyJson() {
        return "{}";
    }
}
