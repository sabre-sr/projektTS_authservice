package projekt.ts.authservice;

import java.io.Serializable;

public class AuthUser extends User implements Serializable {
    private String hash;
    private byte[] salt;

    public AuthUser(){
        super();
    }

    public AuthUser(int id) {
        super(id);
    }

    public AuthUser(int id, String hash, byte[] salt) {
        super(id);
        this.hash = hash;
        this.salt = salt;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}
