package projekt.ts.authservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> login(@RequestBody AuthUser user) throws SQLException {
        AuthUser userDb = Database.database.getUser(user);
        ResponseEntity<String> temp;
        if (userDb.getHash().equals(user.getHash())) {
            temp = new ResponseEntity<>(HttpStatus.OK);
        }
        else temp= new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return temp;
    }

    @PostMapping(path = "register")
    public AuthUser register(@RequestBody AuthUser user) throws SQLException {
        Database.database.addUser(user);
        return Database.database.getUser(new User(user.getId()));
    }

}
