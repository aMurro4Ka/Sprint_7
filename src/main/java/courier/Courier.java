package courier;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Courier {
    private String login;
    private String password;
    private String firstName;

    // Конструктор с двумя параметрами (без firstName)
    public Courier(String login, String password) {
        this.login = login;
        this.password = password;
        // firstName останется null
    }
}