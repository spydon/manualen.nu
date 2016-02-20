package nu.manualen.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import nu.manualen.client.PerkService;

@SuppressWarnings("serial")
public class PerkServiceImpl extends DBConnector implements PerkService {

    @Override
    public void addBacker(int id, String name, String address, String postNumber,
            String town, String nick, String email, String pref, String perk, int amount, boolean mentioned) throws IllegalArgumentException {
        Connection conn = getConnection();

        try {
            if (qtyLeft(perk)) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO backers ("
                        + "id, "
                        + "name, "
                        + "address, "
                        + "post_number, "
                        + "city, nick, "
                        + "email, "
                        + "pref, "
                        + "amount, "
                        + "mentioned, "
                        + "perk) "
                        + "VALUES "
                        + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, address);
                ps.setString(4, postNumber);
                ps.setString(5, town);
                ps.setString(6, nick);
                ps.setString(7, email);
                ps.setString(8, pref);
                ps.setInt(9, amount);
                ps.setBoolean(10, mentioned);
                ps.setString(11, perk);

                ps.executeUpdate();
                ps.close();
                conn.close();
            } else {
                throw new IllegalArgumentException("Det fanns inget kvar i den kategorin");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getAmount() {
        Connection conn = getConnection();
        int total = 0;
        try {
//            PreparedStatement ps = conn.prepareStatement("SELECT SUM(amount) as total FROM backers WHERE payed = 1");
            PreparedStatement ps = conn.prepareStatement("SELECT SUM(amount) as total FROM backers");
            ResultSet rs = ps.executeQuery();
            rs.first();
            total = rs.getInt("total");
            ps.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    @Override
    public int getQty(String perk) {
        Connection conn = getConnection();
        int total = 0;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) as sold FROM backers WHERE perk = ?");
            ps.setString(1, perk);
            ResultSet rs = ps.executeQuery();
            rs.first();
            total = lazyUglyHackChecker(perk)-rs.getInt("sold");
            ps.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    @Override
    public ArrayList<String> getBackers() {
        Connection conn = getConnection();
        ArrayList<String> backers = new ArrayList<String>();
        try {
//            PreparedStatement ps = conn.prepareStatement("SELECT nick FROM backers WHERE payed = 1 AND mentioned = 1 ORDER BY amount desc");
//            PreparedStatement ps = conn.prepareStatement("SELECT nick FROM backers WHERE mentioned = 1 ORDER BY amount desc");
            PreparedStatement ps = conn.prepareStatement("SELECT nick, total FROM (SELECT nick, sum(amount) as total FROM backers WHERE mentioned = 1 GROUP BY nick) as grouped ORDER BY total desc;");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                backers.add(rs.getString("nick"));
            }
            ps.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return backers;
    }

    private boolean qtyLeft(String perk) {
        return getQty(perk) > 0;
    }

    //Do this in database instead
    private int lazyUglyHackChecker(String perk) {
        switch(perk) {
        case "Märke<br /> Limited Edition™":
            return 10;
        case "Manualen i Digital Version":
            return 100;
        case "Manualen v11111011111":
            return 100;
        case "Nämnd i boken":
            return 20;
        case "Ny sång skriven<br />och tillägnad dig":
            return 3;
        case "Egen sida (Ej reklam)":
            return 4;
        case "Reklam (Helsida)":
            return 2;
        case "Donera fritt<br />utan att få någonting":
            return 1000;
        case "Overallssittningen<br />21 November<br />(Märke ingår)":
            return 54;
        case "Manualsläppsfesten<br />(När den släpps)":
            return 0;
        default:
            return 0;
        }
    }
}