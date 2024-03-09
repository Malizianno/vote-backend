package ro.cristiansterie.vote.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ro.cristiansterie.vote.entity.CandidateDAO;
import ro.cristiansterie.vote.entity.UserDAO;


public class EntityHelper {
    private static final String[] FIRST_NAMES = new String[] { "Mihai", "Andrei", "George", "Cristian", "Ion", "Paul",
            "Ștefan", "Alexandru", "Petru", "Florin" };
    private static final String[] LAST_NAMES = new String[] { "Popescu", "Ionescu", "Gabor", "Iliescu", "Petrescu",
            "Chiricheș", "Protopopescu", "Deaconu", "Pamfil", "Pretop" };
    private static final String[] PARTIES = new String[] { "PSD", "PNL", "USR", "UDMR", "PMP", "POT", "PD-L",
            "Noua Dreaptă", "Independent" };

    private static Random random = new Random();

    public static List<CandidateDAO> generateFakeCandidates(int no) {
        List<CandidateDAO> returnable = new ArrayList<>();

        for (int i = 0; i < no; i++) {
            returnable.add(generateCandidateDAO());
        }

        return returnable;
    }

    public static List<UserDAO> generateFakeUsers(int no) {
        List<UserDAO> returnable = new ArrayList<>();

        for (int i = 0; i < no; i++) {
            returnable.add(generateUserDAO());
        }

        return returnable;
    }

    private static CandidateDAO generateCandidateDAO() {
        CandidateDAO dto = new CandidateDAO();

        dto.setFirstName(FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]);
        dto.setLastName(LAST_NAMES[random.nextInt(LAST_NAMES.length)]);
        dto.setParty(PARTIES[random.nextInt(PARTIES.length)]);
        dto.setDescription(
                "Numele meu este " + dto.getFirstName() + " " + dto.getLastName()
                        + ("Independent".equals(dto.getParty())
                                ? " și sunt "
                                : " și fac parte din partidul ")
                        + dto.getParty()
                        + " și particip anul acesta la alegerile prezidențiale, cu dorința de a schimba România!");
        dto.setImage("https://100k-faces.glitch.me/random-image");

        return dto;
    }

    private static UserDAO generateUserDAO() {
        UserDAO dto = new UserDAO();

        dto.setUsername(
                FIRST_NAMES[random.nextInt(FIRST_NAMES.length)].toLowerCase() + "." + LAST_NAMES[random.nextInt(LAST_NAMES.length)].toLowerCase());

        return dto;
    }
}
