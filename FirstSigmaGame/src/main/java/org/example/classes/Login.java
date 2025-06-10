package org.example.classes;
import jakarta.persistence.*;
import java.time.LocalDateTime;


/// ////////////////////////////////////////// DANE DO EKRANU POCZATKOWEGO:
@Entity
@Table(name="Users")
public class Login {
/// /////////////////////////////////////////////////////////////ZMIENNE:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="Nickname")
    private String nickname;
    @Column(name = "dateOfLogin")
    private String date;

    /// ////////////////////////////////////////////KONSTRUKTORY:

    public Login() {}
    public Login(String nickname) {
        this.nickname = nickname;
        this.date = LocalDateTime.now().toString();
    }

    /// ////////////////////////////////////////////////GETERY I SETERY:

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return "Login{" +
                "nickname='" + nickname + '\'' +
                ", id=" + id +
                ", date='" + date + '\'' +
                '}';
    }
}
