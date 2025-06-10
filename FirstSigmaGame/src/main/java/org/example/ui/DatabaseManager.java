package org.example.ui;
import org.example.classes.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.classes.Player;
import java.time.LocalDateTime;


/// ///////////////////////////////////////////////////////////////////////////POŁĄCZENIE Z BAZĄ DANYCH
public class DatabaseManager {
    private static EntityManagerFactory emf;
    private static final String PERSISTENCE_UNIT_NAME = "sigma-game-pu";

///////////////////////////////////////////////////////////////////POCZATEK
    public static void initialize() {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            System.out.println("Połączono");
        } catch (Exception e) {
            System.err.println("Błąd: " + e.getMessage());
            e.printStackTrace();
        }
    }




/// //////////////////////////////////////////////////////////KONIEC
public static void close() {
    if (emf != null && emf.isOpen()) {
        emf.close();
        System.out.println("KONIEC");
    }
}

/// /////////////////////////////////METODY DO TWORZENIA GRACZA:
    public static boolean saveLoginSession(String playerNick) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();

            Login session = new Login(playerNick);

            em.getTransaction().begin();
            em.persist(session);
            em.getTransaction().commit();

            System.out.println("zapisano sesje:" + session);
            return true;

        } catch (Exception e) {
            System.err.println("Błąd podczas zapisywania SESJI: " + e.getMessage());
            e.printStackTrace();

            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public static boolean checkPlayerExists(String playerNick) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();

            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Player p WHERE p.playerName = :nick", Long.class)
                    .setParameter("nick", playerNick)
                    .getSingleResult();

            return count > 0;

        } catch (Exception e) {
            System.err.println("Błąd podczas sprawdzania gracza: " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public static boolean createPlayerIfNotExists(String playerNick) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();

            // Sprawdź czy gracz już istnieje
            if (checkPlayerExists(playerNick)) {
                System.out.println("Gracz " + playerNick + " już istnieje w bazie.");
                return true;
            }


            Player newPlayer = new Player();
            newPlayer.setPlayerName(playerNick);
            newPlayer.setPlayerSurname(""); // Domyślnie puste
            newPlayer.setPlayerAge(0);
            newPlayer.setPlayerGender("Unknown");
            newPlayer.setPlayerPassword(""); // Bez hasła na razie
            newPlayer.setPlayerScore("0");

            em.getTransaction().begin();
            em.persist(newPlayer);
            em.getTransaction().commit();

            System.out.println("Nowy gracz: " + playerNick);
            return true;

        } catch (Exception e) {
            System.err.println("Błąd podczas tworzenia gracza: " + e.getMessage());
            e.printStackTrace();

            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


}
