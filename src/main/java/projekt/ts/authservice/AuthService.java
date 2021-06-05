package projekt.ts.authservice;

import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class AuthService {

    @PostMapping(path = "authenticate")
    public boolean authenticateUser(AuthUser user) throws SQLException {
        AuthUser userValid = Database.database.getUser(new User(user.getId()));
        return user.getHash().equals(userValid.getHash());
    }

    @GetMapping(path = "getsalt/{id}")
    public AuthUser getSalt(@PathVariable int id) throws SQLException {
        return Database.database.getSalt(new User(id));
    }

    @PostMapping(path = "/login")
    public Boolean login(@RequestBody AuthUser user) throws SQLException {
        AuthUser userDb = Database.database.getUser(user);
        return userDb.getHash().equals(user.getHash());
    }

    // TODO: możemy zrobić te maile może?

}
