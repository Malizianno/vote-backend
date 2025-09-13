package ro.cristiansterie.vote.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ro.cristiansterie.vote.entity.CandidateDAO;
import ro.cristiansterie.vote.entity.UserDAO;

public class EntityHelper {
    private static final String[] FIRST_NAMES = new String[] { "Mihai", "Ana", "Andrei", "Adela", "George", "Andreea",
            "Cristian", "Angela", "Ion", "Bianca", "Paul", "Ștefania", "Ștefan", "Anca", "Alexandru", "Vasilica",
            "Petru", "Veta", "Florin" };
    private static final String[] LAST_NAMES = new String[] { "Anghelescu", "Popescu", "Aftenie", "Ionescu",
            "Adumitroaiei", "Dîrlău", "Gabor", "Decusară", "Iliescu", "Deju", "Petrescu", "Hârlișcă", "Chiricheș",
            "Halbeș", "Kogălniceanu", "Kiriță", "Protopopescu", "Deaconu", "Pamfil", "Pretop", "Oală", "Similea" };

    private static Random random = new Random();

    private EntityHelper() {
        // empty private constructor
    }

    public static List<CandidateDAO> generateFakeCandidates(int electionID) {
        List<CandidateDAO> returnable = new ArrayList<>();

        PartyTypeEnum[] parties = PartyTypeEnum.values();

        for (int i = 1; i < parties.length; i++) {
            returnable.add(generateCandidateDAO(parties[i], electionID));
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

    private static CandidateDAO generateCandidateDAO(PartyTypeEnum party, int electionID) {
        CandidateDAO dto = new CandidateDAO();

        dto.setFirstName(FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]);
        dto.setLastName(LAST_NAMES[random.nextInt(LAST_NAMES.length)]);
        dto.setParty(party);
        dto.setElectionId(electionID);
        dto.setDescription(
                "Numele meu este " + dto.getFirstName() + " " + dto.getLastName()
                        + (PartyTypeEnum.IND.equals(dto.getParty())
                                ? " și sunt "
                                : " și fac parte din partidul ")
                        + dto.getParty()
                        + " și particip anul acesta la alegerile prezidențiale, cu dorința de a schimba România!");
        dto.setImage("https://picsum.photos/200/300");

        return dto;
    }

    private static UserDAO generateUserDAO() {
        UserDAO dto = new UserDAO();

        dto.setRole(UserRoleEnum.ADMIN);
        dto.setPassword("$2a$10$f/QdFjL002VC5.ZmhEtW9eKqUJfn0CvvzIr.dbW/0h5Eu3q1d75ga");
        dto.setUsername(
                FIRST_NAMES[random.nextInt(FIRST_NAMES.length)].toLowerCase() + "."
                        + LAST_NAMES[random.nextInt(LAST_NAMES.length)].toLowerCase());

        return dto;
    }
}
