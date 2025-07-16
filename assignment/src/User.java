import java.util.ArrayList;
import java.util.List;

public class User {
    protected int id;
    protected String username;
    protected String email;
    
    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
    
    public List<String> getUserInformation(){
        List<String> info = new ArrayList<>();
        info.add(String.valueOf(id));
        info.add(username);
        info.add(email);
        return info;
    }
}
