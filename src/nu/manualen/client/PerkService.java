package nu.manualen.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("perk")
public interface PerkService extends RemoteService {
    void addBacker(int id, String name, String address, String postNumber, String town, String nick, String email, String pref, String perk, int amount, boolean mentioned) throws IllegalArgumentException;
    int getAmount();
    ArrayList<String> getBackers();
    int getQty(String perk);
}
