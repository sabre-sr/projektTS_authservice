package projekt.ts.authservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public AuthUser login(@RequestBody AuthUser user) throws SQLException {
        AuthUser userDb = Database.database.getUser(user);
        if (user.getHash().equals(userDb.getHash())) {
            return user;
        }
        else throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @PostMapping(path = "register")
    public AuthUser register(@RequestBody AuthUser user) throws SQLException {
        Database.database.addUser(user);
        return Database.database.getUser(new User(user.getId()));
    }

}
