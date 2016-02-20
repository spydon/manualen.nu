package nu.manualen.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>PerkService</code>.
 */
public interface PerkServiceAsync {
    void addBacker(int id,
            String name,
            String address,
            String postNumber,
            String town,
            String nick,
            String email,
            String pref,
            String perk,
            int amount,
            boolean mentioned,
            AsyncCallback<Void> callback) throws IllegalArgumentException;
    void getAmount(AsyncCallback<Integer> callback);
    void getBackers(AsyncCallback<ArrayList<String>> callback);
    void getQty(String perk, AsyncCallback<Integer> callback);
}
